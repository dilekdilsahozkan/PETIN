package com.moralabs.pet.petProfile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.moralabs.pet.core.domain.BaseResult
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.core.presentation.viewmodel.ViewState
import com.moralabs.pet.petProfile.data.remote.dto.PetDto
import com.moralabs.pet.petProfile.domain.PetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val useCase: PetUseCase
): BaseViewModel<List<PetDto>>(useCase){

    fun getPet(){
        viewModelScope.launch {
            useCase.petPost()
                .onStart {
                    _state.value = ViewState.Loading()
                }
                .catch { exception ->
                    _state.value = ViewState.Error(message = exception.message)
                    Log.e("CATCH", "exception : $exception")
                }
                .collect { baseResult ->
                    when (baseResult) {
                        is BaseResult.Success -> {
                            _state.value = ViewState.Idle()
                            _state.value = ViewState.Success(baseResult.data)
                        }
                        is BaseResult.Error -> {
                            _state.value = ViewState.Error(baseResult.error.code, baseResult.error.message)
                        }
                    }
                }
        }
    }

    fun getAnotherUserPet(userId: String?){
        viewModelScope.launch {
            useCase.getAnotherUserPet(userId)
                .onStart {
                    _state.value = ViewState.Loading()
                }
                .catch { exception ->
                    _state.value = ViewState.Error(message = exception.message)
                    Log.e("CATCH", "exception : $exception")
                }
                .collect { baseResult ->
                    when (baseResult) {
                        is BaseResult.Success -> {
                            _state.value = ViewState.Idle()
                            _state.value = ViewState.Success(baseResult.data)
                        }
                        is BaseResult.Error -> {
                            _state.value = ViewState.Error(baseResult.error.code, baseResult.error.message)
                        }
                    }
                }
        }
    }
}