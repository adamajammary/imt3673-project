package com.imt3673.project.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.imt3673.project.main.R;

/**
 * Helper utilities.
 */
public final class Utils {

    /**
     * Displays the message in a pop-up alert dialog.
     * @param message Message to display
     */
    public static void alertMessage(final String message, final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle(R.string.app_name);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", (d, i) -> d.dismiss());
        dialog.show();
    }

}
