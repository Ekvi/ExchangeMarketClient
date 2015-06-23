package com.ekvilan.exchangemarket.view;


import android.app.AlertDialog;
import android.content.Context;


public class DialogProvider {

    public void createDialog(String title, String message, Context context, String button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(button, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
