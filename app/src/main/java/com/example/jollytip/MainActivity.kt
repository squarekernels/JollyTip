package com.example.jollytip

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBar)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(
                p0: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress %"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }

        // Get value of base and tip percent
        // Compute the tip and total
        // Update UI to show values
        val baseAmount = etBaseAmount.text.toString().toDoubleOrNull() ?: 0.0
        val tipPercent = seekBarTip.progress

        // Calculate the tip and total amounts
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        tvTipAmount.text = String.format("%.2f", tipAmount)
        tvTotalAmount.text = String.format("%.2f", totalAmount)
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "\uD83D\uDE2D"
            in 10..15 -> "\uD83D\uDE42"
            in 16..19 -> "☺\uFE0F☺\uFE0F"
            in 20..24 -> "\uD83D\uDE04\uD83D\uDE04"
            else -> "\uD83E\uDD29\uD83E\uDD29\uD83E\uDD29"
        }

        tvTipDescription.text = tipDescription
        // Update color based on tip percentage
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.primary_color),
            ContextCompat.getColor(this, R.color.accent_color)
        ) as Int
        tvTipDescription.setTextColor(color)
//        constraintLayout.setBackgroundColor(color)
    }
}