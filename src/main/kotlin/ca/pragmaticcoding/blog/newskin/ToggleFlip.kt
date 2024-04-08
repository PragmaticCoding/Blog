package ca.pragmaticcoding.blog.newskin


import javafx.beans.binding.Bindings
import javafx.css.*
import javafx.scene.control.Skin
import javafx.scene.control.ToggleButton
import javafx.scene.control.skin.ToggleButtonSkin
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.shape.Circle

class ToggleFlip() : ToggleButton() {

    init {
        styleClass.setAll("toggle-flip")
    }

    override fun createDefaultSkin(): Skin<*>? {
        return ToggleFlipSkin(this)
        super.getCssMetaData()
    }

    companion object CssStuff {
        val BUTTON_SIZE_META_DATA: CssMetaData<ToggleFlip, Number> =
            object : CssMetaData<ToggleFlip, Number>("-wfx-flipper-size", StyleConverter.getSizeConverter()) {
                override fun isSettable(styleable: ToggleFlip) = !styleable.flipperSizeImpl.isBound
                override fun getStyleableProperty(styleable: ToggleFlip) = styleable.flipperSizeImpl
            }
        private val cssMetaDataList =
            (ToggleButton.getClassCssMetaData() + ToggleFlip.BUTTON_SIZE_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    fun flipperSizeProperty() = flipperSizeImpl
    val flipperSizeImpl: StyleableDoubleProperty =
        SimpleStyleableDoubleProperty(BUTTON_SIZE_META_DATA, this, "flipperSize", 12.0)
    var flipperSize: Double
        get() = flipperSizeImpl.value
        set(value) = flipperSizeImpl.set(value)
}


class ToggleFlipSkin(val control: ToggleButton) : ToggleButtonSkin(control) {
    fun flipperSizeProperty() = flipperSizeImpl
    val flipperSizeImpl: StyleableDoubleProperty =
        SimpleStyleableDoubleProperty(ToggleFlip.BUTTON_SIZE_META_DATA, this, "flipperSize", 12.0)
    var flipperSize: Double
        get() = flipperSizeImpl.value
        set(value) = flipperSizeImpl.set(value)

    private val flipperBox: Region = createFlipperBox()

    companion object CssStuff {
        val BUTTON_SIZE_META_DATA: CssMetaData<ToggleButton, Number> =
            object : CssMetaData<ToggleButton, Number>("-wfx-flipper-size", StyleConverter.getSizeConverter()) {
                override fun isSettable(styleable: ToggleButton) =
                    !((styleable.skin as ToggleFlipSkin).flipperSizeImpl.isBound)

                override fun getStyleableProperty(styleable: ToggleButton) =
                    (styleable.skin as ToggleFlipSkin).flipperSizeImpl
            }

        private val cssMetaDataList =
            (ToggleButton.getClassCssMetaData() + BUTTON_SIZE_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    override fun getCssMetaData(): MutableList<CssMetaData<out Styleable, *>> {
        return getClassCssMetaData()
    }

    init {
        control.styleClass.setAll("toggle-flip")
        updateChildren()
    }

    override fun updateChildren() {
        super.updateChildren()
        if (flipperBox != null) {
            children.add(flipperBox)
        }
    }

    private fun createFlipperBox(): Region = AnchorPane().apply {
        val flipperOff = flipper("flipper", false)
        val flipperOn = flipper("flipper", true)
        setFlipperInsets(flipperOff, flipperOn)
        flipperSizeProperty().addListener { _ -> setFlipperInsets(flipperOff, flipperOn) }
        minWidthProperty().bind(flipperSizeProperty().multiply(6.0))
        children += listOf(flipperOff, flipperOn)
        styleClass += "flipper-box"
    }

    private fun flipper(theStyle: String, showWhen: Boolean) = StackPane().apply {
        minWidthProperty().bind(flipperSizeImpl.multiply(2.0))
        minHeightProperty().bind(flipperSizeImpl.multiply(2.0))
        maxHeightProperty().bind(flipperSizeImpl.multiply(2.0))
        maxWidthProperty().bind(flipperSizeImpl.multiply(2.0))
        shape = Circle(10.0)
        styleClass += theStyle
        visibleProperty().bind(
            Bindings.createBooleanBinding(
                { control.isSelected == showWhen },
                control.selectedProperty()
            )
        )
    }

    private fun setFlipperInsets(buttonOff: StackPane, buttonOn: StackPane) {
        val inset = flipperSize / 6.0
        AnchorPane.setLeftAnchor(buttonOff, inset)
        AnchorPane.setTopAnchor(buttonOff, inset)
        AnchorPane.setBottomAnchor(buttonOff, inset)
        AnchorPane.setRightAnchor(buttonOn, inset)
        AnchorPane.setTopAnchor(buttonOn, inset)
        AnchorPane.setBottomAnchor(buttonOn, inset)
    }

    override fun computeMinWidth(
        height: Double,
        topInset: Double,
        rightInset: Double,
        bottomInset: Double,
        leftInset: Double
    ) = super.computeMinWidth(
        height,
        topInset,
        rightInset,
        bottomInset,
        leftInset
    ) + snapSizeX(flipperBox.minWidth(-1.0))


    override fun computeMinHeight(
        width: Double,
        topInset: Double,
        rightInset: Double,
        bottomInset: Double,
        leftInset: Double
    ) = super.computeMinHeight(width - flipperBox.minWidth(-1.0), topInset, rightInset, bottomInset, leftInset)
        .coerceAtLeast(topInset + flipperBox.minHeight(-1.0) + bottomInset)

    override fun computePrefWidth(
        height: Double,
        topInset: Double,
        rightInset: Double,
        bottomInset: Double,
        leftInset: Double
    ) = super.computePrefWidth(
        height,
        topInset,
        rightInset,
        bottomInset,
        leftInset
    ) + snapSizeX(flipperSize * 6.33) + snapSizeX(20.0)


    override fun computePrefHeight(
        width: Double,
        topInset: Double,
        rightInset: Double,
        bottomInset: Double,
        leftInset: Double
    ) = super.computePrefHeight(width - flipperBox.prefWidth(-1.0), topInset, rightInset, bottomInset, leftInset)
        .coerceAtLeast(topInset + flipperBox.prefHeight(-1.0) + bottomInset)


    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
//        val boxWidth = snapSizeX(flipperSize * 6.33)
//        val boxHeight = snapSizeY(flipperSize * 2.33)
//        val computeWidth = control.prefWidth(-1.0).coerceAtLeast(control.minWidth(-1.0))
//        val labelWidth = (computeWidth - boxWidth).coerceAtMost(w - snapSizeX(boxWidth))
//        val labelHeight = control.prefHeight(labelWidth).coerceAtMost(h)
//        val maxHeight = boxHeight.coerceAtLeast(labelHeight)
//        val xOffset = Utils.computeXOffset(w, labelWidth + boxWidth, control.alignment.hpos) + x
//        val yOffset = Utils.computeYOffset(h, maxHeight, control.alignment.vpos) + y
//        layoutLabelInArea(xOffset + boxWidth, yOffset, labelWidth, maxHeight, control.alignment)
//        flipperBox.resize(boxWidth, boxHeight)
//        positionInArea(
//            flipperBox,
//            xOffset,
//            yOffset,
//            boxWidth,
//            maxHeight,
//            0.0,
//            control.alignment.hpos,
//            control.alignment.vpos
//        )
    }
}

