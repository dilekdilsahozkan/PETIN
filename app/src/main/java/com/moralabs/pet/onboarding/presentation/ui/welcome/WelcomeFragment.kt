package com.moralabs.pet.onboarding.presentation.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.databinding.FragmentWelcomeBinding
import com.moralabs.pet.mainPage.presentation.ui.MainPageActivity
import com.moralabs.pet.onboarding.data.remote.dto.LoginDto
import com.moralabs.pet.onboarding.presentation.ui.login.LoginActivity
import com.moralabs.pet.onboarding.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : View.OnClickListener,
    BaseFragment<FragmentWelcomeBinding, LoginDto, LoginViewModel>() {

    private var timerJob: Job? = null

    override fun getLayoutId() = R.layout.fragment_welcome
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun fragmentViewModel(): BaseViewModel<LoginDto> {
        val viewModel: LoginViewModel by viewModels()
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int) = when (position) {
                0 -> TutorialFragment().apply {
                    arguments =
                        bundleOf(TutorialFragment.TEXT to R.string.tutorial_1, TutorialFragment.IMAGE to R.drawable.ic_tutorial_1)
                }
                1 -> TutorialFragment().apply {
                    arguments =
                        bundleOf(TutorialFragment.TEXT to R.string.tutorial_2, TutorialFragment.IMAGE to R.drawable.ic_tutorial_2)
                }
                2 -> TutorialFragment().apply {
                    arguments =
                        bundleOf(TutorialFragment.TEXT to R.string.tutorial_3, TutorialFragment.IMAGE to R.drawable.ic_tutorial_3)
                }
                else -> throw NotImplementedError()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                timerJob?.cancel()

                timerJob = lifecycleScope.launch {
                    delay(6000)
                    binding.viewPager.currentItem = (binding.viewPager.currentItem + 1) % 3
                }
            }
        })

        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    override fun addListeners() {
        super.addListeners()

        binding.loginButton.setOnClickListener(this)
        binding.guestButton.setOnClickListener(this)
    }

    override fun stateSuccess(data: LoginDto) {
        super.stateSuccess(data)
        startActivity(Intent(context, MainPageActivity::class.java))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_button -> {
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
            R.id.guest_button -> {
                viewModel.guestLogin()
            }
        }
    }
}