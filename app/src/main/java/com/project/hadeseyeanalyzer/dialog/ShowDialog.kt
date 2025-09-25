package com.project.hadeseyeanalyzer.dialog;

import android.content.Context
import androidx.core.graphics.green
import com.developer.kalert.KAlertDialog
import com.project.hadeseyeanalyzer.R

class ShowDialog(private val context: Context) {

    var isTrue = false

    fun invalidDialog(title: String, message: String) {
        KAlertDialog(context, KAlertDialog.ERROR_TYPE, isTrue)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmText("OK")
            .confirmButtonColor(R.color.red)
            .setConfirmClickListener { dialog ->
                dialog.dismissWithAnimation()
            }
            .show()
    }

    fun successDialog(title: String, message: String) {
        KAlertDialog(context, KAlertDialog.SUCCESS_TYPE, isTrue)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmText("OK")
            .confirmButtonColor(R.color.green)
            .setConfirmClickListener { dialog ->
                dialog.dismissWithAnimation()
            }
            .show()
    }

    fun successDialog(title: String, message: String, button: String, onConfirm: Runnable?) {
        KAlertDialog(context, KAlertDialog.SUCCESS_TYPE, isTrue)
            .setTitleText(title)
            .setContentText(message)
            .setConfirmText(button)
            .confirmButtonColor(R.color.green)
            .setConfirmClickListener { dialog ->
                dialog.dismissWithAnimation()
                onConfirm?.run()
            }
            .show()
    }

}