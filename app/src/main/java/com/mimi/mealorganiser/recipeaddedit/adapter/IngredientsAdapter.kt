package com.mimi.mealorganiser.recipeaddedit.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mimi.mealorganiser.R
import kotlinx.android.synthetic.main.item_expand_child.view.*
import android.view.MenuInflater
import com.mimi.mealorganiser.utils.ItemTouchHelperAdapter
import java.util.*
import java.util.Collections.swap


/**
 * Created by Mimi on 06/11/2017.
 *
 */

class IngredientsAdapter(private val onAdd: () -> Unit,
                         private val editItem: (String) -> Unit,
                         private val deleteItem: (String) -> Unit)
    : RecyclerView.Adapter<IngredientViewHolder>(){

    private val items = arrayListOf<String>()

    fun refreshItems(newItems: List<String>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size + 1

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = IngredientViewHolder(LayoutInflater.from(parent!!.context).
            inflate(R.layout.item_expand_child,
                    parent, false))

    override fun onBindViewHolder(holder: IngredientViewHolder?, position: Int) {
        if (position >= items.size)
            holder?.bindAddNewItemButton(onAdd)
        else
            holder?.bindText(items[position], editItem, deleteItem)
        holder?.setIsRecyclable(false)
    }

}

class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val label by lazy { itemView.findViewById<TextView>(R.id.label) }
    private val index by lazy { itemView.findViewById<TextView>(R.id.index) }

    fun bindText(value: String, editItem: (String) -> Unit, deleteItem: (String) -> Unit) {
        label.text = value
        label.setTextColor(ContextCompat.getColor(label.context, android.R.color.black))
        index.text = "* "
        index.visibility = View.VISIBLE
        itemView.optionsButton.visibility = View.VISIBLE
        itemView.optionsButton.setOnClickListener {
            showPopupMenu(value, editItem, deleteItem)
        }
    }

    private fun showPopupMenu(value: String, editItem: (String) -> Unit, deleteItem: (String) -> Unit) {
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
                itemView.context.getString(R.string.ingredient))
        label.setTextColor(ContextCompat.getColor(label.context, R.color.colorAccent))
        index.visibility = View.GONE
        itemView.setOnClickListener { addItem() }
        itemView.optionsButton.visibility = View.GONE

    }
}