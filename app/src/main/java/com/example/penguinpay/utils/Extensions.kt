package com.example.penguinpay.utils

import android.view.Gravity
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.pow

@ExperimentalCoroutinesApi
fun <T> MutableStateFlow<T>.updateValue(updateFn: T.() -> T): T {
    val updatedValue = updateFn(this.value)
    this.value = updatedValue
    return updatedValue
}

val Fragment.viewLifecycleScope
    get() = viewLifecycleOwner.lifecycleScope

val Fragment.dialogToShow
    get() = LoadingDialog.getInstance(requireContext())

fun Fragment.showLoadingDialog() {
    try {
        if (requireActivity().isFinishing.not()) {
            dialogToShow.show()
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun cancelDialog() {
            if (dialogToShow.isShowing) {
                dialogToShow.dismiss()
            }
        }
    })
}

fun Fragment.hideLoadingDialog() {
    try {
        dialogToShow.dismiss()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let {
        val snack = Snackbar.make(it.findViewById(android.R.id.content), snackbarText, timeLength)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        view.layoutParams = params
        snack.show()
    }
}

fun convertBinaryToDecimal(binaryNumber: Long): Int {
    var binaryNumber = binaryNumber
    var decimalNo = 0
    var power = 0

    while (binaryNumber > 0) {
        val r = binaryNumber % 10
        decimalNo = (decimalNo + r * Math.pow(2.0, power.toDouble())).toInt()
        binaryNumber /= 10
        power++
    }
    return decimalNo
}

fun convertDecimalToBinary(num: Int): String {
    // converting decimal to binary
    return Integer.toBinaryString(num)
}