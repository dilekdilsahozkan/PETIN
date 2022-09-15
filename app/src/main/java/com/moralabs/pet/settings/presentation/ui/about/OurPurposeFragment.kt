package com.moralabs.pet.settings.presentation.ui.about

import androidx.fragment.app.viewModels
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.BaseViewModel
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.databinding.FragmentOurPurposeBinding
import com.moralabs.pet.settings.data.remote.dto.SettingsDto
import com.moralabs.pet.settings.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OurPurposeFragment : BaseFragment<FragmentOurPurposeBinding, SettingsDto, SettingsViewModel>() {

    override fun getLayoutId() = R.layout.fragment_our_purpose
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun setToolbar() {
        super.setToolbar()
        toolbarListener?.showTitleText(getString(R.string.settings_our_purpose))
    }

    override fun fragmentViewModel(): BaseViewModel<SettingsDto> {
        val viewModel: SettingsViewModel by viewModels()
        return viewModel
    }
}