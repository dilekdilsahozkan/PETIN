package com.moralabs.pet.mainPage.data.repository

import com.moralabs.pet.core.data.remote.dto.BaseResponse
import com.moralabs.pet.core.data.remote.dto.CommentDto
import com.moralabs.pet.mainPage.data.remote.dto.CommentRequestDto
import retrofit2.Response

interface CommentRepository {
    suspend fun writeComment(postId: String?, writeNewComment: CommentRequestDto): Response<BaseResponse<List<CommentDto>>>
    suspend fun getComment(postId: String?): Response<BaseResponse<List<CommentDto>>>
    //  suspend fun likeComment(): Response<BaseResponse>
   //  suspend fun unlikeComment(): Response<BaseResponse>
}