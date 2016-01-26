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
package com.cpd.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpd.adapters.LibraryAdapter;
import com.cpd.network.LibraryLoader;
import com.cpd.network.parsers.LibraryParser;
import com.cpd.ufrgsmobile.R;
import com.cpd.vos.LibraryUserVo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Fragment that shows user information and books.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class LibraryFragment extends Fragment {

    private android.support.v4.app.FragmentManager fragmentManager;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	private android.support.design.widget.FloatingActionButton mRenewFab;

    private TextView mUsername;
	private TextView mUserExtraInfo;

    private LibraryAdapter mAdapter;
    private LibraryLoader mLibraryLoader;

	private RelativeLayout mEmptyLayout;

	private RecyclerView mLibraryRecycler;

	private LibraryUserVo mUserVo;

	public void setUserInfo(LibraryUserVo user){
		this.mUserVo = user;
	}

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_library, container, false);


		// Load Views
        mUsername = (TextView) view.findViewById(R.id.library_fragment_user_info_name);
		mUsername.setText(mUserVo.name);
	    mUserExtraInfo = (TextView) view.findViewById(R.id.library_fragment_user_info_extra_info);
		mUserExtraInfo.setText(generateExtraInfo(mUserVo));
		mRenewFab = (android.support.design.widget.FloatingActionButton) view.findViewById(R.id.library_fragment_fab);
		mRenewFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				renewBooks();
			}
		});
		mEmptyLayout = (RelativeLayout) view.findViewById(R.id.library_fragment_books_layout);

        // Load adapter
		mLibraryRecycler = (RecyclerView) view.findViewById(R.id.library_fragment_books_recycler);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mLibraryRecycler.setLayoutManager(llm);

        mAdapter = new LibraryAdapter(getContext(), mUserVo);
        mLibraryRecycler.setAdapter(mAdapter);

	    // Get library info
        mLibraryLoader = new LibraryLoader(getContext());

        setRetainInstance(true);

		if(mUserVo.loans == 0){
			mRenewFab.setVisibility(View.GONE);
			mEmptyLayout.setVisibility(View.VISIBLE);
			mLibraryRecycler.setVisibility(View.INVISIBLE);
		}

	    return view;
	}

	private void renewBooks() {
		final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.library_fragment_recycler), R.string.renewing_items, Snackbar.LENGTH_INDEFINITE);
		TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.WHITE);
		snackbar.show();

		mLibraryLoader.renewBooks(new LibraryParser.LibraryRenewReady() {
			@Override
			public void onLibraryRenewReady(String response) {
				snackbar.dismiss();
				showDialog(getActivity().getString(R.string.renew), response);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLibraryRenewError(Exception exception) {
				snackbar.dismiss();
				showDialog(getActivity().getString(R.string.error), getActivity().getString(R.string.not_possible_to_renew_books_message));
			}
		}, true);
	}

	/**
     * The layout has one TextView to show all user information. We need to format user
     * information to one string.
     */
    private String generateExtraInfo(LibraryUserVo user) {
        NumberFormat formatter = new DecimalFormat("#0.00");

		String loanString;
		String reservationString;
		String debtString;

		if(user.loans == 0){
			loanString = getActivity().getString(R.string.no_loan);
		}
		else{
			loanString = user.loans + " " + (user.loans != 1 ? getActivity().getString(R.string.loans_word) : getActivity().getString(R.string.loan_word));
		}


		if(user.reservations == 0){
			reservationString = getActivity().getString(R.string.no_reservation);
		}
		else {
			reservationString = user.reservations + " " + (user.reservations != 1 ? getActivity().getString(R.string.reservations_word) : getActivity().getString(R.string.reservation_word));
		}

		if(user.debt == 0){
			debtString = getActivity().getString(R.string.no_debt);
		}
		else {
			debtString = getActivity().getString(R.string.reais_currency) + formatter.format(user.debt) + " "  + getActivity().getString(R.string.debt_word);
		}
        return  loanString + "\n" +
				reservationString + "\n" +
				debtString;
	}

	private void showDialog(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message).setTitle(title);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();

	}

}
