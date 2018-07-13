package com.example.anhptt.codeforfun.activities.main.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.example.anhptt.codeforfun.R
import com.example.anhptt.codeforfun.activities.data.response.UserDetailResponse
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.detail.MainActivityDetail
import com.example.anhptt.codeforfun.activities.main.adapter.UserAdapter
import com.example.anhptt.codeforfun.activities.main.presenter.MainControllerContract
import com.example.anhptt.codeforfun.activities.main.presenter.MainControllerPresenter

class MainController : Controller(), MainControllerContract.View, UserAdapter.ActionCallback {

    @BindView(R.id.refreshData)
    lateinit var refreshDataView: SwipeRefreshLayout
    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    private lateinit var presenter: MainControllerContract.Presenter
    private lateinit var context: Context
    private lateinit var mUserAdapter: UserAdapter
    private var mVisibleItemCount = 0
    private var mFirstVisibleItem = 0
    private var mTotalItemCount = 0
    private var page = 1
    private var isFinalPage = false
    private var itemList: MutableList<UserDetailResponse> = mutableListOf()
    private var layoutManager: LinearLayoutManager? = null
    private var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main, container, false)
        this.context = inflater.context
        ButterKnife.bind(this, view)
        presenter = MainControllerPresenter(this.context, this)
        mUserAdapter = UserAdapter()
        layoutManager = LinearLayoutManager(context, 1, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mUserAdapter
        loadMore()
        presenter.getUserList(page)
        refreshDataView.setOnRefreshListener {
            page = 1
            mUserAdapter.clearItems()
            presenter.getUserList(page)
        }
        mUserAdapter.setActionCallback(this)
        return view
    }

    override fun onClick(position: Int, userDetail: UserDetailResponse) {
        val intent = Intent(context, MainActivityDetail::class.java)
        val bundle = Bundle()
        bundle.putSerializable("BUNDLE_EXTRA", userDetail)
        intent.putExtra("DATA_EXTRA", bundle)
        startActivity(intent)
    }

    private fun loadMore() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mTotalItemCount = layoutManager!!.itemCount
                mVisibleItemCount = layoutManager!!.childCount
                mFirstVisibleItem = layoutManager!!.findFirstVisibleItemPosition()
                if (!isLoading && mTotalItemCount <= (mFirstVisibleItem + mVisibleItemCount)) {
                    recyclerView!!.post {
                        mUserAdapter.setFooterLoading(UserDetailResponse())
                    }
                    page += 1
                    isLoading = true
                    presenter.getUserList(page)
                }
            }
        })
    }

    override fun setPresenter(presenter: MainControllerContract.Presenter): MainControllerContract.Presenter {
        return this.presenter
    }


    override fun showUserListOnSuccess(listItem: MutableList<UserDetailResponse>, page: Int, finalPage: Boolean) {
        this.page = page
        this.isLoading = finalPage
        this.itemList = listItem
        if (listItem.size > 0) {
            mUserAdapter.setItems(this.itemList)
        }
    }

    override fun showUserListOnError(throwable: Throwable) {
    }

    override fun emptyList() {
        mUserAdapter.removeLoading()
    }

    override fun hideLoading() {
        refreshDataView.isRefreshing = false
    }

    override fun showLoading() {

    }
}