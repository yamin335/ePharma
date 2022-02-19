package com.rtchubs.pharmaerp.ui.offers

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.api.ApiCallStatus
import com.rtchubs.pharmaerp.databinding.CreateOfferFragmentBinding
import com.rtchubs.pharmaerp.models.OfferStoreBody
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.customers.SelectCustomerFragment
import com.rtchubs.pharmaerp.ui.products.SelectProductFragment
import com.rtchubs.pharmaerp.util.*
import java.util.*
import kotlin.collections.ArrayList

class CreateOfferFragment : BaseFragment<CreateOfferFragmentBinding, CreateOfferViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_create_offer
    override val viewModel: CreateOfferViewModel by viewModels {
        viewModelFactory
    }

    lateinit var offerProductListAdapter: OfferProductListAdapter

    var taxType = ""

    var taxTypeValues = arrayOf("", "inclusive", "exclusive")

    var taxTypes = arrayOf("Select Offer Type", "VAT/TAX Inclusive", "VAT/TAX Exclusive")

    var total = 0.0

    override fun onResume() {
        super.onResume()

        SelectProductFragment.selectedProduct?.let {
            val list = viewModel.offerItems.value ?: mutableListOf()
            if (!list.contains(it)) {
                //it.available_qty = 1
                viewModel.offerItems.addNewItem(it)
            }
            SelectProductFragment.selectedProduct = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SelectCustomerFragment.selectedCustomer = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        offerProductListAdapter = OfferProductListAdapter (appExecutors) { item ->
            //viewModel.offerItems.removeItem(item)
        }

        viewDataBinding.productRecycler.adapter = offerProductListAdapter

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, taxTypes)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.spinnerOfferType.adapter = categoryAdapter

        viewDataBinding.spinnerOfferType.onItemSelectedListener =
            object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taxType = taxTypeValues[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }

        viewModel.newOfferResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            response?.let {
                if (it.data != null) {
                    showSuccessToast(requireContext(), "Offer successfully added!")
                    navController.popBackStack()
                }
            }
        })

        viewModel.apiCallStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewDataBinding.btnSubmitOffer.isEnabled = it != ApiCallStatus.LOADING
            viewDataBinding.btnAddProduct.isEnabled = it != ApiCallStatus.LOADING
        })

        viewDataBinding.btnAddProduct.setOnClickListener {
            SelectProductFragment.selectedProduct = null
            navigateTo(CreateOfferFragmentDirections.actionCreateOfferFragmentToSelectProductNavGraph())
        }

        viewModel.offerItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer { orderItems ->
            orderItems?.let {
                val amountString = viewModel.offerPercent.value ?: "0"
                val amount = amountString.toInt()
                viewDataBinding.btnSubmitOffer.isEnabled = it.isNotEmpty() && viewModel.apiCallStatus.value != ApiCallStatus.LOADING && amount > 0
                showHideDataView()
                //offerProductListAdapter.submitList(it)
                offerProductListAdapter.notifyDataSetChanged()
            }
        })

        viewModel.offerPercent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val amountString = it ?: "0"
            val amount = amountString.toInt()
            viewDataBinding.btnSubmitOffer.isEnabled = !viewModel.offerItems.value.isNullOrEmpty() &&
                    viewModel.apiCallStatus.value != ApiCallStatus.LOADING && amount > 0
        })

        viewDataBinding.btnSubmitOffer.setOnClickListener {
            val items = viewModel.offerItems.value ?: ArrayList()
            val ids = ArrayList<Int>()
            for (item in items) {
                //ids.add(item.id)
            }
            val offerStoreBody = OfferStoreBody(viewModel.offerNote.value, viewModel.offerPercent.value,
                null, null, ids, "", preferencesHelper.merchantId)
            viewModel.addNewOffer(offerStoreBody)
//            if (viewModel.selectedCustomer.value == null) {
//                showWarningToast(requireContext(), "Please select customer")
//                return@setOnClickListener
//            }
//
//            if (viewModel.orderItems.value.isNullOrEmpty()) {
//                showWarningToast(requireContext(), "Please select product")
//                return@setOnClickListener
//            }
//
//            val productList = ArrayList<OrderStoreProduct>()
//            viewModel.orderItems.value?.forEach { item ->
//                val quantity = item.quantity ?: 1
//                val mrp = item.mrp?.toInt() ?: 0
//                productList.add(OrderStoreProduct(item.id, item.description, "qty",
//                    quantity, item.mrp?.toInt(), 0, "0",
//                    0, "0", mrp * quantity, ""))
//            }
//
//            val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(
//                Date()
//            )
//
//            viewModel.placeOrder(OrderStoreBody(viewModel.selectedCustomer.value?.id, preferencesHelper.merchantId,
//                "", viewModel.invoiceNumber.value, today, taxType, "", total.toInt(),
//                0, 0, total.toInt(), 0, total.toInt(), productList))

        }

    }

    private fun showHideDataView() {
        if (viewModel.offerItems.value?.isEmpty() == true) {
            viewDataBinding.productRecycler.visibility = View.GONE
            viewDataBinding.textNoProductsFound.visibility = View.VISIBLE
        } else {
            viewDataBinding.productRecycler.visibility = View.VISIBLE
            viewDataBinding.textNoProductsFound.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
        }
        return true
    }
}