package nk.sixstrings.ui.play

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class PlayDifficultyPickerDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val options = listOf("Easy", "Medium", "Hard").toTypedArray();

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Pick a difficulty")
                .setItems(
                    options
                ) { _, which ->

                    val bundle = Bundle()
                    bundle.putString(SELECTED_PLAY_DIFFICULTY, options[which])

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

    companion object {
        const val SELECTED_PLAY_DIFFICULTY = "SELECTED_PLAY_DIFFICULTY"
    }
}