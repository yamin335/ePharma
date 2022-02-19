package com.rtchubs.pharmaerp.ui.add_product

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.BuildConfig
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.api.ApiCallStatus
import com.rtchubs.pharmaerp.databinding.AddProductFragmentBinding
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.util.BitmapUtilss
import com.rtchubs.pharmaerp.util.PermissionUtils.isCameraAndGalleryPermissionGranted
import com.rtchubs.pharmaerp.util.PermissionUtils.isCameraPermission
import com.rtchubs.pharmaerp.util.PermissionUtils.isGalleryPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_REQUEST_CODE = 111
private const val FEATURE_IMAGE = 1
private const val SAMPLE_IMAGE = 2
private const val IMAGE_FOLDER_NAME = "product_images"
class AddProductFragment : BaseFragment<AddProductFragmentBinding, AddProductViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_product
    override val viewModel: AddProductViewModel by viewModels {
        viewModelFactory
    }

    var placeholder: Drawable? = null
    var featureImage: Bitmap? = null
    var imageType = 0
    var imageForPosition = 0

    lateinit var picFromCameraLauncher: ActivityResultLauncher<Intent>
    lateinit var picFromGalleryLauncher: ActivityResultLauncher<Intent>

    lateinit var currentPhotoPath: String

    lateinit var sampleImageAdapter: SampleImageListAdapter

    var product: PharmaProduct? = null

    val args: AddProductFragmentArgs by navArgs()
    var isEditMode = false

    var productCategory = ""

    var productCategories = arrayOf("Product Category", "Shirt", "Pant", "Skirt", "Shoes", "Hats")

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

        isEditMode = args.isEdit

        sampleImageAdapter = SampleImageListAdapter {
            imageForPosition = it
            imageType = SAMPLE_IMAGE
            captureImage()
        }

        viewDataBinding.sampleImageRecycler.adapter = sampleImageAdapter

        var i = 0
        while (i < 5) {
            sampleImageAdapter.setImage(null, i)
            i++
        }

        checkImages()

        viewDataBinding.llAddFeatureImage.setOnClickListener {
            imageType = FEATURE_IMAGE
            captureImage()
        }

        viewDataBinding.btnAddFeatureImage.setOnClickListener {
            imageType = FEATURE_IMAGE
            captureImage()
        }

        picFromCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val file = File(currentPhotoPath)
            val imageBitmap = BitmapUtilss.getBitmapFromContentUri(
                requireContext().contentResolver, Uri.fromFile(
                    file
                )
            )
            val bitmap = imageBitmap ?: return@registerForActivityResult

            when(imageType) {
                FEATURE_IMAGE -> {
                    featureImage = BitmapUtilss.getResizedBitmap(bitmap, 700)
                    Glide.with(requireContext())
                        .load(featureImage)
                        .centerCrop()
                        .placeholder(placeholder)
                        .into(viewDataBinding.featureImage)
                }

                SAMPLE_IMAGE -> {
                    sampleImageAdapter.setImage(bitmap, imageForPosition)
                }
            }
            checkImages()
        }

        picFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val photoUri = result?.data?.data
            photoUri?.let {
                val imageBitmap = BitmapUtilss.getBitmapFromContentUri(
                    requireContext().contentResolver, photoUri
                )
                val bitmap = imageBitmap ?: return@registerForActivityResult

                when(imageType) {
                    FEATURE_IMAGE -> {
                        featureImage = BitmapUtilss.getResizedBitmap(bitmap, 700)
                        Glide.with(requireContext())
                            .load(featureImage)
                            .centerCrop()
                            .placeholder(placeholder)
                            .into(viewDataBinding.featureImage)
                    }

                    SAMPLE_IMAGE -> {
                        sampleImageAdapter.setImage(bitmap, imageForPosition)
                    }

                    else -> {}
                }

                checkImages()
            }
        }

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, productCategories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.spinnerCategory.adapter = categoryAdapter

        viewDataBinding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                productCategory = if (position != 0) {
                    productCategories[position]
                } else {
                    ""
                }
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

        viewModel.addProductResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            response?.let {
                if (it.data != null) navController.popBackStack()
            }
        })

        viewModel.apiCallStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewDataBinding.btnAddProduct.isEnabled = it != ApiCallStatus.LOADING
        })

        product = args.product
        val item = product ?: return

        if (isEditMode) {
//            Glide.with(this)
//                .asBitmap()
//                .load(item.thumbnail)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        featureImage = resource
//                        viewDataBinding.featureImage.setImageBitmap(featureImage)
//                        checkImages()
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })
//
//            Glide.with(this)
//                .asBitmap()
//                .load(item.product_image1)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        sampleImageAdapter.setImage(resource, 0)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })
//
//            Glide.with(this)
//                .asBitmap()
//                .load(item.product_image2)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        sampleImageAdapter.setImage(resource, 1)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })
//
//            Glide.with(this)
//                .asBitmap()
//                .load(item.product_image3)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        sampleImageAdapter.setImage(resource, 2)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })
//
//            Glide.with(this)
//                .asBitmap()
//                .load(item.product_image4)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        sampleImageAdapter.setImage(resource, 3)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })
//
//            Glide.with(this)
//                .asBitmap()
//                .load(item.product_image5)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        sampleImageAdapter.setImage(resource, 4)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                    }
//                })

            viewModel.name.postValue(item.name)
            viewModel.buyingPrice.postValue(item.buying_price?.toString())
            viewModel.sellingPrice.postValue(item.selling_price?.toString())
            viewModel.mrp.postValue(item.mrp?.toString())
            viewModel.expiredDate.postValue(item.expired_date)
            viewModel.description.postValue(item.description)
        } else {
            viewModel.name.postValue(item.name)
            viewModel.buyingPrice.postValue(item.buying_price?.toString())
            viewModel.sellingPrice.postValue(item.selling_price?.toString())
            viewModel.mrp.postValue(item.mrp?.toString())
            viewModel.expiredDate.postValue(item.expired_date)
            viewModel.description.postValue(item.description)
        }

        viewDataBinding.btnAddProduct.setOnClickListener {
            val sampleImages = sampleImageAdapter.getImageList()
            if (BitmapUtilss.makeEmptyFolderIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME)) {
                var thumbPath: String? = null
                var image1Path: String? = null
                var image2Path: String? = null
                var image3Path: String? = null
                var image4Path: String? = null
                var image5Path: String? = null

                featureImage?.let {
                    val file = BitmapUtilss.makeEmptyFileIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME, "thumbnail.jpg")
                    if (file.exists()) file.delete()

                    try {
                        BitmapUtilss.saveBitmapFileIntoExternalStorageWithTitle(it, file)
                    } catch (e: IOException) {
                        return@let
                    }
                    thumbPath = file.path
                }

                sampleImages[0]?.let {
                    val file = BitmapUtilss.makeEmptyFileIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME, "product_image1.jpg")
                    if (file.exists()) file.delete()

                    try {
                        BitmapUtilss.saveBitmapFileIntoExternalStorageWithTitle(it, file)
                    } catch (e: IOException) {
                        return@let
                    }
                    image1Path = file.path
                }

                sampleImages[1]?.let {
                    val file = BitmapUtilss.makeEmptyFileIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME, "product_image2.jpg")
                    if (file.exists()) file.delete()

                    try {
                        BitmapUtilss.saveBitmapFileIntoExternalStorageWithTitle(it, file)
                    } catch (e: IOException) {
                        return@let
                    }
                    image2Path = file.path
                }

                sampleImages[2]?.let {
                    val file = BitmapUtilss.makeEmptyFileIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME, "product_image3.jpg")
                    if (file.exists()) file.delete()

                    try {
                        BitmapUtilss.saveBitmapFileIntoExternalStorageWithTitle(it, file)
                    } catch (e: IOException) {
                        return@let
                    }
                    image3Path = file.path
                }

                sampleImages[3]?.let {
                    val file = BitmapUtilss.makeEmptyFileIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME, "product_image4.jpg")
                    if (file.exists()) file.delete()

                    try {
                        BitmapUtilss.saveBitmapFileIntoExternalStorageWithTitle(it, file)
                    } catch (e: IOException) {
                        return@let
                    }
                    image4Path = file.path
                }

                sampleImages[4]?.let {
                    val file = BitmapUtilss.makeEmptyFileIntoExternalStorageWithTitle(requireContext(), IMAGE_FOLDER_NAME, "product_image5.jpg")
                    if (file.exists()) file.delete()

                    try {
                        BitmapUtilss.saveBitmapFileIntoExternalStorageWithTitle(it, file)
                    } catch (e: IOException) {
                        return@let
                    }
                    image5Path = file.path
                }


                viewModel.addProduct(thumbPath, image1Path, image2Path, image3Path, image4Path, image5Path,1, preferencesHelper.merchantId, "")
            }
        }

    }

    private fun checkImages() {
        if (featureImage == null) {
            viewDataBinding.btnAddFeatureImage.visibility = View.GONE
            viewDataBinding.llAddFeatureImage.visibility = View.VISIBLE
        } else {
            viewDataBinding.btnAddFeatureImage.visibility = View.VISIBLE
            viewDataBinding.llAddFeatureImage.visibility = View.GONE
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

    private fun captureImage() {
        if (isCameraAndGalleryPermissionGranted(requireActivity())) {
            selectImage()
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            getString(R.string.take_picture), getString(R.string.choose_from_gallery),
            getString(R.string.cancel)
        )
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(getString(R.string.choose_an_option))
        builder.setItems(items) { dialog: DialogInterface, item: Int ->
            if (items[item] == getString(R.string.take_picture)) {
                if (isCameraPermission(requireActivity())) {
                    captureImageFromCamera()
                }
            } else if (items[item] == getString(R.string.choose_from_gallery)) {
                if (isGalleryPermission(requireActivity())) {
                    chooseImageFromGallery()
                }
            } else if (items[item] == getString(R.string.cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun captureImageFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${BuildConfig.APPLICATION_ID}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    val flags: Int = takePictureIntent.flags
                    // check that the nested intent does not grant URI permissions
                    if (flags and Intent.FLAG_GRANT_READ_URI_PERMISSION == 0 &&
                        flags and Intent.FLAG_GRANT_WRITE_URI_PERMISSION == 0
                    ) {
                        // redirect the nested Intent
                        picFromCameraLauncher.launch(takePictureIntent)
                    }
                }
            }
        }
    }

    private fun chooseImageFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK
        )
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        galleryIntent.resolveActivity(requireActivity().packageManager)?.let {
            val flags: Int = galleryIntent.flags
            // check that the nested intent does not grant URI permissions
            if (flags and Intent.FLAG_GRANT_READ_URI_PERMISSION == 0 &&
                flags and Intent.FLAG_GRANT_WRITE_URI_PERMISSION == 0
            ) {
                // redirect the nested Intent
                picFromGalleryLauncher.launch(galleryIntent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isCameraAndGalleryPermissionGranted(requireActivity())) {
                    selectImage()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}