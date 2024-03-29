package com.moralabs.pet.profile.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moralabs.pet.R
import com.moralabs.pet.databinding.FragmentOtherUserActionBottomSheetBinding
import com.moralabs.pet.profile.presentation.viewmodel.ProfileViewModel

class OtherUserActionBottomSheetFragment(
    val isUserFollowed: Boolean?,
    val isUserBlocked: Boolean?,
    val userId: String?,
    val listenerFollowUnfollow: FollowUnfollowBottomSheetListener,
    val listenerBlockUnblock: BlockUnblockBottomSheetListener,
    val listenerReportUser: ReportUserBottomSheetListener
) : BottomSheetDialogFragment(),
    View.OnClickListener {

    lateinit var binding: FragmentOtherUserActionBottomSheetBinding
    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_other_user_action_bottom_sheet, null, false
        )

        InitBottomSheetUI()
        binding.followUnfollowUserText.setOnClickListener(this)
        binding.blockUnblockUserText.setOnClickListener(this)
        binding.reportUserText.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.follow_unfollow_user_text -> {
                listenerFollowUnfollow.onFollowUnfollowItemClick(isUserFollowed)
                dismiss()
            }
            R.id.block_unblock_user_text -> {
                listenerBlockUnblock.onBlockUnblockItemClick(isUserBlocked)
                dismiss()
            }
            R.id.report_user_text -> {
                listenerReportUser.onReportItemClick(userId, 0)
                dismiss()
            }
        }
    }

    private fun InitBottomSheetUI() {
        binding.reportUserText.visibility = View.VISIBLE
        isUserFollowed?.let {
            if (isUserBlocked != true) {
                binding.followUnfollowUserText.visibility = View.VISIBLE
                if (it) {
                    binding.followUnfollowUserText.text = getString(R.string.unfollow_user)
                } else {
                    binding.followUnfollowUserText.text = getString(R.string.follow_user)
                }
            }
        }
        isUserBlocked?.let {
            binding.blockUnblockUserText.visibility = View.VISIBLE
            if (it) {
                binding.blockUnblockUserText.text = getString(R.string.unblock_user)
            } else {
                binding.blockUnblockUserText.text = getString(R.string.block_user)
            }
        }
    }
}

interface FollowUnfollowBottomSheetListener {
    fun onFollowUnfollowItemClick(isUserFollowed: Boolean?)
}

interface BlockUnblockBottomSheetListener {
    fun onBlockUnblockItemClick(isUserBlocked: Boolean?)
}

interface ReportUserBottomSheetListener {
    fun onReportItemClick(userId: String?, isUserReported: Int?)
}