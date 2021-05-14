package com.example.penguinpay.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

class PhoneNumberMask(var etPhoneNumber: EditText) {

    fun listen() {
        etPhoneNumber.addTextChangedListener(phoneEntryWatcher)
    }

    fun stopListening() {
        etPhoneNumber.removeTextChangedListener(phoneEntryWatcher)
    }

    private val phoneEntryWatcher = object : TextWatcher {
        var lastChar = " "

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            val digits = etPhoneNumber.text.toString().length
            if (digits > 1)
                lastChar = etPhoneNumber.text.toString().substring(digits - 1)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val digits = etPhoneNumber.text.toString().length
            Log.d("LENGTH", "" + digits)
            if (etPhoneNumber.text.toString().startsWith("0")) {
                if (lastChar != "-") {
                    if (digits == 4 || digits == 8) {
                        etPhoneNumber.append("-")
                    }
                }
            } else {
                if (lastChar != "-") {
                    if (digits == 3 || digits == 7) {
                        etPhoneNumber.append("-")
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable) {
        }
    }
}
