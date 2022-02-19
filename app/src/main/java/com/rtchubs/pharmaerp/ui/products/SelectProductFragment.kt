package com.rtchubs.pharmaerp.ui.products

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.SelectProductFragmentBinding
import com.rtchubs.pharmaerp.models.login.Merchant
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.ui.add_product.AllProductListAdapter
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class SelectProductFragment : BaseFragment<SelectProductFragmentBinding, SelectProductViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_select_product
    override val viewModel: SelectProductViewModel by viewModels {
        viewModelFactory
    }

    companion object {
        var selectedProduct: PharmaProduct? = null
        private var allProducts: List<PharmaProduct> = ArrayList()
    }

    lateinit var allProductListAdapter: AllProductListAdapter

    var merchant: Merchant? = null
    lateinit var searchView: SearchView

    override fun onResume() {
        super.onResume()
        viewModel.getProductList(1, "", preferencesHelper.getUser().branch_id ?: 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        merchant = null

        allProductListAdapter = AllProductListAdapter (
            appExecutors
        ) { item ->
            selectedProduct = item
            navController.popBackStack()
        }

        viewDataBinding.productRecycler.adapter = allProductListAdapter

        viewModel.productListResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.data?.products?.product?.let { product ->
                allProducts = product
                showHideDataView()
                allProductListAdapter.submitDataList(allProducts)
            }
        })

    }

    private fun showHideDataView() {
        if (allProducts.isEmpty()) {
            viewDataBinding.container.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.container.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_bar, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(requireActivity().componentName)
        )
        searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha)
        searchView.maxWidth = Int.MAX_VALUE

        // listening to search query text change
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // filter recycler view when query submitted
                allProductListAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // filter recycler view when text is changed
                allProductListAdapter.filter.filter(query)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
            R.id.action_search -> {
                return true
            }
        }
        return true
    }
}