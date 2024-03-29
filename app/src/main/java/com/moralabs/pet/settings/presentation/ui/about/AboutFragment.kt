package com.moralabs.pet.settings.presentation.ui.about

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.databinding.FragmentAboutBinding
import com.moralabs.pet.profile.data.remote.dto.UserDto
import com.moralabs.pet.settings.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding, UserDto, SettingsViewModel>() {

    override fun getLayoutId() = R.layout.fragment_about
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun fragmentViewModel(): BaseViewModel<UserDto> {
        val viewModel: SettingsViewModel by viewModels()
        return viewModel
    }

    override fun setToolbar() {
        super.setToolbar()
        toolbarListener?.showTitleText(getString(R.string.settings_about))
    }

    override fun addListeners() {
        super.addListeners()

        binding.whatIsPet.setOnClickListener {
            findNavController().navigate(R.id.action_aboutFragment_to_whatIsPetFragment)
        }
        binding.whoWeAre.setOnClickListener {
            findNavController().navigate(R.id.action_aboutFragment_to_whoWeAreFragment)

        }
        binding.ourPurpose.setOnClickListener {
            findNavController().navigate(R.id.action_aboutFragment_to_ourPurposeFragment)
        }
    }
}

enum class InfoTypes(val type: Int) {
    AGREEMENT(1),
    WHAT_IS_PET(2),
    WHO_ARE_WE(3),
    OUR_PURPOSE(4)
}