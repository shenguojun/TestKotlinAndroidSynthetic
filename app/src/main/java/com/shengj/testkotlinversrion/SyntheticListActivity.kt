package com.shengj.testkotlinversrion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_synthetic_list.*

class SyntheticListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synthetic_list)

        // Access the synthetic view before recyclerview initialize
        item_text

        val adapter = CustomAdapter()
        item_list.adapter =adapter

        // Access the synthetic view after recyclerview initialize and change the text color
        // In kotlin 1.4.x because of the view cache, item_text is still the outer text view
        // But in kotlin 1.5.x without view cache, here actually using findViewById and get the first item in recyclerView
        item_list.post {
            item_text.setTextColor(resources.getColor(android.R.color.holo_purple))
        }
    }


    class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.item_text)

        }
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_list_item, viewGroup, false)
            return ViewHolder(view)
        }
        override fun getItemCount() = 3
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = "Inner Layer Text $position"
        }

    }

}