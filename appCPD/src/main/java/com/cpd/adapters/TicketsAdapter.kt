package com.cpd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cpd.network.models.Tickets
import com.cpd.ufrgsmobile.R
import com.cpd.utils.LayoutUtils
import kotlinx.android.synthetic.main.item_ticket.view.*

/**
 * Created by Theo on 23/06/17.
 */

class TicketsAdapter : RecyclerView.Adapter<TicketsAdapter.TicketsVH>() {

    private val mList = arrayListOf<Tickets>()


    fun updateTicketList(list: List<Tickets>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketsVH(itemView)
    }

    override fun onBindViewHolder(holder: TicketsVH, position: Int) {
        val ticketsVo = mList[position]
        holder.bindView(ticketsVo)
    }

    override fun getItemCount(): Int = mList.size

    class TicketsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(tickets : Tickets){
            val maxBarSize = LayoutUtils.spToPx(248)
            val percent = java.lang.Float.valueOf(tickets.nrrefeicoesresta) / java.lang.Float.valueOf(tickets.nrrefeicoestotal)
            val realSize = (maxBarSize * percent).toInt()

            itemView.token.text = tickets.nrtiquete
            itemView.value.text = "${tickets.nrrefeicoesresta} de ${tickets.nrrefeicoestotal} disponível"
            itemView.real_size.layoutParams.width = realSize
            itemView.real_size.requestLayout()
        }
    }
}

//internal class TicketsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    var token: TextView
//    var value: TextView
//    var backBar: LinearLayout
//    var realBar: View
//
//    init {
//
//        token = itemView.findViewById<View>(R.id.token) as TextView
//        value = itemView.findViewById<View>(R.id.value) as TextView
//        backBar = itemView.findViewById<View>(R.id.max_size) as LinearLayout
//        realBar = itemView.findViewById(R.id.real_size)
//    }
//}
