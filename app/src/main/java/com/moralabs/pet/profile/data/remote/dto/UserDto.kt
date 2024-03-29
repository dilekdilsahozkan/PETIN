package com.moralabs.pet.profile.data.remote.dto

import android.os.Parcelable
import androidx.annotation.Keep
import com.moralabs.pet.core.data.remote.dto.BaseDto
import com.moralabs.pet.newPost.data.remote.dto.MediaDto
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class UserDto(
    val userId: String = "",
    val media: MediaDto? = null,
    val userName: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthDate: Long? = null,
    val followerCount: Int? = null,
    val followedCount: Int? = null,
    val postCount: Int? = null,
    val pageIndex: Int? = null,
    val isUserFollowed: Boolean? = null,
    val isUserBlockedByUser: Boolean? = null,
    val isBlocked: Boolean? = null
) : BaseDto(), Parcelable
