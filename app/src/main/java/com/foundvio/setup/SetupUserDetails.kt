package com.foundvio.setup

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.foundvio.setup.validation.ValidationField

/**
 * Extra details provided by user
 *
 * This class support two-way binding between the UI and data.
 * This means that if the user were to update the text in the UI, the data here gets updated as well
 *
 * @see fragment_register_details.xml for UI binding
 *
 */
class SetupUserDetails : BaseObservable() {

    @Transient val phoneValidation = ValidationField<String>().apply {
        rule("Phone cannot be blank") { it.isNotBlank() }
    }

    @get:Bindable
    var phone: String = ""
        set(value) {
            field = value
            phoneValidation.validate(field)
            notifyPropertyChanged(BR.phone)
        }

    @Transient val familyNameValidation = ValidationField<String>().apply {
        rule("Family Name cannot be blank") { it.isNotBlank() }
    }

    @get:Bindable
    var familyName: String = ""
        set(value) {
            field = value
            familyNameValidation.validate(field)
            notifyPropertyChanged(BR.familyName)
        }

    @Transient val givenNameValidation = ValidationField<String>().apply {
        rule("Given Name cannot be blank") { it.isNotBlank() }
    }
    @get:Bindable
    var givenName: String = ""
        set(value) {
            field = value
            givenNameValidation.validate(field)
            notifyPropertyChanged(BR.givenName)
        }

    var isTrackee = false

    /**
     * Check if all the fields are valid
     */
    fun validate(): Boolean {
        return phoneValidation.validate(phone) and
                familyNameValidation.validate(familyName) and
                givenNameValidation.validate(givenName)
    }

}