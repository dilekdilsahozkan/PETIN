package com.moralabs.pet.notification.presentation.ui

import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.toolbar.PetToolbarListener
import com.moralabs.pet.core.presentation.ui.BaseActivity
import com.moralabs.pet.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationBinding>(),
    PetToolbarListener {

    companion object {
        const val CONTENT_ID = "postId"
        const val OTHER_USER_ID = "otherUserId"
    }

    override fun getLayoutId() = R.layout.activity_notification
}