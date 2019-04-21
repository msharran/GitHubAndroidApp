package com.example.sharran.github.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sharran.github.R
import com.example.sharran.github.RepositoryDetailsActivity
import com.example.sharran.github.utils.AppContext
import com.example.sharran.github.utils.RepositoryDetail
import com.example.sharran.github.utils.checkInternetAndExecute
import kotlinx.android.synthetic.main.repository_list_item.view.*

class RepositoryListAdapter(val context: Context , var repositoryList : List<RepositoryDetail>) :
    RecyclerView.Adapter<RepositoryListAdapter.RepositoryListViewHolder>() {

    class RepositoryListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.name
        val description: TextView = itemView.description
        val language: TextView = itemView.language
        val watchers: TextView = itemView.watchers
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_list_item, parent, false)
        return RepositoryListViewHolder(view)
    }

    override fun getItemCount(): Int = repositoryList.size


    override fun onBindViewHolder(holder: RepositoryListViewHolder, position: Int) {
        val repository = repositoryList[position]

        holder.name.text = repository.full_name
        holder.description.text = repository.description ?: ""
        holder.language.text = repository.language
        holder.watchers.text = repository.watchers.toString()
        holder.itemView.setOnClickListener {
           checkInternetAndExecute(context){
               AppContext.repositoryDetail = repository
               val intent = Intent(context, RepositoryDetailsActivity::class.java)
               context.startActivity(intent)
           }
        }
    }

}