package com.nejj.workoutorganizerapp.ui.dialogs

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmationDialogMaker {
    companion object {
        fun showDeleteConfirmationDialog(
            context: Context,
        ) : Boolean {
            var answer = false
            MaterialAlertDialogBuilder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this exercise?")
                .setNegativeButton("Cancel") { dialog, which ->
                    answer = false
                }
                .setPositiveButton("Delete") { dialog, which ->
                    answer = true
                }
                .show()

            return answer
        }
    }
}