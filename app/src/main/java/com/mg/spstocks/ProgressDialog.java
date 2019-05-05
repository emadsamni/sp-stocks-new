package com.mg.spstocks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;


public class ProgressDialog {
    private static ProgressDialog instance;
    private Dialog dialog;

    private ProgressDialog() {}

    public static ProgressDialog getInstance() {
        if (instance == null) {
            instance = new ProgressDialog();
        }
        return instance;
    }



    public void show(Context context) {
        cancel();

        dialog = new Dialog(context, R.style.myDialog);
        dialog.setContentView(R.layout.layout_progress_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
    public void cancel() {
        if (isShowing()) {
            dialog.cancel();
        }
    }

    public boolean isShowing() {
        if (dialog == null) {
            return false;
        }

        Context context = dialog.getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity)context;
            if(activity.isFinishing() || activity.isDestroyed()) {
                return false;
            }
        }

        return dialog.isShowing();
    }







}
