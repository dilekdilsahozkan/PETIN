package com.moralabs.pet.offer.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.moralabs.pet.BR
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.core.presentation.adapter.BaseListAdapter
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.databinding.FragmentOfferUserBinding
import com.moralabs.pet.databinding.ItemUserInfoBinding
import com.moralabs.pet.message.presentation.ui.MessageDetailActivity
import com.moralabs.pet.offer.data.remote.dto.OfferDetailDto
import com.moralabs.pet.offer.data.remote.dto.OfferDto
import com.moralabs.pet.offer.presentation.viewmodel.OfferViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferUserFragment :
    BaseFragment<FragmentOfferUserBinding, OfferDetailDto, OfferViewModel>() {

    private val postId: String? by lazy {
        activity?.intent?.getStringExtra(OfferUserActivity.POST_ID)
    }

    override fun getLayoutId() = R.layout.fragment_offer_user
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun fragmentViewModel(): BaseViewModel<OfferDetailDto> {
        val viewModelMake: OfferViewModel by viewModels()
        return viewModelMake
    }

    private val userAdapter: BaseListAdapter<OfferDto, ItemUserInfoBinding> by lazy {
        BaseListAdapter(R.layout.item_user_info, BR.item, onRowClick = {
            startActivity(Intent(context, OfferActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        OfferActivity.OFFER_ID to it.id,
                    )
                )
            })
        })
    }

    override fun setToolbar() {
        super.setToolbar()
        toolbarListener?.showTitleText(getString(R.string.allOffers))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = userAdapter
        viewModel.usersOffer(postId)
    }

    override fun stateSuccess(data: OfferDetailDto) {
        super.stateSuccess(data)

        userAdapter.submitList(data.allOffers)
    }
}