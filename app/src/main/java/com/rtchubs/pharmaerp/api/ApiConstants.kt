package com.rtchubs.pharmaerp.api

import com.rtchubs.pharmaerp.api.Api.API_REPO
import com.rtchubs.pharmaerp.api.Api.API_VERSION
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_ACCOUNT
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_BANK
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_BANK_INFO
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_CARD
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_COMMON
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_CONNECT
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_ERP_LARAVEL
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_HRM_LARAVEL
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_PROFILE
import com.rtchubs.pharmaerp.api.Api.DIRECTORY_USER_ROLE
import com.rtchubs.pharmaerp.api.Api.INDEX

object Api {
    private const val PROTOCOL = "https"
    private const val API_ROOT = "api.dermahealthcare.store"
    const val API_ROOT_URL = "$PROTOCOL://$API_ROOT"
    const val INDEX = "index.php"
    const val API_REPO = "api"
    const val API_VERSION = "v1"
    const val DIRECTORY_ACCOUNT = "account"
    const val DIRECTORY_CONNECT = "connect"
    const val DIRECTORY_BANK_INFO = "bankinformation"
    const val DIRECTORY_CARD = "banklink"
    const val DIRECTORY_BANK = "cardlink"
    const val DIRECTORY_PROFILE = "profile"
    const val DIRECTORY_COMMON= "common"
    const val DIRECTORY_USER_ROLE = "userRole"
    const val DIRECTORY_ERP_LARAVEL = "erpLaravel"
    const val DIRECTORY_HRM_LARAVEL = "hrmLaravel"
    const val ContentType = "Content-Type: application/json"
}

object ApiEndPoint {
    /* Registration */
    const val INQUIRE = "/$API_REPO/$API_VERSION/${DIRECTORY_ACCOUNT}/inquire"
    const val REQUESTOTP = "/$API_REPO/$API_VERSION/${DIRECTORY_ACCOUNT}/request-otp"
    const val REGISTRATION = "/$API_REPO/$API_VERSION/${DIRECTORY_ACCOUNT}"
    const val CONNECT_TOKEN = "/$API_REPO/$API_VERSION/${DIRECTORY_CONNECT}/token"
    const val GET_BANK_LIST = "/$API_REPO/$API_VERSION/${DIRECTORY_BANK_INFO}/bank-list"
    const val ADD_BANK = "/$API_REPO/$API_VERSION/${DIRECTORY_BANK}"
    const val ADD_CARD = "/$API_REPO/$API_VERSION/${DIRECTORY_CARD}"
    const val MY_ACCOUNT_LIST = "/$API_REPO/$API_VERSION/${DIRECTORY_PROFILE}/accounts"

    // Pharma ERP APIs
    const val LOGIN = "/$DIRECTORY_USER_ROLE/$API_REPO/login"
    const val ALL_MALL = "/$API_REPO/shopping-malls"
    const val ALL_MERCHANTS = "/$API_REPO/all-merchants"
    const val PHARMA_PRODUCTS = "/$DIRECTORY_ERP_LARAVEL/$API_REPO/product"
    const val CUSTOMERS = "/$DIRECTORY_ERP_LARAVEL/$API_REPO/customer"
    const val ADD_CUSTOMER = "/$INDEX/$API_REPO/customer"
    const val ADD_PRODUCT = "/$INDEX/$API_REPO/product"
    const val SALE = "/$DIRECTORY_ERP_LARAVEL/$API_REPO/sale"
    const val ORDER_LIST = "/$DIRECTORY_ERP_LARAVEL/$API_REPO/sales/filter"
    const val OFFER_LIST = "/$API_REPO/getshopoffers"
    const val PRODUCT_DETAILS = "/$API_REPO/$DIRECTORY_COMMON/{id}/product"
    const val ADD_OFFER = "/$API_REPO/newshopoffer"
    const val GIFT_POINT_REQUESTS = "/$API_REPO/get/customer/merchant/reward"
    const val GIFT_POINT_HISTORY = "/$API_REPO/get/customer/merchant/reward"
    const val BARCODE_PRODUCTS = "/$API_REPO/product/barcodes"

    const val ATTENDANCE_UPDATE = "/$DIRECTORY_HRM_LARAVEL/$API_REPO/set/track/data"
    const val ATTENDANCE_CHECK_IN_OUT = "/$DIRECTORY_HRM_LARAVEL/$API_REPO/employee/punched"

    const val STOCK_PRODUCTS = "/$API_REPO/product-ledger/bypage"
    const val STOCK_PRODUCTS_DETAILS = "/$API_REPO/show/barcode/attributes/bydetails"
    const val RECEIVE_PRODUCT_IMAGE_UPLOAD = "/$API_REPO/lot-receive/images"
    const val RECEIVE_PRODUCT = "/$API_REPO/lot-receive"
    const val PURCHASE_LIST = "/$API_REPO/purchase"
}

object ResponseCodes {
    const val CODE_SUCCESS = 200
    const val CODE_TOKEN_EXPIRE = 401
    const val CODE_UNAUTHORIZED = 403
    const val CODE_VALIDATION_ERROR = 400
    const val CODE_DEVICE_CHANGE = 451
    const val CODE_FIRST_LOGIN = 426
}

object ApiCallStatus {
    const val LOADING = 0
    const val SUCCESS = 1
    const val ERROR = 2
    const val EMPTY = 3
}
