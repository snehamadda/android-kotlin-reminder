package com.geeklabs.remindme.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeklabs.remindme.R
import com.geeklabs.remindme.models.Reminder
import com.geeklabs.remindme.utils.Util
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
            itemView.descriptionTV.text = reminder.description

            val reminderDate =
                Util.getFormattedDate(reminder.date + " " + reminder.time, "dd/MM/YYYY HH:mm")
            if (reminderDate.time < System.currentTimeMillis()) {
                // normal text
                itemView.reminderTV.text = reminder.title
            } else {
                // Set strike off text
                itemView.reminderTV.text = reminder.title
                itemView.serialTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemView.reminderTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemView.descriptionTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemView.timeTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemView.dateTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            itemView.timeTV.text = reminder.time
            itemView.dateTV.text = reminder.date

            itemView.more.setOnClickListener {
                itemClick.onItemClick(reminder, itemView.more, adapterPosition)
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