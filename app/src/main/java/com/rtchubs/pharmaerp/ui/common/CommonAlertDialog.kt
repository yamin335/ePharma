package com.rtchubs.pharmaerp.ui.common

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rtchubs.pharmaerp.R

class CommonAlertDialog internal constructor(private val callBack: ActionCallback, private val title: String, private val subTitle: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val exitDialog: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setIcon(R.mipmap.ic_launcher)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                callBack.onYes()
                dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                callBack.onNo()
                dialog.cancel()
            }
        if (subTitle.isNotBlank()) {
            exitDialog.setMessage(subTitle)
        }
        return exitDialog.create()
    }

    interface ActionCallback{
        fun onYes()
        fun onNo()
    }
}