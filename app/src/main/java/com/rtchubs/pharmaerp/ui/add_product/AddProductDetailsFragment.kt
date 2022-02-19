package com.rtchubs.pharmaerp.ui.add_product

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.AddProductDetailsFragmentBinding
import com.rtchubs.pharmaerp.models.add_product.AddProductResponse
import com.rtchubs.pharmaerp.ui.common.BaseFragment

private const val FEATURE_IMAGE = 1
private const val SAMPLE_IMAGE = 2
class AddProductDetailsFragment : BaseFragment<AddProductDetailsFragmentBinding, AddProductDetailsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_product_details
    override val viewModel: AddProductDetailsViewModel by viewModels {
        viewModelFactory
    }

    var placeholder: Drawable? = null

    lateinit var sampleImageAdapter: ProductDetailsSampleImageListAdapter
    lateinit var product: AddProductResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        placeholder = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.image_placeholder
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        //product = AllProductsFragment.allProductsList[productId]

        sampleImageAdapter = ProductDetailsSampleImageListAdapter()
        viewDataBinding.sampleImageRecycler.adapter = sampleImageAdapter

        //viewDataBinding.featureImage.setImageBitmap(product.featureImage)
//        if (!product.sampleImages.isNullOrEmpty()) {
//            sampleImageAdapter.submitImageList(product.sampleImages)
//        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
        }
        return true
    }

    companion object {
        var productId: Int = 0
    }
}