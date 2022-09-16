package com.moralabs.pet.profile.data.repository

import com.moralabs.pet.core.data.remote.dto.BaseResponse
import com.moralabs.pet.profile.data.remote.dto.UserDto
import com.moralabs.pet.profile.data.remote.dto.UserInfoDto
import retrofit2.Response

interface ProfileRepository {
    suspend fun userInfo(): Response<BaseResponse<UserDto>>
    suspend fun otherUsersInfo(userId: String?): Response<BaseResponse<UserDto>>
    suspend fun getFollowedList(): Response<BaseResponse<List<UserInfoDto>>>
    suspend fun getFollowerList(): Response<BaseResponse<List<UserInfoDto>>>
    suspend fun followUser(userId: String?): Response<BaseResponse<*>>
    suspend fun unfollowUser(userId: String?): Response<BaseResponse<*>>
    suspend fun getBlockedList(): Response<BaseResponse<List<UserInfoDto>>>
    suspend fun blockUser(userId: String?): Response<BaseResponse<*>>
    suspend fun unblockUser(userId: String?): Response<BaseResponse<*>>
}