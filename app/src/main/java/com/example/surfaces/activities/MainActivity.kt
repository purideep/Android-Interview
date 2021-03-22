package com.example.surfaces.activities

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surfaces.R
import com.example.surfaces.adapters.MainListAdapter
import com.example.surfaces.adapters.TabsAdapter
import com.example.surfaces.models.SurfaceModel
import com.example.surfaces.models.TabModel
import com.example.surfaces.presenter.SportsPresenter
import com.example.surfaces.presenter.SportsPresenterDelegate
import com.example.surfaces.util.CommonDialog

class MainActivity : AppCompatActivity(), SportsPresenterDelegate, TabsAdapter.TabSelectListener,
    MainListAdapter.SurfaceSelectListener {

    var mainAdapter: MainListAdapter? = null
    var tabsAdapter: TabsAdapter? = null
    var presenter: SportsPresenter? = null
    var tabsRecyclerView: RecyclerView? = null
    var mainRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFindViewById()
        setupPresenter()
    }

    private fun setupPresenter() {
        presenter = SportsPresenter(this)
        presenter?.load()
    }

    private fun setupFindViewById() {
        tabsRecyclerView = findViewById(R.id.tab_recycler_view)
        mainRecyclerView = findViewById(R.id.main_recycler_view)
    }

    override fun onReadyTabs(tabs: List<TabModel>) {
        tabsAdapter = TabsAdapter(this, tabs, this)
        tabsRecyclerView?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tabsRecyclerView?.adapter = tabsAdapter
    }

    override fun onReadyList(list: List<Any>) {
        mainAdapter = MainListAdapter(this, list, this)
        mainRecyclerView?.layoutManager = LinearLayoutManager(this)
        mainRecyclerView?.adapter = mainAdapter

    }

    override fun onTabSelected(tabModel: TabModel) {
        presenter?.onTabSelected(tabModel)
        tabsAdapter?.list?.forEach { it.isSelected = it.name == tabModel.name }
        tabsAdapter?.notifyDataSetChanged()
    }

    override fun onSurfaceSelected(item: SurfaceModel?, venuName: String?) {
        CommonDialog.show(
            this,
            item?.name ?: "Title",
            venuName ?: "venue",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss();
                presenter?.onPressedYes(this@MainActivity)
            })
    }

    override fun showError(messageId: Int) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
    }
}