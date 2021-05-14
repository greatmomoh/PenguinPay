package com.example.penguinpay.utils

sealed class LoadingState {
    object Idle :LoadingState()
    object Working :LoadingState()
    object Error : LoadingState()
    object Success : LoadingState()
}
