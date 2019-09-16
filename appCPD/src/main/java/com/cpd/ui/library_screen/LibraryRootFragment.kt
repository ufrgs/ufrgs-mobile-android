/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.ui.library_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.cpd.ufrgsmobile.R
import com.cpd.ui.login_screen.LoginFragment


/**
 * Fragment used to call the library fragments. The
 * view pager requires a fixed fragment, so we
 * change the library fragments inside this one.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
class LibraryRootFragment : Fragment() {

    private val mUfrgsTokenManager = UfrgsTokenManager()
    private val libraryFragment = LibraryFragment()
    private val loginFragment = LoginFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.root_biblio, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureNestedFragments()
    }

    fun configureNestedFragments() {
        if (isAdded) {
            val transaction = childFragmentManager.beginTransaction()
            if (mUfrgsTokenManager.hasToken(activity!!))
                transaction.replace(R.id.root_biblio_layout, libraryFragment)
            else
                transaction.replace(R.id.root_biblio_layout, loginFragment)

            transaction.commit()
        }
    }

    fun autorenewBooks() {
        if (libraryFragment.isAdded) {
            libraryFragment.autorenewAction()
        }
    }

    fun renewBooks() {
        if (libraryFragment.isAdded) {
            libraryFragment.renewAllBooks()
        }
    }

    fun logoff() {
        if (libraryFragment.isAdded) {
            libraryFragment.displayLogoffMessage()
        }
    }

    companion object {
        private val TAG = "LibraryRootFragment"

        fun newInstance(): LibraryRootFragment {
            return LibraryRootFragment()
        }
    }
}
