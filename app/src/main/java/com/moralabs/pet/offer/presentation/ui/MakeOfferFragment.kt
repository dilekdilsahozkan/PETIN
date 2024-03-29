package com.moralabs.pet.offer.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.moralabs.pet.BR
import com.moralabs.pet.R
import com.moralabs.pet.core.presentation.viewmodel.BaseViewModel
import com.moralabs.pet.core.presentation.adapter.BaseListAdapter
import com.moralabs.pet.core.presentation.ui.BaseFragment
import com.moralabs.pet.databinding.FragmentMakeOfferBinding
import com.moralabs.pet.databinding.ItemPetCardBinding
import com.moralabs.pet.mainPage.presentation.ui.MainPageActivity
import com.moralabs.pet.offer.data.remote.dto.OfferRequestDto
import com.moralabs.pet.offer.presentation.viewmodel.MakeOfferViewModel
import com.moralabs.pet.petProfile.data.remote.dto.CreateOfferDto
import com.moralabs.pet.petProfile.data.remote.dto.PetDto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeOfferFragment :
    BaseFragment<FragmentMakeOfferBinding, CreateOfferDto, MakeOfferViewModel>() {

    private val postId: String? by lazy {
        activity?.intent?.getStringExtra(MakeOfferActivity.POST_ID)
    }

    private val offerType: Int? by lazy {
        activity?.intent?.getIntExtra(MakeOfferActivity.OFFER_TYPE, 0)
    }

    val MAX_CHAR_NUMBER = 255

    override fun getLayoutId() = R.layout.fragment_make_offer
    override fun fetchStrategy() = UseCaseFetchStrategy.NO_FETCH

    override fun fragmentViewModel(): BaseViewModel<CreateOfferDto> {
        val viewModelMake: MakeOfferViewModel by viewModels()
        return viewModelMake
    }

    private val petAdapter: BaseListAdapter<PetDto, ItemPetCardBinding> by lazy {
        BaseListAdapter(R.layout.item_pet_card, BR.item, onRowClick = { selected ->
            petAdapter.currentList.forEach { pet ->
                pet.selected = pet == selected
            }
            petAdapter.notifyDataSetChanged()
        })
    }

    override fun setToolbar() {
        super.setToolbar()
        toolbarListener?.showTitleText(getString(R.string.offer))
    }

    override fun stateSuccess(data: CreateOfferDto) {
        super.stateSuccess(data)

        if (offerType == OfferType.FIND_PARTNER_TYPE.type && data.getOffer?.size == 0) {
            binding.addPetText.visibility = View.VISIBLE
        } else {
            binding.addPetText.visibility = View.GONE
        }

        petAdapter.submitList(data.getOffer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.petList.adapter = petAdapter

        if (offerType == OfferType.ADOPTION_TYPE.type) {
            binding.petList.visibility = View.GONE
            binding.selectText.visibility = View.GONE
        } else if (offerType == OfferType.FIND_PARTNER_TYPE.type) {
            binding.petList.visibility = View.VISIBLE
            binding.selectText.visibility = View.VISIBLE
        }
    }

    override fun addListeners() {
        super.addListeners()

        binding.explanationText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text: String = binding.explanationText.text.toString()
                if (text.startsWith(" ")) {
                    binding.explanationText.setText(text.trim { it <= ' ' })
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.explanationText.doAfterTextChanged {
            binding.inputViewCounter.text = "${it?.length}/${MAX_CHAR_NUMBER}"

            if (it?.length == MAX_CHAR_NUMBER) {
                binding.inputViewCounter.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.mainColor
                    )
                )
            } else {
                binding.inputViewCounter.setTextColor(R.color.darkGray)
            }
        }

        binding.makeOfferButton.setOnClickListener {
            if (binding.explanationText.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.add_text), Toast.LENGTH_LONG)
                    .show()
            } else {
                val pet = petAdapter.currentList.filter { it.selected }.firstOrNull()
                if (offerType == OfferType.ADOPTION_TYPE.type) {
                    viewModel.newOffer(
                        OfferRequestDto(
                            postId = postId,
                            text = binding.explanationText.text.toString()
                        )
                    )
                    Toast.makeText(context, R.string.offer_sent, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, MainPageActivity::class.java))
                } else if (offerType == OfferType.FIND_PARTNER_TYPE.type && pet?.selected == true) {
                    viewModel.newOffer(
                        OfferRequestDto(
                            postId = postId,
                            text = binding.explanationText.text.toString(),
                            petId = pet.id
                        )
                    )
                    Toast.makeText(context, R.string.offer_sent, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, MainPageActivity::class.java))
                } else {
                    Toast.makeText(requireContext(), R.string.have_to_add_pet, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        viewModel.petValue()
    }

    override fun addObservers() {
        super.addObservers()

        viewModel.petsList.observe(viewLifecycleOwner) { it ->
            petAdapter.submitList(it.sortedByDescending { it.selected })
        }
    }
}

internal enum class OfferType(val type: Int) {
    FIND_PARTNER_TYPE(2),
    ADOPTION_TYPE(3)
}