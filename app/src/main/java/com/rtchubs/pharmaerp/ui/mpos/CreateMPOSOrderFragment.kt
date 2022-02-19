package com.rtchubs.pharmaerp.ui.mpos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.BuildConfig
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.api.ApiCallStatus
import com.rtchubs.pharmaerp.databinding.CreateMPOSOrderFragmentBinding
import com.rtchubs.pharmaerp.models.MPOSOrderProductsRequestBody
import com.rtchubs.pharmaerp.models.order.OrderStoreProduct
import com.rtchubs.pharmaerp.models.order.OrderStoreBody
import com.rtchubs.pharmaerp.ui.barcode_reader.LiveBarcodeScanningActivity
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.common.CommonAlertDialog
import com.rtchubs.pharmaerp.ui.customers.SelectCustomerFragment
import com.rtchubs.pharmaerp.ui.order.OrderProductListAdapter
import com.rtchubs.pharmaerp.ui.products.SelectProductFragment
import com.rtchubs.pharmaerp.util.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateMPOSOrderFragment : BaseFragment<CreateMPOSOrderFragmentBinding, CreateMPOSOrderViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_create_mpos_order
    override val viewModel: CreateMPOSOrderViewModel by viewModels {
        viewModelFactory
    }

    lateinit var orderProductListAdapter: OrderProductListAdapter

    lateinit var permissionRequestLauncher: ActivityResultLauncher<Array<String>>
    lateinit var qrCodeScannerLauncher: ActivityResultLauncher<Intent>

    val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA
    )

    var taxType = ""

    var taxTypeValues = arrayOf("", "inclusive", "exclusive")

    var taxTypes = arrayOf("VAT/TAX Type", "VAT/TAX Inclusive", "VAT/TAX Exclusive")

    var total = 0.0

    override fun onResume() {
        super.onResume()

        SelectCustomerFragment.selectedCustomer?.let {
            viewModel.selectedCustomer.postValue(it)
            SelectCustomerFragment.selectedCustomer = null
        }

        SelectProductFragment.selectedProduct?.let { pharmaProduct ->
            val list = viewModel.orderItems.value ?: mutableListOf()
            val createOrderPharmaProduct = OrderStoreProduct(pharmaProduct.name, pharmaProduct.description,
                pharmaProduct.mrp, 1, 0, pharmaProduct.name, pharmaProduct.id ?: -1, pharmaProduct.available_qty)
            if (list.isNotEmpty()) {
                var isAlreadyAdded = false
                var index = 0
                while (index < list.size) {
                    val item = list[index]
                    isAlreadyAdded = item.product_id == createOrderPharmaProduct.product_id
                    index++
                }
                if (isAlreadyAdded) {
                    viewModel.incrementOrderItemQuantity(createOrderPharmaProduct.product_id)
                } else {
                    val qty = createOrderPharmaProduct.quantity ?: 1
                    val price = createOrderPharmaProduct.price ?: 0
                    createOrderPharmaProduct.amount = qty * price
                    viewModel.orderItems.addNewItem(createOrderPharmaProduct)
                }
            } else {
                val qty = createOrderPharmaProduct.quantity ?: 1
                val price = createOrderPharmaProduct.price ?: 0
                createOrderPharmaProduct.amount = qty * price
                viewModel.orderItems.addNewItem(createOrderPharmaProduct)
            }
            SelectProductFragment.selectedProduct = null
        }

        if (viewModel.selectedCustomer.value == null) {
            viewDataBinding.customerName.text = "Select Customer"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SelectCustomerFragment.selectedCustomer = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
        viewModel.generateInvoiceID()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        orderProductListAdapter = OrderProductListAdapter (
            appExecutors,
            object : OrderProductListAdapter.CartItemActionCallback {
                override fun incrementCartItemQuantity(id: Int) {
                    viewModel.incrementOrderItemQuantity(id)
                }

                override fun decrementCartItemQuantity(id: Int) {
                    viewModel.decrementOrderItemQuantity(id)
                }

            }
        ) { item ->
            viewModel.orderItems.removeItem(item)
        }

        viewDataBinding.productRecycler.adapter = orderProductListAdapter

        viewModel.selectedCustomer.observe(viewLifecycleOwner, Observer { customer ->
            customer?.let {
                viewDataBinding.customerDetails.visibility = View.VISIBLE
            }
        })

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, taxTypes)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.spinnerVatTax.adapter = categoryAdapter

        viewDataBinding.spinnerVatTax.onItemSelectedListener =
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

        viewModel.orderPlaceResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (it.data?.sale != null) {
                    showSuccessToast(requireContext(), "Order placed successfully!")
                    navController.popBackStack()
                }
            }
        })

        viewModel.apiCallStatus.observe(viewLifecycleOwner, Observer {
            viewDataBinding.btnSubmitOrder.isEnabled = it != ApiCallStatus.LOADING
        })

        viewDataBinding.selectCustomer.setOnClickListener {
            SelectCustomerFragment.selectedCustomer = null
            navigateTo(CreateMPOSOrderFragmentDirections.actionCreateMPOSOrderFragmentToSelectCustomerNavGraph())
        }

        viewDataBinding.btnAddProduct.setOnClickListener {
            //scanQRCode()
            SelectProductFragment.selectedProduct = null
            navigateTo(CreateMPOSOrderFragmentDirections.actionCreateMPOSOrderFragmentToSelectProductNavGraph())
        }

        viewModel.orderItems.observe(viewLifecycleOwner, Observer { orderItems ->
            orderItems?.let {
                showHideDataView()
                orderProductListAdapter.submitList(it)
                orderProductListAdapter.notifyDataSetChanged()
                total = 0.0
                it.forEach { item ->
                    total += item.amount ?: 0
                }
                total = total.toRounded(2)
                viewDataBinding.totalPrice = total.toString()
                viewDataBinding.linearTotal.visibility = View.VISIBLE
            }
        })

        viewDataBinding.btnSubmitOrder.setOnClickListener {
            if (viewModel.selectedCustomer.value == null) {
                showWarningToast(requireContext(), "Please select customer")
                return@setOnClickListener
            }

            val productList = viewModel.orderItems.value ?: ArrayList()

            if (viewModel.orderItems.value.isNullOrEmpty()) {
                showWarningToast(requireContext(), "Please select product")
                return@setOnClickListener
            }

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(
                Date()
            )

            viewModel.placeOrder(
                OrderStoreBody(
                    viewModel.selectedCustomer.value?.id?.toString() ?: "",
                    total.toString(),
                    preferencesHelper.getUser().branch_id?.toString() ?: "1",
                    preferencesHelper.getUser().id?.toString() ?: return@setOnClickListener,
                    "", viewModel.invoiceNumber.value ?: return@setOnClickListener,
                    "0", "0", "percentage",
                    "0", productList, total.toString()
                )
            )
        }

        permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }

            if (granted) {
                scanQRCode()
            } else {
                val shouldShowPermissionRationaleDialog = PermissionUtils.checkPermissionRationale(
                    requireActivity() as AppCompatActivity,
                    requiredPermissions)

                if (shouldShowPermissionRationaleDialog) {
                    val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.ActionCallback{
                        override fun onYes() {
                            permissionRequestLauncher.launch(requiredPermissions)
                        }

                        override fun onNo() {

                        }
                    }, "Allow Permissions!", "Allow location and camera permissions to " +
                            "use this feature.\n\nDo you want to allow permission?")
                    explanationDialog.show(childFragmentManager, "#call_permission_dialog")
                } else {
                    val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.ActionCallback{
                        override fun onYes() {
                            PermissionUtils.goToSettings(requireContext(), BuildConfig.APPLICATION_ID)
                        }

                        override fun onNo() {

                        }
                    }, "Allow Permissions!", "Allow location and camera permissions to " +
                            "use this feature.\n\nDo you want to allow permission?")
                    explanationDialog.show(childFragmentManager, "#call_permission_dialog")
                }
            }
        }

        qrCodeScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val intent = result.data ?: return@registerForActivityResult
            if (intent.hasExtra("barcode_result")) {
                val qrCodeData = intent.getStringExtra("barcode_result") ?: ""
                if (qrCodeData.isNotBlank()) {
                    val dataArray = qrCodeData.split(",")
                    if (dataArray.isNotEmpty()) {
                        val codes = ArrayList<Long>()
                        for(data in dataArray) {
                            try {
                                val code = data.toLong()
                                codes.add(code)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                showErrorToast(requireContext(), "Invalid Barcode Found!")
                            }
                        }
                        viewModel.getProductsByBarcodes(MPOSOrderProductsRequestBody(codes))
                    } else {
                        showErrorToast(requireContext(), "No product added!")
                    }
                } else {
                    showErrorToast(requireContext(), "Invalid QR Code!")
                }
            }
        }

//        if (viewModel.orderItems.value.isNullOrEmpty()) {
//            scanQRCode()
//        }
    }


    private fun scanQRCode() {
        if (PermissionUtils.checkPermission(
                requireActivity() as AppCompatActivity,
                requiredPermissions
            )) {
            qrCodeScannerLauncher.launch(Intent(requireActivity(), LiveBarcodeScanningActivity::class.java))
        } else {
            permissionRequestLauncher.launch(requiredPermissions)
        }
    }

    private fun showHideDataView() {
        if (viewModel.orderItems.value?.isEmpty() == true) {
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