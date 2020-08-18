package com.example.stormy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {

    final Context mContext;

    public AlertDialogFragment(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.error_title))
                .setMessage(mContext.getString(R.string.error_msg))
                .setPositiveButton(mContext.getString(R.string.error_button), null);

        return builder.create();
    }
}
