package com.rtchubs.pharmaerp.models

data class MPOSOrderProduct(val id: Int?, val product_id: Int?, val product_detail_id: Int?,
                          val merchant_id: Int?, val attribute_id: Int?, val barcode: String?,
                          val created_at: String?, val updated_at: String?, val image: Any?,
                          val status: Int?, val receive_date: String?, val product: Product?,
                          val product_detail: ProductDetails?, val attributes: List<Attribute>?)

data class MPOSOrderProductsRequestBody(val barcodes: ArrayList<Long>)