package com.rtchubs.pharmaerp.ui.barcode_print

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.PrintBarcodeFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.util.AppConstants
import com.rtchubs.pharmaerp.util.FileUtils
import com.rtchubs.pharmaerp.util.PrinterUtils
import com.rtchubs.pharmaerp.util.showErrorToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PrintBarcodeFragment : BaseFragment<PrintBarcodeFragmentBinding, BarcodePrintViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_print_barcode
    override val viewModel: BarcodePrintViewModel by viewModels {
        viewModelFactory
    }

    val args: PrintBarcodeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_print, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
            R.id.menu_print -> {
                val url = args.downloadUrl

                if (url.isNotBlank()) {
                    val filepath = FileUtils.getLocalStorageFilePath(
                        requireContext(),
                        AppConstants.downloadedPdfFiles
                    )

                    val fileName = url.split("/").last()
                    val pdfFile = File("$filepath/$fileName")
                    if (pdfFile.exists()) {
                        PrinterUtils.printPDF(requireContext(), pdfFile)
                    } else {
                        viewModel.downloadPdfFile(url, filepath, fileName)
                    }
                }
            }
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        viewModel.showHideProgress.observe(viewLifecycleOwner, Observer { pair ->
            val shouldShowProgress = pair.first
            val progress = pair.second
            if (shouldShowProgress) {
                viewDataBinding.progressView.visibility = View.VISIBLE
                viewDataBinding.loader.progress = progress
                viewDataBinding.progress.text = "$progress%"
                if (progress == 100) viewDataBinding.progressView.visibility = View.GONE
            } else {
                viewDataBinding.progressView.visibility = View.GONE
            }
        })

        viewModel.pdfDownloadResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.first) {
                loadPDF(File(response.second))
            } else {
                showErrorToast(requireContext(), "Error downloading report!")
            }
        })

        val url = args.downloadUrl

        if (url.isNotBlank()) {
            val filepath = FileUtils.getLocalStorageFilePath(
                requireContext(),
                AppConstants.downloadedPdfFiles
            )

            val fileName = url.split("/").last()
            val pdfFile = File("$filepath/$fileName")
            if (pdfFile.exists()) {
                loadPDF(pdfFile)
            } else {
                viewModel.downloadPdfFile(url, filepath, fileName)
            }
        }
    }

    private fun loadPDF(file: File) {
        if (file.exists()) {
            try {
                lifecycleScope.launch(Dispatchers.Main.immediate) {
                    try {
                        viewDataBinding.pdfView.fromFile(file)
                            .pageFitPolicy(FitPolicy.WIDTH)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .onError {
                                showErrorToast(requireContext(), "Error showing report!")
                            }
                            .load()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}