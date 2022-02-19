package com.rtchubs.pharmaerp.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.binding.FragmentDataBindingComponent
import com.rtchubs.pharmaerp.databinding.CustomLoaderDialogFragmentBinding


class CustomLoadingDialogFragment: DialogFragment() {
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent()

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.AppTransparentDialog)

        val inflater = requireActivity().layoutInflater
        // Inflate the layout for this fragment
        val binding: CustomLoaderDialogFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_custom_loader,
            null,
            false,
            dataBindingComponent
        )

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}