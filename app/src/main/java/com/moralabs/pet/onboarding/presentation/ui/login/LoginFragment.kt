package com.moralabs.pet.onboarding.presentation.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.databinding.FragmentLoginBinding
import com.moralabs.pet.mainPage.presentation.ui.MainPageActivity
import com.moralabs.pet.onboarding.data.remote.dto.LoginDto
import com.moralabs.pet.onboarding.data.remote.dto.LoginRequestDto
import com.moralabs.pet.onboarding.presentation.ui.register.RegisterActivity
import com.moralabs.pet.onboarding.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginDto, LoginViewModel>() {

    private val isFromAction by lazy {
        activity?.intent?.getBooleanExtra(LoginActivity.BUNDLE_ACTION, false) ?: false
    }

    override fun getLayoutId() = R.layout.fragment_login
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun fragmentViewModel(): BaseViewModel<LoginDto> {
        val viewModel: LoginViewModel by viewModels()
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paddingDp = 15
        val density = context?.resources?.displayMetrics?.density
        var paddingPixel = 0f
        density?.let {
            paddingPixel = it * paddingDp
        }
        binding.passwordEdittext.setPadding(paddingPixel.toInt(), 0, 0, 0)
    }

    override fun addListeners() {
        super.addListeners()
        binding.loginButton.setOnClickListener {
            viewModel.login(
                LoginRequestDto(
                    email = binding.emailEdittext.text.toString(),
                    password = binding.passwordEdittext.text.toString()
                )
            )
        }
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_login_to_forgotPasswordFragment)
        }
        setRegisterClickable()
    }

    override fun stateSuccess(data: LoginDto) {
        super.stateSuccess(data)

        if (isFromAction) {
            activity?.setResult(
                Activity.RESULT_OK,
                Intent().run { putExtras(bundleOf(LoginActivity.RESULT_LOGIN to LoginResults.LOGIN_OK)) })
            activity?.finish()
        } else {
            startActivity(Intent(context, MainPageActivity::class.java))
            activity?.finish()
        }
    }

    override fun stateError(data: String?) {
        super.stateError(data)
        binding.emailEdittext.setBackgroundResource(R.drawable.stroke_orange)
        binding.passwordEdittext.setBackgroundResource(R.drawable.stroke_orange)
        binding.errorLinear.visibility = View.VISIBLE
    }

    private fun setRegisterClickable() {
        val spannableString = SpannableString(getString(R.string.dontHaveAccount))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(context, RegisterActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#FF724C")
            }
        }
        spannableString.setSpan(
            clickableSpan,
            spannableString.length - 10,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.haveAccountText.text = spannableString
        binding.haveAccountText.movementMethod = LinkMovementMethod.getInstance()
    }
}
