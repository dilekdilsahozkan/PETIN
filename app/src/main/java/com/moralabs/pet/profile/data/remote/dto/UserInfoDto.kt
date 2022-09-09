package com.moralabs.pet.profile.data.remote.dto

import com.moralabs.pet.core.data.remote.dto.BaseDto
import com.moralabs.pet.newPost.data.remote.dto.MediaDto

data class UserInfoDto(
    val userId: String? = "",
    val fullName: String? = null,
    val userName: String? = null,
    val media: MediaDto? = null
) : BaseDto()
