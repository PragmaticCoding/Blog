package ca.pragmaticcoding.blog.customproperty

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.binding.Binding
import javafx.beans.binding.ObjectBinding
import javafx.beans.binding.ObjectExpression
import javafx.beans.property.*
import javafx.beans.value.*
import javafx.collections.ObservableList
import javafx.util.Subscription

interface CustomerModel {
    fun accountProperty(): StringProperty
    fun getAccount(): String?
    fun setAccount(p0: String)

    fun fNameProperty(): StringProperty
    fun getFName(): String?
    fun setFName(p0: String)

    fun lNameProperty(): StringProperty
    fun getLName(): String?
    fun setLName(p0: String)

    fun getAge(): Int
    fun setAge(p0: Int)

    fun fullName(): ObservableStringValue
}

class CustomerImpl : CustomerModel {
    companion object {
        private var counter = 0
    }

    val serial = counter++
    private val fNameProp: StringProperty = SimpleStringProperty("")
    private val lNameProp: StringProperty = SimpleStringProperty("")
    private val accountProp: StringProperty = SimpleStringProperty("")
    private var age: Int = 0

    override fun accountProperty(): StringProperty = accountProp
    override fun getAccount(): String = accountProp.get()
    override fun setAccount(p0: String) {
        accountProp.set(p0)
    }

    override fun fNameProperty(): StringProperty = fNameProp
    override fun getFName(): String = fNameProp.get()
    override fun setFName(p0: String) {
        fNameProp.set(p0)
    }

    override fun lNameProperty(): StringProperty = lNameProp
    override fun getLName(): String = lNameProp.get()
    override fun setLName(p0: String) {
        lNameProp.set(p0)
    }

    override fun getAge() = age
    override fun setAge(p0: Int) {
        age = p0
    }


    override fun fullName(): ObservableStringValue = lNameProp.concat(", ").concat(fNameProp)

    override fun toString() = "CustomerImpl: $serial"
}


interface ObservableCustomerValue : ObservableObjectValue<CustomerModel>, CustomerModel

abstract class CustomerExpression : ObjectExpression<CustomerModel>(), ObservableCustomerValue {
    protected val delegate = CustomerImpl()

    override fun accountProperty() = delegate.accountProperty()
    override fun getAccount(): String? = delegate.getAccount()
    override fun setAccount(p0: String) {
        delegate.setAccount(p0)
    }

    override fun fNameProperty(): StringProperty = delegate.fNameProperty()
    override fun getFName(): String? = delegate.getFName()
    override fun setFName(p0: String) {
        delegate.setFName(p0)
    }

    override fun lNameProperty(): StringProperty = delegate.lNameProperty()
    override fun getLName(): String? = delegate.getLName()
    override fun setLName(p0: String) {
        delegate.setLName(p0)
    }

    override fun getAge(): Int = delegate.getAge()
    override fun setAge(p0: Int) {
        delegate.setAge(p0)
    }

    override fun fullName(): ObservableStringValue = delegate.fullName()
}

abstract class CustomerBinding : CustomerExpression(), Binding<CustomerModel> {

    private lateinit var delegationHelper: DelegationHelper
    private val bindingDelegate = object : ObjectBinding<CustomerModel>() {
        override fun computeValue(): CustomerModel {
            this@CustomerBinding.delegationHelper.unsubscribeObject()
            val result = this@CustomerBinding.computeValue()
            println("The result $result")
            delegationHelper.newCustomerModel(result)
            return result
        }

        fun delegateBind(vararg var1: Observable) {
            this.bind(*var1)
        }

        fun delegateUnbind(vararg var1: Observable) {
            this.unbind(*var1)
        }

        fun delegateGet(): CustomerModel? {
            println("In get")
            return get()
        }

        override fun onInvalidating() {
            this@CustomerBinding.invalidate()
            super.onInvalidating()
        }

        fun delegateIsObserved() = this.isObserved
    }

    init {
        println("In the Binding Parent Init")
        delegationHelper = DelegationHelper(delegate, bindingDelegate, true)
    }

    override fun addListener(var1: InvalidationListener?) {
        bindingDelegate.addListener(var1)
    }

    override fun removeListener(var1: InvalidationListener?) {
        bindingDelegate.removeListener(var1)
    }

    override fun addListener(var1: ChangeListener<in CustomerModel?>?) {
        bindingDelegate.addListener(var1)
    }

    override fun removeListener(var1: ChangeListener<in CustomerModel?>?) {
        removeListener(var1)
    }

    protected fun bind(vararg var1: Observable) {
        bindingDelegate.delegateBind(*var1)
    }

    protected fun unbind(vararg var1: Observable) {
        bindingDelegate.delegateUnbind(*var1)
    }

    override fun dispose() {
        bindingDelegate.dispose()
    }

    override fun getDependencies(): ObservableList<*> {
        return bindingDelegate.dependencies
    }

    override fun get(): CustomerModel? = bindingDelegate.delegateGet()

    protected open fun onInvalidating() {
    }

    override fun invalidate() {
        bindingDelegate.invalidate()
    }

    override fun isValid(): Boolean {
        return bindingDelegate.isValid
    }

    protected fun isObserved(): Boolean {
        return bindingDelegate.delegateIsObserved()
    }

    protected open fun allowValidation(): Boolean {
        return true
    }

    protected abstract fun computeValue(): CustomerModel

    override fun toString(): String {
        return if (this.isValid) "ObjectBinding [value: " + get().toString() + "]" else "ObjectBinding [invalid]"
    }
}

abstract class ReadOnlyCustomerProperty : CustomerExpression(), ReadOnlyProperty<CustomerModel>

interface WritableCustomerValue : WritableObjectValue<CustomerModel>, CustomerModel

abstract class CustomerProperty : ReadOnlyCustomerProperty(), Property<CustomerModel>, WritableCustomerValue

class DelegationHelper(
    private val delegate: CustomerModel,
    private val objectProperty: ObservableValue<CustomerModel>,
    private val isBinding: Boolean = false
) {
    private var fNameSubscription: Subscription? = null
    private var lNameSubscription: Subscription? = null
    private var accountSubscription: Subscription? = null
    private var inUpdate: Boolean = false

    fun unsubscribeObject() {
        accountSubscription?.unsubscribe()
        fNameSubscription?.unsubscribe()
        lNameSubscription?.unsubscribe()
        delegate.setAge(0)
    }

    fun newCustomerModel(newCustomerModel: CustomerModel?) {
        if (newCustomerModel != null) {
            accountSubscription = newCustomerModel.accountProperty()
                .subscribe { newVal -> updateProperty(delegate.accountProperty(), newVal) }
            fNameSubscription = newCustomerModel.fNameProperty()
                .subscribe { newVal -> updateProperty(delegate.fNameProperty(), newVal) }
            lNameSubscription = newCustomerModel.lNameProperty()
                .subscribe { newVal -> updateProperty(delegate.lNameProperty(), newVal) }
            delegate.setAge(newCustomerModel.getAge())
        }
    }

    init {
        if (!isBinding) {
            delegate.accountProperty()
                .subscribe { newVal -> objectProperty.value?.let { updateProperty(it.accountProperty(), newVal) } }
            delegate.fNameProperty()
                .subscribe { newVal -> objectProperty.value?.let { updateProperty(it.fNameProperty(), newVal) } }
            delegate.lNameProperty()
                .subscribe { newVal -> objectProperty.value?.let { updateProperty(it.lNameProperty(), newVal) } }
        }
    }

    private fun <T : Any> updateProperty(property: Property<T>, newVal: T) {
        if (!inUpdate) {
            inUpdate = true
            if (!property.isBound) property.value = newVal
            inUpdate = false
        }
    }
}


abstract class CustomerPropertyBase : CustomerProperty() {
    private var objectProperty = SimpleObjectProperty<CustomerModel>()
    private var delegationHelper = DelegationHelper(delegate, objectProperty)

    var fred: ObjectPropertyBase<CustomerModel>? = null


    override fun addListener(p0: ChangeListener<in CustomerModel>?) {
        objectProperty.addListener(p0)
    }

    override fun addListener(p0: InvalidationListener?) {
        objectProperty.addListener(p0)
    }

    override fun removeListener(p0: ChangeListener<in CustomerModel>?) {
        objectProperty.removeListener(p0)
    }

    override fun removeListener(p0: InvalidationListener?) {
        objectProperty.removeListener(p0)
    }

    override fun getValue() = get()

    override fun get(): CustomerModel = objectProperty.get()

    override fun setValue(p0: CustomerModel?) {
        this.set(p0)
    }

    override fun bind(p0: ObservableValue<out CustomerModel>?) {
        objectProperty.bind(p0)
    }

    override fun unbind() {
        objectProperty.unbind()
    }

    override fun isBound(): Boolean = objectProperty.isBound

    override fun bindBidirectional(p0: Property<CustomerModel>?) {
        objectProperty.bindBidirectional(p0)
    }

    override fun unbindBidirectional(p0: Property<CustomerModel>?) {
        objectProperty.unbindBidirectional(p0)
    }

    override fun set(p0: CustomerModel?) {
        delegationHelper.unsubscribeObject()
        delegationHelper.newCustomerModel(p0)
        objectProperty.set(p0)

    }
}

class SimpleCustomerProperty(initialValue: CustomerModel? = null) : CustomerPropertyBase() {

    init {
        set(initialValue)
    }

    override fun getBean() = "Bean!"
    override fun getName() = "A Customer"
}



