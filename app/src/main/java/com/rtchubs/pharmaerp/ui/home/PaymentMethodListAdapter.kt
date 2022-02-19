package com.rtchubs.pharmaerp.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.models.SubBook
import com.rtchubs.pharmaerp.databinding.LayoutPaymentMethodRowBinding
import com.rtchubs.pharmaerp.models.PaymentMethod
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class PaymentMethodListAdapter(
    private val appExecutors: AppExecutors,
    private var itemCallback: ((SubBook) -> Unit)? = null

) : DataBoundListAdapter<PaymentMethod, LayoutPaymentMethodRowBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<PaymentMethod>() {
        override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem?.id == newItem?.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: PaymentMethod,
            newItem: PaymentMethod
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    val onClicked = MutableLiveData<PaymentMethod>()
    override fun createBinding(parent: ViewGroup): LayoutPaymentMethodRowBinding {
        val binding: LayoutPaymentMethodRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_payment_method_row, parent, false
        )
        return binding
    }


    override fun bind(binding: LayoutPaymentMethodRowBinding, position: Int) {
        val item = getItem(position)
        binding.model = item
        if (item.id == "-1") {
            binding.tvDefault.visibility = View.GONE
        }
        binding.root.setOnClickListener { v ->
            onClicked.value = item
        }
        // binding.tvCardNumber.text = item.title
        // Glide.with(binding.ivIcon.context).load(R.drawable.plus).into(binding.ivIcon)
    }


}