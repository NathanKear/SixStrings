package nk.sixstrings.ui.play

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PlaySpeedPickerDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val options = listOf("One", "Two", "Three").toTypedArray();

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Pick One")
                .setItems(
                    options
                ) { dialog, which ->
                    Log.i("Click", options[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}