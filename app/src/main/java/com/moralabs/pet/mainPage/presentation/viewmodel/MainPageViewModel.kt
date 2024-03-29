package com.moralabs.pet.mainPage.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moralabs.pet.core.data.remote.dto.PostDto
import com.moralabs.pet.core.domain.BaseResult
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.core.presentation.viewmodel.ViewState
import com.moralabs.pet.mainPage.domain.MainPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val useCase: MainPageUseCase
) : BaseViewModel<List<PostDto>>(useCase) {

    private var job: Job? = null
    var postType = MutableLiveData<Int>(-1)

    private var _deleteState: MutableStateFlow<ViewState<Boolean>> = MutableStateFlow(ViewState.Idle())
    val deleteState: StateFlow<ViewState<Boolean>> = _deleteState

    private var _reportState: MutableStateFlow<ViewState<Boolean>> = MutableStateFlow(ViewState.Idle())
    val reportState: StateFlow<ViewState<Boolean>> = _reportState
    private val posts = mutableListOf<PostDto>()

    private var lastDateTime: Long? = null
    private var appendingState = false

    fun feedPost(forceReload: Boolean = true, searchQuery: String? = null, postType: Int? = null) {

        if (appendingState && !forceReload && searchQuery == null) {
            return
        }

        if (!forceReload && searchQuery == null && lastDateTime != null) {
            appendingState = true
        }

        if (forceReload) {
            posts.clear()
            lastDateTime = null
        }

        job?.cancel()

        job = viewModelScope.launch {
            useCase.getFeed(searchQuery, if(postType == 4) null else postType, lastDateTime)
                .onStart {
                    if (searchQuery == null) _state.value = ViewState.Loading()
                    if (postType == null) _state.value = ViewState.Loading()
                }
                .catch { exception ->
                    appendingState = false
                    _state.value = ViewState.Error(message = exception.message)
                    Log.e("CATCH", "exception : $exception")
                }
                .collect { baseResult ->
                    appendingState = false
                    when (baseResult) {
                        is BaseResult.Success -> {
                            if (baseResult.data.isNotEmpty()) {
                                lastDateTime = baseResult.data.minOf { it.dateTime ?: -1 }
                            }

                            posts.addAll(baseResult.data)

                            _state.value = ViewState.Success(posts)
                        }
                        is BaseResult.Error -> {
                            _state.value = ViewState.Error(baseResult.error.code, baseResult.error.message)
                        }
                    }
                }
        }
    }

    fun likePost(postId: String?) {
        GlobalScope.launch { useCase.likePost(postId).collect { } }
    }

    fun unlikePost(postId: String?) {
        GlobalScope.launch { useCase.unlikePost(postId).collect { } }
    }

    fun reportPost(postId: String?, reportType: Int?) {
        viewModelScope.launch {
            useCase.reportPost(postId, reportType)
                .onStart {
                    _reportState.value = ViewState.Loading()
                }
                .catch { exception ->
                    _reportState.value = ViewState.Error(message = exception.message)
                    Log.e("CATCH", "exception : $exception")
                }
                .collect { baseResult ->
                    when (baseResult) {
                        is BaseResult.Success -> {
                            _state.value = ViewState.Idle()
                            _reportState.value = ViewState.Success(baseResult.data)
                        }
                        is BaseResult.Error -> {
                            _reportState.value = ViewState.Error(baseResult.error.code, baseResult.error.message)
                        }
                    }
                }
        }
    }

    fun deletePost(postId: String?) {
        viewModelScope.launch {
            useCase.deletePost(postId)
                .onStart {
                    _deleteState.value = ViewState.Loading()
                }
                .catch { exception ->
                    _deleteState.value = ViewState.Error(message = exception.message)
                    Log.e("CATCH", "exception : $exception")
                }
                .collect { baseResult ->
                    when (baseResult) {
                        is BaseResult.Success -> {
                            _deleteState.value = ViewState.Success(baseResult.data)
                        }
                        is BaseResult.Error -> {
                            _deleteState.value = ViewState.Error(baseResult.error.code, baseResult.error.message)
                        }
                    }
                }
        }
    }
}