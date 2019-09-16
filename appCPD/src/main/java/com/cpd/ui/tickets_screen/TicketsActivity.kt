package com.cpd.ui.tickets_screen

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.cpd.adapters.TicketsAdapter
import com.cpd.network.models.Tickets
import com.cpd.ufrgsmobile.R
import com.cpd.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_tickets.*

/**
 * Created by Theo on 23/06/17.
 */

class TicketsActivity : AppCompatActivity(), TicketsContract.View {

    private var mAdapter: TicketsAdapter? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)
        LayoutUtils.setStatusBarColor(this, "#7d2623")

        mAdapter = TicketsAdapter()
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = mAdapter


        TicketsPresenter(this, this).getTickets()

        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.setMessage(getString(R.string.loading))
        mProgressDialog!!.show()

        background.setOnClickListener { onBackPressed() }

    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.my_fade_in, R.anim.my_fade_out)
    }

    override fun showTickets(tickets: List<Tickets>) {
        try {
            mProgressDialog!!.dismiss()
            if (tickets.isNotEmpty()) {
                mAdapter!!.updateTicketList(tickets)
                no_tickets.visibility = View.GONE
            } else {
                no_tickets.visibility = View.VISIBLE
            }

            background.requestLayout()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showMessage(msg: String) {
        try {
            if (mProgressDialog!!.isShowing)
                mProgressDialog!!.dismiss()
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
