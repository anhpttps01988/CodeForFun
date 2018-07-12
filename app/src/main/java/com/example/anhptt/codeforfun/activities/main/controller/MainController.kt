package com.example.anhptt.codeforfun.activities.main.controller

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import com.bluelinelabs.conductor.Controller
import com.example.anhptt.codeforfun.R
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.main.adapter.UserAdapter
import com.example.anhptt.codeforfun.activities.main.presenter.MainControllerContract
import com.example.anhptt.codeforfun.activities.main.presenter.MainControllerPresenter

class MainController : Controller(), MainControllerContract.View {

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
    private var page = 0
    private var isFinalPage = false
    private var itemList: MutableList<UserListResponse.User> = mutableListOf()
    private var layoutManager: LinearLayoutManager? = null
    private var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main, container, false)
        this.context = inflater.context
        ButterKnife.bind(this, view)
        presenter = MainControllerPresenter(this.context, this)
        mUserAdapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context, 1, false)
        recyclerView.adapter = mUserAdapter
        presenter.getUserList(1, false)
        refreshDataView.setOnRefreshListener {
            mUserAdapter.clearItems()
            presenter.getUserList(1, false)
        }
        return view
    }

    private fun loadMore(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mTotalItemCount = layoutManager!!.itemCount
                mVisibleItemCount = layoutManager!!.childCount
                mFirstVisibleItem = layoutManager!!.findFirstVisibleItemPosition()
                if (!isLoading && mTotalItemCount <= (mFirstVisibleItem + mVisibleItemCount)) {
//                    itemList.add(itemList.size, UserListResponse.User())
//                    page += 1
//                    isLoading = true
                }
            }
        })
    }

    override fun setPresenter(presenter: MainControllerContract.Presenter): MainControllerContract.Presenter {
        return this.presenter
    }

    override fun showUserListOnSuccess(listItem: MutableList<UserListResponse.User>, page: Int, finalPage: Boolean) {
        this.page = page
        this.isFinalPage = finalPage
        this.itemList = listItem
        if (listItem.size > 0) {
            mUserAdapter.setItems(this.itemList)
        }
    }

    override fun showUserListOnError(throwable: Throwable) {
    }

    override fun hideLoading() {
        refreshDataView.isRefreshing = false
    }

    override fun showLoading() {

    }
}