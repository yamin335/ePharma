package com.rtchubs.pharmaerp.ui.customers

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
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.SelectCustomerFragmentBinding
import com.rtchubs.pharmaerp.models.customers.Customer
import com.rtchubs.pharmaerp.models.login.Merchant
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import java.util.*

class SelectCustomerFragment : BaseFragment<SelectCustomerFragmentBinding, SelectCustomerViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_select_customer
    override val viewModel: SelectCustomerViewModel by viewModels {
        viewModelFactory
    }

    companion object {
        var selectedCustomer: Customer? = null
        private var allCustomers: List<Customer> = ArrayList()
    }

    lateinit var allCustomersListAdapter: AllCustomersListAdapter

    var merchant: Merchant? = null
    lateinit var searchView: SearchView

    override fun onResume() {
        super.onResume()
        if (allCustomers.isEmpty()) {
            viewModel.getCustomers(1, "")
        } else {
            allCustomersListAdapter.submitDataList(allCustomers)
            showHideDataView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        merchant = null

        allCustomersListAdapter = AllCustomersListAdapter (
            appExecutors
        ) { item ->
            selectedCustomer = item
            navController.popBackStack()
        }

        viewDataBinding.allCustomersRecycler.adapter = allCustomersListAdapter

        viewModel.customerList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { customers ->
            customers?.let {
                allCustomers = it
                showHideDataView()
                allCustomersListAdapter.submitDataList(allCustomers)
            }
        })

    }

    private fun showHideDataView() {
        if (allCustomers.isEmpty()) {
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
                allCustomersListAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // filter recycler view when text is changed
                allCustomersListAdapter.filter.filter(query)
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