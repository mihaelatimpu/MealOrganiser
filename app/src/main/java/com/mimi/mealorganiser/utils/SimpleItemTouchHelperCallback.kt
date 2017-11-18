package com.mimi.mealorganiser.utils

import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.RecyclerView


/**
 * Created by Mimi on 07/11/2017.
 * This class is used for the drag and drop mechanism
 */
class SimpleItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter)
    : ItemTouchHelper.Callback() {


    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = true


    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        adapter.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {}
}

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

}
