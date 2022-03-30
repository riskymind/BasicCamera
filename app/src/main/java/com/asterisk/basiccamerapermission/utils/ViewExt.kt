package com.asterisk.basiccamerapermission.utils

import android.view.View
import com.asterisk.basiccamerapermission.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(msgId: Int, length: Int) {
    showSnackBar(context.getString(msgId), length)
}

fun View.showSnackBar(msg: String, length: Int) {
    showSnackBar(msg, length, null) {}
}

fun View.showSnackBar(
    msgId: Int,
    length: Int,
    actionMessageId: Int,
    action: (View) -> Unit,
) {
    showSnackBar(context.getString(msgId), length, context.getString(actionMessageId), action)
}


fun View.showSnackBar(
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit,
) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }
    }

    snackbar.show()
}

fun View.snackbar(message: Int, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        action?.let { setAction(context.getString(R.string.ok)) { action.invoke() } }
    }


    snackbar.show()
}