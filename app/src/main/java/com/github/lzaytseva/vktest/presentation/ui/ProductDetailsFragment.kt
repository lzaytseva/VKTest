package com.github.lzaytseva.vktest.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.lzaytseva.vktest.R
import com.github.lzaytseva.vktest.app.App
import com.github.lzaytseva.vktest.databinding.FragmentProductDetailsBinding
import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.presentation.state.ProductDetailsScreenState
import com.github.lzaytseva.vktest.presentation.viewmodel.ProductDetailsViewModel
import com.github.lzaytseva.vktest.presentation.viewmodel.ViewModelFactory
import com.github.lzaytseva.vktest.util.ErrorType
import javax.inject.Inject

class ProductDetailsFragment() : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private var productId = 0


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProductDetailsViewModel::class.java]
    }
    private val component by lazy {
        (requireActivity().application as App).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            productId = it.getInt(ARGS_PRODUCT_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadDetails(productId)
        configureToolbar()
        observeViewModel()
        setRetryBtnClickListener()
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: ProductDetailsScreenState) {
        when (state) {
            is ProductDetailsScreenState.Loading -> showLoading()
            is ProductDetailsScreenState.Content -> showContent(state.product)
            is ProductDetailsScreenState.Error -> showError(state.error)
        }
    }

    private fun showContent(product: Product) {
        hideViews(progressBarGone = true, errorLayoutGone = true)

        with(binding) {
            Glide.with(ivProductPhoto)
                .load(product.thumbnailUrl)
                .placeholder(R.drawable.product_image_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        resources.getDimensionPixelSize(R.dimen.thumbnail_image_corner_radius)
                    ),
                )
                .into(ivProductPhoto)
            tvName.text = product.name
            tvDescription.text = product.description.trim()
            tvBrand.text = product.brand.trim()
            tvPrice.text = "${product.price}$"
            tvCategory.text = product.category
            tvRating.text = product.rating.toString()
        }
    }

    private fun showError(error: ErrorType) {
        hideViews(progressBarGone = true, contentLayoutGone = true)
        binding.tvError.text = getTextError(error)
    }

    private fun showLoading() {
        hideViews(errorLayoutGone = true, contentLayoutGone = true)
    }


    private fun hideViews(
        errorLayoutGone: Boolean = false,
        progressBarGone: Boolean = false,
        contentLayoutGone: Boolean = false
    ) {
        binding.errorLayout.isGone = errorLayoutGone
        binding.progressBar.isGone = progressBarGone
        binding.content.isGone = contentLayoutGone
    }

    private fun getTextError(error: ErrorType): String {
        return when (error) {
            ErrorType.SERVER_ERROR -> getString(R.string.server_error)
            ErrorType.NO_INTERNET -> getString(R.string.no_internet_error)
            else -> ""
        }
    }

    private fun configureToolbar() {
        (requireActivity() as? MainActivity)?.run {
            toolbar.title = null
            toolbar.navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        }
    }

    private fun setRetryBtnClickListener() {
        binding.btnRetry.setOnClickListener { viewModel.loadDetails(productId) }
    }

    private fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductDetailsBinding {
        return FragmentProductDetailsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGS_PRODUCT_ID = "product_id"

        fun createArgs(productId: Int): Bundle {
            return bundleOf(
                ARGS_PRODUCT_ID to productId
            )
        }
    }
}