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
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.cpd.ufrgsmobile.R;

/**
 * @author Alan Wink
 */
public class LibraryAutoRenewDialogFragment extends DialogFragment {

    private RenewDialogResponse mCallback;

    public void setRenewDialogResponse(RenewDialogResponse callback){
        mCallback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.auto_renew);
        builder.setMessage(getContext().getString(R.string.auto_renew_activate_warning_message));

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mCallback != null)
                    mCallback.onAutoRenewAccepted();
            }
        });

        builder.setNegativeButton(R.string.not_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mCallback != null)
                    mCallback.onAutoRenewDenied();
            }
        });

        return builder.create();
    }

    public interface RenewDialogResponse{
        void onAutoRenewAccepted();
        void onAutoRenewDenied();
    }
}
