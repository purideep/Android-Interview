package com.example.surfaces.util

import android.R
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


object CommonDialog {

    fun show(
        context: Context,
        title: String,
        message: String,
        callback: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                R.string.yes,
                callback
            ) // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(R.string.no, null)
            .setIcon(R.drawable.ic_dialog_alert)
            .show()
    }
}