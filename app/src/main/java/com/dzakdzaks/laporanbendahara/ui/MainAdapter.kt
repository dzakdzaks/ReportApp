package com.dzakdzaks.laporanbendahara.ui

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.google.android.material.card.MaterialCardView
import com.oushangfeng.pinnedsectionitemdecoration.utils.FullSpanUtil

class MainAdapter(
    list: MutableList<Report>?,
    private val onItemClick: (Report) -> Unit
) : BaseMultiItemQuickAdapter<Report, BaseViewHolder>(list) {

    init {
        addItemType(Report.ITEM_HEADER, R.layout.item_header)
        addItemType(Report.ITEM_DATA, R.layout.item_report)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, Report.ITEM_HEADER)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        FullSpanUtil.onViewAttachedToWindow(holder, this, Report.ITEM_HEADER)
    }

    override fun convert(holder: BaseViewHolder, item: Report) {
        when (holder.itemViewType) {
            Report.ITEM_HEADER -> {
                holder.setText(R.id.tvHeader, item.getCreatedAtJustDateReadable())
            }
            else -> {
                setupData(holder, item)
            }
        }
    }

    private fun setupData(holder: BaseViewHolder, data: Report) {
        holder.apply {
            val type = data.type
            setText(R.id.tvType, type)
            setText(
                R.id.tvTitle,
                if (type == Report.INCOME) data.sourceIncome else data.typeExpense
            )
            setText(
                R.id.tvTotal,
                if (type == Report.INCOME) data.getTotalIncomeCurrencyFormat() else data.getTotalExpenseCurrencyFormat()
            )
            getView<MaterialCardView>(R.id.cardReport).setOnClickListener { onItemClick(data) }
        }
    }

    fun getCountBody(): Int {
        return data.filter { it.itemType == Report.ITEM_DATA }.size
    }

}