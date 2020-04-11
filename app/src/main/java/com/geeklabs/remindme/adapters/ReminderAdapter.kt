package com.geeklabs.remindme.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.remindme.R
import com.geeklabs.remindme.models.Reminder
import kotlinx.android.synthetic.main.item_reminder.view.*

class ReminderAdapter constructor(private val itemClick: OnItemClickListener) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    var reminderList = mutableListOf<Reminder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(reminderList[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindItems(reminder: Reminder, position: Int) {

            itemView.serialTV.text = "${position + 1}."
            itemView.reminderTV.text = reminder.title
            itemView.descriptionTV.text = reminder.description
            itemView.timeTV.text = reminder.time
            itemView.dateTV.text = reminder.date

            itemView.setOnClickListener {
                itemClick.onItemClick(reminder, itemView, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            reminder: Reminder,
            view: View,
            position: Int
        )
    }

}