package com.robivan.myweather.view.contentProvider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.robivan.myweather.R

class ContactsAdapter(private var data: List<List<String?>>) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_contacts_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ContactsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val contactNumber: TextView =
            itemView.findViewById(R.id.contacts_number_recycler_view_item_text)
        private val contactName: TextView =
            itemView.findViewById(R.id.contacts_name_recycler_view_item_text)

        fun bind(contact: List<String?>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                contactName.text = contact[0]
                contactNumber.text = contact[1]
                contactNumber.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "on click: ${contact[1]}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }


    }
}