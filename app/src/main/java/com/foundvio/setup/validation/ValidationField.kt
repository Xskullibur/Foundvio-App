package com.foundvio.setup.validation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Provides validation check on field
 *
 * Example:
 *
 *  Creating a [ValidationField] for validating email
 *  ```
 *  val checker = ValidationField<String>().apply {
 *      //List of predicates or rules for validating the field
 *      rule("Email cannot be blank") { it.isNotBlank() }
 *      rule("Email is invalid") { it.contains("@") }
 *  }
 *  ```
 *
 *  For checking if email is valid:
 *  ```
 *
 *  val email = "address@domain.com"
 *  checker.validate(email) <- returns true
 *
 *  checker.observe(<lifecycle>) { validationMessage ->
 *      println(validationMessage)
 *  }
 *
 *  ```
 *
 */
class ValidationField<T>{

    var validateCheckers: MutableList<ValidationChecker<T>> = mutableListOf()
    var validateCheckersMessage: MutableList<String> = mutableListOf()

    private var _validationMessage = MutableLiveData<String?>(null)
    var validationMessage: LiveData<String?> = _validationMessage

    /**
     * Check if the given [field] is valid based on the rules or predicates
     * @return if the [field] is valid
     */
    fun validate(field: T): Boolean {
        return validateCheckers.withIndex().any { (index, checker) ->
            val result = checker.validate(field)
            if(!result){ _validationMessage.postValue(validateCheckersMessage[index])}
            result
        }.also { valid ->
            if(valid) _validationMessage.postValue(null)
        }
    }

    /**
     * Add a new rule or predicate for validating field when calling [validate]
     * @param errorMessage error message if the rule or predicate were to fail
     * @param validationField rule or predicate for validating field
     */
    fun rule(errorMessage: String, validationField: ValidationChecker<T>) {
        validateCheckers += validationField
        validateCheckersMessage += errorMessage
    }

    /**
     * Observe [validationMessage] for any new error message
     */
    fun observe(owner: LifecycleOwner, observable: Observer<String?>){
        validationMessage.observe(owner, observable)
    }

}

fun interface ValidationChecker<T> {
    fun validate(value: T): Boolean
}