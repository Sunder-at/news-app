package com.sunder.fct;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

public class ErrorAlertDialog {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Dialog mDialog;

    public ErrorAlertDialog(Context baseContext) {
        builder = new AlertDialog.Builder(baseContext);
        builder.setCancelable(false).setTitle("Greška").setMessage("Ups, došlo je do pogreške");
        builder.setPositiveButton("U REDU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
    }

    public void show(){
        alertDialog.show();
    }

    public boolean isShowing(){
        return alertDialog.isShowing();
    }

    public interface Dialog{
        void dismiss();
    }
}
