package com.meghana.focusflow

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button

    private var timeLeftInMillis: Long = 1500000
    private var timerRunning = false

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)

        val btnPause = findViewById<Button>(R.id.btnPause)

        updateTimerText()

        btnStart.setOnClickListener {

            if (!timerRunning) {
                startTimer()
            }
        }

        btnPause.setOnClickListener {

            if (timerRunning) {

                countDownTimer.cancel()
                timerRunning = false
            }
        }
    }

    private fun startTimer() {

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {

                timerRunning = false
            }
        }.start()

        timerRunning = true
    }

    private fun updateTimerText() {

        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            minutes,
            seconds
        )

        tvTimer.text = timeFormatted
    }
}