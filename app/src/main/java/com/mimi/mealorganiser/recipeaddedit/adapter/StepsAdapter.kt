package com.mimi.mealorganiser.recipeaddedit.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mimi.mealorganiser.R
import kotlinx.android.synthetic.main.item_expand_child.view.*
import com.mimi.mealorganiser.pojo.Step
import com.mimi.mealorganiser.utils.ItemTouchHelperAdapter
import java.util.*


/**
 * Created by Mimi on 06/11/2017.
 *
 */

class StepsAdapter(private val onAdd: () -> Unit,
                   private val editItem: (Step) -> Unit,
                   private val deleteItem: (Step) -> Unit,
                   private val onOrderChanged:(ArrayList<Step>)->Unit)
    : RecyclerView.Adapter<ItemViewHolder>(), ItemTouchHelperAdapter{

    private val items = arrayListOf<Step>()

    fun refreshItems(newItems: List<Step>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        onOrderChanged(items)
    }

    override fun getItemCount() = items.size + 1

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = ItemViewHolder(LayoutInflater.from(parent!!.context).
            inflate(R.layout.item_expand_child,
                    parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        if (position >= items.size)
            holder?.bindAddNewItemButton(onAdd)
        else
            holder?.bindText(items[position], editItem, deleteItem)
        holder?.setIsRecyclable(false)
    }

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val label by lazy { itemView.findViewById<TextView>(R.id.label) }
    private val index by lazy { itemView.findViewById<TextView>(R.id.index) }

    fun bindText(step: Step, editItem: (Step) -> Unit, deleteItem: (Step) -> Unit) {
        label.text = step.text
        label.setTextColor(ContextCompat.getColor(label.context, android.R.color.black))
        index.text = itemView.context.getString(R.string.position_index, adapterPosition + 1)
        index.visibility = View.VISIBLE
        itemView.optionsButton.visibility = View.VISIBLE
        itemView.optionsButton.setOnClickListener {
            showPopupMenu(step, editItem, deleteItem)
        }
    }

    private fun showPopupMenu(value: Step, editItem: (Step) -> Unit, deleteItem: (Step) -> Unit) {
        val popup = PopupMenu(itemView.context, itemView.optionsButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_edit_delete, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> editItem(value)
                R.id.delete -> deleteItem(value)
            }
            false
        }
    }

    fun bindAddNewItemButton(addItem: () -> Unit) {
        label.text = itemView.context.getString(R.string.add_item,
                itemView.context.getString(R.string.step))
        label.setTextColor(ContextCompat.getColor(label.context, R.color.colorAccent))
        index.visibility = View.GONE
        itemView.setOnClickListener { addItem() }
        itemView.optionsButton.visibility = View.GONE

    }
}