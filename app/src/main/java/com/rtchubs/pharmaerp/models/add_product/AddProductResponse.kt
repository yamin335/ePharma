package com.rtchubs.pharmaerp.models.add_product

import java.io.Serializable

data class AddProductResponse(val data: AddProductResponseData?)

data class AddProductResponseData(val id: Int?, val name: String?, val barcode: String?,
                                  val description: String?, val buying_price: String?,
                                  val selling_price: String?, val mrp: String?, val expired_date: String?,
                                  val category_id: String?, val merchant_id: String?, val thumbnail: String?,
                                  val product_image1: String?, val product_image2: String?, val product_image3: String?,
                                  val product_image4: String?, val product_image5: String?, val updated_at: String?,
                                  val created_at: String?, val available_qty: Any?): Serializable