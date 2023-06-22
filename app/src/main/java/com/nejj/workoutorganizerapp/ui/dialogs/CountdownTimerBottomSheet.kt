package com.nejj.workoutorganizerapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nejj.workoutorganizerapp.R
import com.nejj.workoutorganizerapp.databinding.BottomSheetCountdownTimerBinding

class CountdownTimerBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewBinding: BottomSheetCountdownTimerBinding
    private var pressedNumbers: String = ""
    private var timerText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        viewBinding = BottomSheetCountdownTimerBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    companion object {
        const val TAG = "CountdownTimerBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val numberButtons = arrayOf(
            viewBinding.btnOne,
            viewBinding.btnTwo,
            viewBinding.btnThree,
            viewBinding.btnFour,
            viewBinding.btnFive,
            viewBinding.btnSix,
            viewBinding.btnSeven,
            viewBinding.btnEight,
            viewBinding.btnNine,
            viewBinding.btnZero
        )

        for(button in numberButtons) {
            button.apply { setOnClickListener { appendToTimer() } }
        }

        viewBinding.btnClear.setOnClickListener {
            pressedNumbers = ""
            viewBinding.tvTimer.text = "00:00"
        }

        viewBinding.btnBackspace.setOnClickListener {
            if(pressedNumbers.isNotEmpty())
            {
                pressedNumbers = pressedNumbers.dropLast(1)

                editTimerText()
            }
        }
    }

    private fun Button.appendToTimer() {
        val pressedNumber = text.toString()

        if (pressedNumbers.isEmpty() && pressedNumber == "0") {
            return
        }

        if(pressedNumbers.length >= 4) {
            return
        }

        pressedNumbers += pressedNumber

        editTimerText()
    }

    private fun editTimerText() {
        timerText = pressedNumbers

        while(timerText.length < 4) {
            timerText = timerText.addCharAtIndex('0', 0)
        }

        timerText = timerText.addCharAtIndex(':', 2)

        viewBinding.tvTimer.text = timerText
    }

    private fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()
}