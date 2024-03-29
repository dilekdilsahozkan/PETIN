package com.moralabs.pet.profile.presentation.ui

import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.toolbar.PetToolbarListener
import com.moralabs.pet.core.presentation.ui.BaseActivity
import com.moralabs.pet.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(),
    PetToolbarListener {

    companion object {
        const val OTHER_USER_ID = "otherUserId"
        const val BUNDLE_VISIBILITY = "otherUserId"

    }

    override fun getLayoutId() = R.layout.activity_profile
}