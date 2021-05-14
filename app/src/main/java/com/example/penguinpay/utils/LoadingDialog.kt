package com.example.penguinpay.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.example.penguinpay.R

class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.dialog_loading)
    }

    override fun dismiss() {
        super.dismiss()
        instance = null
    }

    companion object {
        var instance: LoadingDialog? = null

        fun getInstance(context: Context): LoadingDialog {
            if (instance == null) {
                instance = LoadingDialog(context)
            }
            return instance!!
        }
    }
}