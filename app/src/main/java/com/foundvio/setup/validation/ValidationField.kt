package com.foundvio.setup.validation

import androidx.databinding.BaseObservable
import androidx.lifecycle.*

class ValidationField<T> : BaseObservable(){

    var validateCheckers: MutableList<ValidationChecker<T>> = mutableListOf()
    var validateCheckersMessage: MutableList<String> = mutableListOf()

    private var _validationMessage = MutableLiveData<String?>(null)
    var validationMessage: LiveData<String?> = _validationMessage

    fun validate(field: T): Boolean {
        return validateCheckers.withIndex().any { (index, checker) ->
            val result = checker.validate(field)
            if(!result){ _validationMessage.postValue(validateCheckersMessage[index])}
            result
        }.also { valid ->
            if(valid) _validationMessage.postValue(null)
        }
    }

    fun rule(errorMessage: String, validationField: ValidationChecker<T>) {
        validateCheckers += validationField
        validateCheckersMessage += errorMessage
    }

    fun observe(owner: LifecycleOwner, observable: Observer<String?>){
        validationMessage.observe(owner, observable)
    }

}

fun interface ValidationChecker<T> {
    fun validate(value: T): Boolean
}