package ca.pragmaticcoding.blog.styleable

import javafx.css.*

class StandardStyleableObjectProperty<T>(
    attributeName: String,
    styleConverter: StyleConverter<*, T>,
    initialValue: T? = null
) : StyleableObjectProperty<T>() {

    private var cssMetaData: CssMetaData<out Styleable, T> =
        object : CssMetaData<Styleable, T>(attributeName, styleConverter) {
            override fun isSettable(p0: Styleable): Boolean = !this@StandardStyleableObjectProperty.isBound
            override fun getStyleableProperty(p0: Styleable): StyleableProperty<T> =
                this@StandardStyleableObjectProperty
        }

    init {
        initialValue?.let { super.setValue(it) }
    }

    override fun getBean(): Any? = null

    override fun getName(): String = "Styleable Property"

    override fun getCssMetaData(): CssMetaData<out Styleable, T> = cssMetaData
}