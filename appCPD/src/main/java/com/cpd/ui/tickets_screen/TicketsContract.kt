package com.cpd.ui.tickets_screen

import com.cpd.network.models.Tickets

/**
 * Created by Theo on 08/09/17.
 */

interface TicketsContract {
    interface View {
        fun showTickets(tickets: List<Tickets>)
        fun showMessage(msg: String)
    }

    interface Presenter {
        fun getTickets()
    }
}
