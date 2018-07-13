package com.example.anhptt.codeforfun.activities.main.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.anhptt.codeforfun.R
import com.example.anhptt.codeforfun.activities.data.response.UserDetailResponse
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse


class UserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val VIEW_ITEM = 0
        val VIEW_LOADING = 1
    }

    private var userList: MutableList<UserDetailResponse> = mutableListOf()
    private var actionCallback: ActionCallback? = null
    fun setActionCallback(actionCallback: ActionCallback) {
        this.actionCallback = actionCallback
    }

    private fun getActionCallback(): ActionCallback? {
        return this.actionCallback
    }

    fun setItems(items: MutableList<UserDetailResponse>?) {
        if (userList.size > 0) {
            if (userList[userList.size - 1].userId == null) {
                userList.removeAt(userList.size - 1)
                notifyItemRemoved(userList.size)
            }
        }
        userList.addAll(items!!)
        notifyItemInserted(userList.size)
    }

    fun removeLoading() {
        if (userList.size > 0) {
            if (userList[userList.size - 1].userId == null) {
                userList.removeAt(userList.size - 1)
                notifyItemRemoved(userList.size)
            }
        }
    }

    fun setFooterLoading(item: UserDetailResponse) {
        userList.add(userList.size, item)
        notifyItemInserted(userList.size)
    }

    fun clearItems() {
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
            vh = LoadingViewHolder(view)
            return vh
        }
        return null!!
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val user = userList[position]
            holder.name.text = "${user.businessName} ${user.userId}"
            holder.des.text = "${user.description}"
            holder.itemView.setOnClickListener {
                if (getActionCallback() != null) {
                    getActionCallback()?.onClick(position, user)
                }
            }
        } else if (holder is LoadingViewHolder) {
            //TODO
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList[position].userId == null) VIEW_LOADING else VIEW_ITEM
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
        @BindView(R.id.item_user_des)
        lateinit var des: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    interface ActionCallback {
        fun onClick(position: Int, userDetail: UserDetailResponse)
    }
}