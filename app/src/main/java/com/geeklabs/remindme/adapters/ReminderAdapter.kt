package com.geeklabs.remindme.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.remindme.R
import com.geeklabs.remindme.models.Reminder
import kotlinx.android.synthetic.main.item_reminder.view.*

class ReminderAdapter(var reminderList: MutableList<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {


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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(reminder: Reminder, position: Int) {
            itemView.serialTV.text = "${position + 1}."
            itemView.reminderTV.text = reminder.title
            itemView.descriptionTV.text = reminder.description
            itemView.timeTV.text = reminder.time
            itemView.dateTV.text = reminder.date
        }
    }

}