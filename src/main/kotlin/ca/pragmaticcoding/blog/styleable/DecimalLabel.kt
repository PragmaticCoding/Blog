package ca.pragmaticcoding.blog.styleable

import javafx.beans.binding.StringBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableStringValue
import javafx.beans.value.ObservableValue
import javafx.css.*
import javafx.geometry.Pos
import javafx.scene.control.Label

class DecimalLabel(value: ObservableDoubleValue) : Label() {

    private val decimalPlacesSP: StyleableObjectProperty<Number> =
        SimpleStyleableObjectProperty(DECIMAL_PLACES_META_DATA, 2)
    var decimalPlaces: Int
        get() = decimalPlacesSP.value.toInt()
        set(value) = decimalPlacesSP.set(value)

    fun decimalPlacesProperty(): ObjectProperty<Number> = decimalPlacesSP

    private val negativeFormatSP: StyleableObjectProperty<NegativeFormat> =
        SimpleStyleableObjectProperty(NEGATIVE_FORMAT_META_DATA, NegativeFormat.SIGN)
    var negativeFormat: NegativeFormat
        get() = negativeFormatSP.value
        set(value) = negativeFormatSP.set(value)

    fun negativeFormatProperty(): ObjectProperty<NegativeFormat> = negativeFormatSP

    private val formatSP: StyleableStringProperty =
        SimpleStyleableStringProperty(FORMAT_META_DATA, "")
    var formatString: String
        get() = formatSP.value
        set(value) = formatSP.set(value)

    fun formatStringProperty(): StringProperty = formatSP

    companion object CssStuff {
        val negativePseudoClass: PseudoClass = PseudoClass.getPseudoClass("negative")
        val DECIMAL_PLACES_META_DATA =
            object : CssMetaData<DecimalLabel, Number>("-wfx-decimal-places", StyleConverter.getSizeConverter(), 2) {
                override fun isSettable(p0: DecimalLabel): Boolean = !p0.decimalPlacesSP.isBound

                override fun getStyleableProperty(p0: DecimalLabel): StyleableProperty<Number> = p0.decimalPlacesSP
            }
        val NEGATIVE_FORMAT_META_DATA =
            object : CssMetaData<DecimalLabel, NegativeFormat>(
                "-wfx-negative-format",
                StyleConverter.getEnumConverter(NegativeFormat::class.java),
                NegativeFormat.SIGN
            ) {
                override fun isSettable(p0: DecimalLabel): Boolean = !p0.negativeFormatSP.isBound

                override fun getStyleableProperty(p0: DecimalLabel): StyleableProperty<NegativeFormat> =
                    p0.negativeFormatSP
            }
        val FORMAT_META_DATA = object : CssMetaData<DecimalLabel, String>(
            "-wfx-format", StyleConverter.getStringConverter(), ""
        ) {
            override fun isSettable(p0: DecimalLabel): Boolean = !p0.formatSP.isBound

            override fun getStyleableProperty(p0: DecimalLabel): StyleableStringProperty = p0.formatSP
        }

        private val cssMetaDataList =
            (Label.getClassCssMetaData() + DECIMAL_PLACES_META_DATA + NEGATIVE_FORMAT_META_DATA + FORMAT_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    override fun getControlCssMetaData() = getClassCssMetaData()

    init {
        styleClass += "decimal-label"
        textProperty().bind(ConversionBinding(value, decimalPlacesSP, negativeFormatSP, formatSP))
        alignment = Pos.CENTER_RIGHT
        value.subscribe { newVal -> pseudoClassStateChanged(negativePseudoClass, newVal.toFloat() < 0) }
    }

    enum class NegativeFormat { SIGN, PAREN }

    class ConversionBinding(
        private val labelValue: ObservableDoubleValue,
        private val decimalPlaces: ObservableValue<Number>,
        private val negativeFormat: ObservableValue<NegativeFormat>,
        private val formatString: ObservableStringValue
    ) :
        StringBinding() {
        init {
            super.bind(labelValue, decimalPlaces, negativeFormat, formatString)
        }

        override fun computeValue(): String {
            return if (formatString.value != "")
                String.format(formatString.value, labelValue.value)
            else String.format(
                "%${if (negativeFormat.value == NegativeFormat.PAREN) "(" else ""},." +
                        "${decimalPlaces.value.toInt()}f" +
                        if ((labelValue.value.toFloat() >= 0) && (negativeFormat.value == NegativeFormat.PAREN)) " " else "",
                labelValue.value
            )
        }

    }
}