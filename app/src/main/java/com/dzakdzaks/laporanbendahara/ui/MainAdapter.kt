package com.dzakdzaks.laporanbendahara.ui

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.oushangfeng.pinnedsectionitemdecoration.utils.FullSpanUtil

class MainAdapter(
    list: MutableList<Report>
): BaseMultiItemQuickAdapter<Report, BaseViewHolder>(list) {

    init {
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

            }
            else -> {
                setupData(holder, item)
            }
        }
    }

    private fun setupData(holder: BaseViewHolder, currentData: Report) {
        holder.apply {
            setText(R.id.tvTitle, currentData.createdAt)
        }
    }

}