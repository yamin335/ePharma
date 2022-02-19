package com.rtchubs.pharmaerp.util

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import java.io.File

object PrinterUtils {
    fun printPDF(context: Context, file: File) {
        val manager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val adapter = PDFDocumentAdapter(file)
        val attributes = PrintAttributes.Builder().build()
        manager.print("Document", adapter, attributes)
    }
}