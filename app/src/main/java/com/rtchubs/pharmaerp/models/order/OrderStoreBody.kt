package com.rtchubs.pharmaerp.models.order

data class OrderStoreBody(var customer_id: String = "", var subtotal: String = "",
                          var branch_id: String = "", var salesman_id: String = "",
                          var YourReference: String = "", var OurReference: String = "",
                          var discount: String = "", var discount_amount: String = "",
                          var discount_type: String = "", var tax: String = "",
                          var products: List<OrderStoreProduct> = ArrayList(),
                          var grand_total: String = "" )

data class OrderStoreProduct(var product_name: String? = "" , var description: String? = "" ,
                             var price: Int? = 0, var quantity: Int? = 0, var amount: Int? = 0,
                             var selectedProduct: String? = "" , var product_id: Int = 0,
                             var available_qty: Int? = 0)
