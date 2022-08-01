package com.moralabs.pet.core.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moralabs.pet.core.domain.BaseDto
import com.moralabs.pet.core.domain.BaseResult
import com.moralabs.pet.core.domain.BaseUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class BaseViewModel<T: BaseDto>(private val useCase: BaseUseCase) : ViewModel(){
    protected var _state: MutableStateFlow<ViewState<T>> = MutableStateFlow(ViewState.Idle())
    val state: StateFlow<ViewState<T>> = _state

    var latestEntity : T? = null

    open fun fetch(){
        viewModelScope.launch {
            useCase.execute()
                .onStart {
                    _state.value = ViewState.Loading()
                }
                .catch { exception ->
                    _state.value = ViewState.Error(exception.message)
                    Log.e("CATCH", "exception : $exception")
                }
                .collect { baseResult ->
                    when(baseResult){
                        is BaseResult.Success -> {
                            latestEntity = baseResult.data as T
                            _state.value = ViewState.Success(baseResult.data as T)
                        }
                    }
                }
        }
    }

    fun idle(){
        _state = MutableStateFlow(ViewState.Idle())
    }
}

sealed class ViewState<T> {
    data class Success<T>(val data:T): ViewState<T>()
    data class Error<T>(val error: String?): ViewState<T>()
    class Idle<T> : ViewState<T>()
    class Loading<T> : ViewState<T>()
}