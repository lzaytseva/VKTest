package com.github.lzaytseva.vktest.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.lzaytseva.vktest.R
import com.github.lzaytseva.vktest.app.App
import com.github.lzaytseva.vktest.databinding.FragmentProductsBinding
import com.github.lzaytseva.vktest.domain.model.Product
import com.github.lzaytseva.vktest.presentation.state.ProductsScreenState
import com.github.lzaytseva.vktest.presentation.adapter.ProductAdapter
import com.github.lzaytseva.vktest.presentation.viewmodel.ProductsViewModel
import com.github.lzaytseva.vktest.presentation.viewmodel.ViewModelFactory
import com.github.lzaytseva.vktest.util.ErrorType
import com.github.lzaytseva.vktest.util.FeedbackUtils
import javax.inject.Inject

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProductsViewModel::class.java]
    }
    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val adapter = ProductAdapter { productId ->
        findNavController().navigate(
            R.id.action_productsFragment_to_productDetailsFragment,
            ProductDetailsFragment.createArgs(productId)
        )
    }


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeViewModel()
        setRetryBtnClickListener()
        setOnScrollListener()
        configureToolbar()
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: ProductsScreenState) {
        when (state) {
            is ProductsScreenState.Content -> showContent(state.products)
            is ProductsScreenState.Error -> showError(state.error)
            is ProductsScreenState.ErrorLoadingNextPage -> showErrorLoadingNextPage(state)
            ProductsScreenState.Loading -> showLoading()
            ProductsScreenState.LoadingNextData -> showLoadingNextData()
            ProductsScreenState.NoMoreContent -> showNoMoreContent()
        }
    }

    private fun showContent(products: List<Product>) {
        hideViews(progressBarGone = true, errorLayoutGone = true)
        adapter.submitList(products)
    }

    private fun showError(error: ErrorType) {
        hideViews(progressBarGone = true, recyclerViewGone = true)
        binding.tvError.text = getTextError(error)
    }

    private fun showErrorLoadingNextPage(state: ProductsScreenState.ErrorLoadingNextPage) {
        if (state.messageWasShown) return
        hideViews(progressBarGone = true, errorLayoutGone = true)
        FeedbackUtils.showSnackbar(
            root = requireView(),
            text = getTextError(state.error)
        )
        viewModel.setFeedbackWasShown(state)
    }

    private fun showNoMoreContent() {
        hideViews(progressBarGone = true, errorLayoutGone = true)
    }

    private fun showLoading() {
        hideViews(errorLayoutGone = true, recyclerViewGone = true)
    }

    private fun showLoadingNextData() {
        hideViews(errorLayoutGone = true)
    }

    private fun hideViews(
        recyclerViewGone: Boolean = false,
        progressBarGone: Boolean = false,
        errorLayoutGone: Boolean = false
    ) {
        binding.rvProducts.isGone = recyclerViewGone
        binding.progressBar.isGone = progressBarGone
        binding.errorLayout.isGone = errorLayoutGone
    }

    private fun getTextError(error: ErrorType): String {
        return when (error) {
            ErrorType.SERVER_ERROR -> getString(R.string.server_error)
            ErrorType.NO_INTERNET -> getString(R.string.no_internet_error)
            else -> ""
        }
    }

    private fun initRecyclerView() {
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = adapter
    }

    private fun setRetryBtnClickListener() {
        binding.btnRetry.setOnClickListener { viewModel.loadProducts() }
    }

    private fun setOnScrollListener() {
        binding.rvProducts.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        with(binding.rvProducts) {
                            val pos =
                                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            val itemsCount = adapter!!.itemCount
                            if (pos >= itemsCount - 1) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun configureToolbar() {
        (requireActivity() as? MainActivity)?.run {
            toolbar.title = getString(R.string.products_title)
            toolbar.navigationIcon = null
        }
    }

    private fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductsBinding {
        return FragmentProductsBinding.inflate(inflater, container, false)
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
}