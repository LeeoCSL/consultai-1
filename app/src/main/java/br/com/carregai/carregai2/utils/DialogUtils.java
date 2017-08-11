package br.com.carregai.carregai2.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import br.com.carregai.carregai2.R;

public class DialogUtils extends AlertDialog {
    private static ProgressDialog mProgressDialog;

    protected DialogUtils(@NonNull Context context) {
        super(context);
    }

    public static void loadingDialog(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Por favor, aguarde.");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public static void hideLoadingDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}