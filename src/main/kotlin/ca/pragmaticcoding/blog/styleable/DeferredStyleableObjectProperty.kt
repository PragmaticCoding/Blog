package ca.pragmaticcoding.blog.styleable

import javafx.css.CssMetaData
import javafx.css.Styleable
import javafx.css.StyleableObjectProperty

class DeferredStyleableObjectProperty<T>(
    private val bean: Any?,
    private val name: String,
    initialValue: T? = null
) : StyleableObjectProperty<T>() {

    private var cssMetaData: CssMetaData<out Styleable?, T>? = null

    constructor() : this(null, "", null)
    constructor(initialValue: T) : this(null, "", initialValue)

    init {
        initialValue?.let { super.setValue(it) }
    }

    override fun getBean(): Any? {
        return this.bean
    }

    override fun getName(): String {
        return this.name
    }

    override fun getCssMetaData(): CssMetaData<out Styleable?, T>? = cssMetaData

    fun setCssMetaData(newVal: CssMetaData<out Styleable?, T>) {
        if (cssMetaData == null) cssMetaData = newVal
    }
}