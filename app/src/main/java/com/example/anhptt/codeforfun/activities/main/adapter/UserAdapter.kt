package com.example.anhptt.codeforfun.activities.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.anhptt.codeforfun.R
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse


class UserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val VIEW_ITEM = 0
        val VIEW_LOADING = 1
    }

    private var userList: MutableList<UserListResponse.User> = mutableListOf()

    fun setItems(items: MutableList<UserListResponse.User>?) {
        if (userList.size > 0) {
            userList.removeAt(userList.size - 1)
            notifyItemRemoved(userList.size)
        }
        userList.addAll(items!!)
        notifyItemInserted(userList.size)
    }

    fun clearItems(){
        userList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
            vh = ItemViewHolder(view)
            return vh
        } else if (viewType == VIEW_LOADING) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            vh = ItemViewHolder(view)
            return vh
        }
        return null!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val user = userList[position]
            holder.name.text = "${user.userId}"
        } else if (holder is LoadingViewHolder) {
            //TODO
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList.size != 0) VIEW_ITEM else VIEW_LOADING
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.item_user)
        lateinit var name: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}