package com.example.penguinpay.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.penguinpay.repositories.CurrencyRepository
import com.example.penguinpay.utils.LoadingState
import com.example.penguinpay.utils.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    var currencyCode : String = "KES"

    private val _viewState = MutableStateFlow(ViewState.init())
    val viewState: StateFlow<ViewState> = _viewState

    private val _actionState: MutableStateFlow<ActionState> = MutableStateFlow(ActionState.None)
    val actionState: StateFlow<ActionState> = _actionState


    fun fetchExchangeRate() {
        currencyRepository.getExchangeRate(currencyCode).onStart {
            _viewState.updateValue {
                copy(loadState = LoadingState.Working)
            }
        }.onEach {
            _viewState.updateValue {
                copy(loadState = LoadingState.Success, exchangeRate = it)
            }
        }.catch {
            it.printStackTrace()
            _viewState.updateValue {
                copy(loadState = LoadingState.Error, error = it)
            }
        }.launchIn(viewModelScope)
    }

    companion object {

        data class ViewState(
            val loadState: LoadingState,
            val exchangeRate: Double,
            val error: Throwable?
        ) {


            companion object {
                fun init(): ViewState {
                    return ViewState(LoadingState.Idle, 0.0, null)
                }
            }
        }

        sealed class ActionState {
            object None : ActionState()
        }
    }
}