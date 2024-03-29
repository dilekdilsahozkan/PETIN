package com.moralabs.pet.onboarding.data.remote.dto

import androidx.annotation.Keep
import com.moralabs.pet.core.data.remote.dto.BaseDto

@Keep
data class RegisterDto (
    val userId: String = "",
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val accessTokenExpiration: String? = null,
    val refreshTokenExpiration: String? = null,
    val tokenType: String? = null
): BaseDto()