package nk.sixstrings.ui.play

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class PlaySpeedPickerDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val options = listOf("Slow", "Medium", "Fast").toTypedArray();

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Pick a speed")
                .setItems(
                    options
                ) { _, which ->

                    val bundle = Bundle()
                    bundle.putString(SELECTED_PLAY_SPEED, options[which])

                    val intent = Intent().putExtras(bundle)

                    targetFragment!!.onActivityResult(
                        targetRequestCode,
                        Activity.RESULT_OK,
                        intent
                    )

                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }


    companion object {
        const val SELECTED_PLAY_SPEED = "SELECTED_PLAY_SPEED"
    }
}