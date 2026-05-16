package com.meghana.focusflow

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView

    private lateinit var tvSessions: TextView

    private var sessionCount = 0

    private val quotes = listOf(
        "Stay focused and never quit.",
        "Discipline beats motivation.",
        "Small progress is still progress.",
        "One session at a time.",
        "Consistency creates success."
    )
    private lateinit var btnStart: Button

    private var timeLeftInMillis: Long = 1500000
    private var timerRunning = false

    private var countDownTimer: CountDownTimer? = null
    private lateinit var tvQuote: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        tvSessions = findViewById(R.id.tvSessions)
        tvQuote = findViewById(R.id.tvQuote)
        btnStart = findViewById(R.id.btnStart)

        val btnPause = findViewById<Button>(R.id.btnPause)

        val btnReset = findViewById<Button>(R.id.btnReset)

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
        btnReset.setOnClickListener {

            countDownTimer?.cancel()

            timeLeftInMillis = 1500000
            updateTimerText()

            timerRunning = false
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

                sessionCount++

                tvSessions.text = "Sessions Completed: $sessionCount"

                tvQuote.text = quotes.random()

                timeLeftInMillis = 1500000

                updateTimerText()
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