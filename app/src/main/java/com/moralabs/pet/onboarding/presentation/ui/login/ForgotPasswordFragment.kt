package com.moralabs.pet.onboarding.presentation.ui.login

import androidx.fragment.app.viewModels
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.BaseViewModel
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.databinding.FragmentForgotPasswordBinding
import com.moralabs.pet.onboarding.data.remote.dto.ForgotPasswordDto
import com.moralabs.pet.onboarding.data.remote.dto.LoginDto
import com.moralabs.pet.onboarding.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding, LoginDto, LoginViewModel>() {

    override fun getLayoutId() = R.layout.fragment_forgot_password
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun fragmentViewModel(): BaseViewModel<LoginDto> {
        val viewModel: LoginViewModel by viewModels()
        return viewModel
    }

    override fun addListeners() {
        super.addListeners()

        binding.nextButton.setOnClickListener {
            viewModel.forgotPassword(
                ForgotPasswordDto(
                    email = binding.emailEdittext.text.toString()
                )
            )
        }
    }
}