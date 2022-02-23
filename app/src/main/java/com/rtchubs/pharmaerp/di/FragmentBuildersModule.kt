package com.rtchubs.pharmaerp.di

import com.rtchubs.pharmaerp.nid_scan.NIDScanCameraXFragment
import com.rtchubs.pharmaerp.ui.add_payment_methods.AddBankFragment
import com.rtchubs.pharmaerp.ui.add_payment_methods.AddCardFragment
import com.rtchubs.pharmaerp.ui.add_payment_methods.AddPaymentMethodsFragment
import com.rtchubs.pharmaerp.ui.add_product.AddProductFragment
import com.rtchubs.pharmaerp.ui.add_product.AllProductsFragment
import com.rtchubs.pharmaerp.ui.attendance.AttendanceFragment
import com.rtchubs.pharmaerp.ui.barcode_print.PrintBarcodeFragment
import com.rtchubs.pharmaerp.ui.cart.CartFragment
import com.rtchubs.pharmaerp.ui.chapter_list.ChapterListFragment
import com.rtchubs.pharmaerp.ui.customers.AddCustomerFragment
import com.rtchubs.pharmaerp.ui.customers.AllCustomersFragment
import com.rtchubs.pharmaerp.ui.customers.SelectCustomerFragment
import com.rtchubs.pharmaerp.ui.exams.ExamsFragment
import com.rtchubs.pharmaerp.ui.gift_point.GiftPointHistoryDetailsFragment
import com.rtchubs.pharmaerp.ui.gift_point.GiftPointHistoryFragment
import com.rtchubs.pharmaerp.ui.gift_point.GiftPointRequestListDetailsFragment
import com.rtchubs.pharmaerp.ui.gift_point.GiftPointRequestListFragment
import com.rtchubs.pharmaerp.ui.home.*
import com.rtchubs.pharmaerp.ui.how_works.HowWorksFragment
import com.rtchubs.pharmaerp.ui.info.InfoFragment
import com.rtchubs.pharmaerp.ui.login.SignInFragment
import com.rtchubs.pharmaerp.ui.login.ViewPagerFragment
import com.rtchubs.pharmaerp.ui.more.MoreFragment
import com.rtchubs.pharmaerp.ui.mpos.CreateMPOSOrderFragment
import com.rtchubs.pharmaerp.ui.mpos.MPOSFragment
import com.rtchubs.pharmaerp.ui.mpos.MPOSOrderDetailsFragment
import com.rtchubs.pharmaerp.ui.myAccount.MyAccountsFragment
import com.rtchubs.pharmaerp.ui.myDevices.MyDevicesFragment
import com.rtchubs.pharmaerp.ui.offers.CreateOfferFragment
import com.rtchubs.pharmaerp.ui.offers.OffersFragment
import com.rtchubs.pharmaerp.ui.order.CreateOrderFragment
import com.rtchubs.pharmaerp.ui.order.OrderListFragment
import com.rtchubs.pharmaerp.ui.order.OrderTrackHistoryFragment
import com.rtchubs.pharmaerp.ui.otp.OtpFragment
import com.rtchubs.pharmaerp.ui.otp_signin.OtpSignInFragment
import com.rtchubs.pharmaerp.ui.pin_number.PinNumberFragment
import com.rtchubs.pharmaerp.ui.pre_on_boarding.PreOnBoardingFragment
import com.rtchubs.pharmaerp.ui.products.SelectProductFragment
import com.rtchubs.pharmaerp.ui.profile_signin.ProfileSignInFragment
import com.rtchubs.pharmaerp.ui.profiles.ProfilesFragment
import com.rtchubs.pharmaerp.ui.purchase_list.ProductPurchaseFragment
import com.rtchubs.pharmaerp.ui.registration.RegistrationFragment
import com.rtchubs.pharmaerp.ui.settings.SettingsFragment
import com.rtchubs.pharmaerp.ui.setup.SetupFragment
import com.rtchubs.pharmaerp.ui.setup_complete.SetupCompleteFragment
import com.rtchubs.pharmaerp.ui.shops.ShopDetailsContactUsFragment
import com.rtchubs.pharmaerp.ui.shops.ShopDetailsFragment
import com.rtchubs.pharmaerp.ui.shops.ShopDetailsProductListFragment
import com.rtchubs.pharmaerp.ui.splash.SplashFragment
import com.rtchubs.pharmaerp.ui.stock_product.ReceiveProductFragment
import com.rtchubs.pharmaerp.ui.stock_product.StockProductsDetailsFragment
import com.rtchubs.pharmaerp.ui.stock_product.StockProductsFragment
import com.rtchubs.pharmaerp.ui.terms_and_conditions.TermsAndConditionsFragment
import com.rtchubs.pharmaerp.ui.topup.TopUpAmountFragment
import com.rtchubs.pharmaerp.ui.topup.TopUpBankCardFragment
import com.rtchubs.pharmaerp.ui.topup.TopUpMobileFragment
import com.rtchubs.pharmaerp.ui.topup.TopUpPinFragment
import com.rtchubs.pharmaerp.ui.tou.TouFragment
import com.rtchubs.pharmaerp.ui.transactions.TransactionDetailsFragment
import com.rtchubs.pharmaerp.ui.transactions.TransactionsFragment
import com.rtchubs.pharmaerp.ui.video_play.LoadWebViewFragment
import com.rtchubs.pharmaerp.ui.video_play.VideoPlayFragment
import com.rtchubs.pharmaerp.ui.wallet.WalletFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun contributeTermsAndConditionsFragment(): TermsAndConditionsFragment

    @ContributesAndroidInjector
    abstract fun contributeMoreBookListFragment(): MoreBookListFragment

    @ContributesAndroidInjector
    abstract fun contributeMoreShoppingMallFragment(): MoreShoppingMallFragment

    @ContributesAndroidInjector
    abstract fun contributeAllShopListFragment(): AllShopListFragment

    @ContributesAndroidInjector
    abstract fun contributeShopDetailsFragment(): ShopDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeShopDetailsProductListFragment(): ShopDetailsProductListFragment

    @ContributesAndroidInjector
    abstract fun contributeShopDetailsContactUsFragment(): ShopDetailsContactUsFragment

    @ContributesAndroidInjector
    abstract fun contributeProductListFragment(): ProductListFragment

    @ContributesAndroidInjector
    abstract fun contributeProductDetailsFragment(): ProductDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeCartFragment(): CartFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeExamsFragment(): ExamsFragment

    @ContributesAndroidInjector
    abstract fun contributeInfoFragment(): InfoFragment

    @ContributesAndroidInjector
    abstract fun contributeNIDScanCameraXFragment(): NIDScanCameraXFragment

    @ContributesAndroidInjector
    abstract fun contributePreOnBoardingFragment(): PreOnBoardingFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeMoreFragment(): MoreFragment

    @ContributesAndroidInjector
    abstract fun contributeSetAFragment(): SetAFragment

    @ContributesAndroidInjector
    abstract fun contributeSetBFragment(): SetBFragment

    @ContributesAndroidInjector
    abstract fun contributeSetCFragment(): SetCFragment

    @ContributesAndroidInjector
    abstract fun contributeHome2Fragment(): Home2Fragment

    @ContributesAndroidInjector
    abstract fun contributeAddPaymentMethodsFragment(): AddPaymentMethodsFragment


    @ContributesAndroidInjector
    abstract fun contributeHowWorksFragment(): HowWorksFragment

    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeOtpFragment(): OtpFragment

    @ContributesAndroidInjector
    abstract fun contributeTouFragment(): TouFragment

    @ContributesAndroidInjector
    abstract fun contributeSetupFragment(): SetupFragment

    @ContributesAndroidInjector
    abstract fun contributeSetupCompleteFragment(): SetupCompleteFragment

    @ContributesAndroidInjector
    abstract fun contributeProfilesFragment(): ProfilesFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeViewPagerFragment(): ViewPagerFragment

    @ContributesAndroidInjector
    abstract fun contributeChapterListFragment(): ChapterListFragment

    @ContributesAndroidInjector
    abstract fun contributeVideoPlayFragment(): VideoPlayFragment

    @ContributesAndroidInjector
    abstract fun contributeLoadWebViewFragment(): LoadWebViewFragment

    @ContributesAndroidInjector
    abstract fun contributeOtpSignInFragment(): OtpSignInFragment

    @ContributesAndroidInjector
    abstract fun contributePinNumberFragment(): PinNumberFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileSignInFragment(): ProfileSignInFragment

    @ContributesAndroidInjector
    abstract fun contributeAddBankFragment(): AddBankFragment

    @ContributesAndroidInjector
    abstract fun contributeAddCardFragment(): AddCardFragment

    @ContributesAndroidInjector
    abstract fun contributeTopUpMobileFragment(): TopUpMobileFragment

    @ContributesAndroidInjector
    abstract fun contributeTopUpAmountFragment(): TopUpAmountFragment

    @ContributesAndroidInjector
    abstract fun contributeTopUpPinFragment(): TopUpPinFragment

    @ContributesAndroidInjector
    abstract fun contributeTopUpBankCardFragment(): TopUpBankCardFragment

    @ContributesAndroidInjector
    abstract fun contributeAddProductFragment(): AddProductFragment

    @ContributesAndroidInjector
    abstract fun contributeAllProductsFragment(): AllProductsFragment

    @ContributesAndroidInjector
    abstract fun contributeOrderListFragment(): OrderListFragment

    @ContributesAndroidInjector
    abstract fun contributeOrderTrackHistoryFragment(): OrderTrackHistoryFragment

    @ContributesAndroidInjector
    abstract fun contributeAllCustomersFragment(): AllCustomersFragment

    @ContributesAndroidInjector
    abstract fun contributeAddCustomerFragment(): AddCustomerFragment

    @ContributesAndroidInjector
    abstract fun contributeOffersFragment(): OffersFragment

    @ContributesAndroidInjector
    abstract fun contributeMyDevicesFragment(): MyDevicesFragment

    @ContributesAndroidInjector
    abstract fun contributeMyAccountsFragment(): MyAccountsFragment

    @ContributesAndroidInjector
    abstract fun contributeTransactionsFragment(): TransactionsFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateOrderFragment(): CreateOrderFragment

    @ContributesAndroidInjector
    abstract fun contributeSelectCustomerFragment(): SelectCustomerFragment

    @ContributesAndroidInjector
    abstract fun contributeSelectProductFragment(): SelectProductFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateOfferFragment(): CreateOfferFragment

    @ContributesAndroidInjector
    abstract fun contributeTransactionDetailsFragment(): TransactionDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftPointRequestListFragment(): GiftPointRequestListFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftPointRequestListDetailsFragment(): GiftPointRequestListDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeMPOSFragment(): MPOSFragment

    @ContributesAndroidInjector
    abstract fun contributeWalletFragment(): WalletFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftPointHistoryFragment(): GiftPointHistoryFragment

    @ContributesAndroidInjector
    abstract fun contributeGiftPointHistoryDetailsFragment(): GiftPointHistoryDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateMPOSOrderFragment(): CreateMPOSOrderFragment

    @ContributesAndroidInjector
    abstract fun contributeMPOSOrderDetailsFragment(): MPOSOrderDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeStockProductsFragment(): StockProductsFragment

    @ContributesAndroidInjector
    abstract fun contributeStockProductsDetailsFragment(): StockProductsDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeReceiveProductFragment(): ReceiveProductFragment

    @ContributesAndroidInjector
    abstract fun contributeProductPurchaseFragment(): ProductPurchaseFragment

    @ContributesAndroidInjector
    abstract fun contributePrintBarcodeFragment(): PrintBarcodeFragment

    @ContributesAndroidInjector
    abstract fun contributeAttendanceFragment(): AttendanceFragment
}