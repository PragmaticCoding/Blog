package ca.pragmaticcoding.blog.actionproperty

import javafx.beans.property.BooleanPropertyBase

class EncapsulatingProperty(initialValue: Boolean) : BooleanPropertyBase(initialValue) {
    override fun getBean(): Any = Object()
    override fun getName() = "Encapsulating Property"
    var counter = 0

    override fun invalidated() {
        println("Running invalidated()")
        counter++
    }
}

fun main() {
    val testProperty = EncapsulatingProperty(true)
    println("Counter: ${testProperty.counter} value: ${testProperty.value}")
    testProperty.value = true
    println("Counter: ${testProperty.counter} value: ${testProperty.value}")
    testProperty.value = false
    println("Counter: ${testProperty.counter} value: ${testProperty.value}")
    testProperty.value = true
    println("Counter: ${testProperty.counter} value: ${testProperty.value}")
    testProperty.value = false
    println("Counter: ${testProperty.counter} value: ${testProperty.value}")
}