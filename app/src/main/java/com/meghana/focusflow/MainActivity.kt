package com.meghana.focusflow

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvSessions: TextView
    private lateinit var tvQuote: TextView

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var sessionCount = 0

    private val quotes = listOf(
        "Stay focused and never quit.",
        "Discipline beats motivation.",
        "Small progress is still progress.",
        "One session at a time.",
        "Consistency creates success."
    )

    private var timeLeftInMillis: Long = 1500000
    private var timerRunning = false

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvGreeting = findViewById(R.id.tvGreeting)
        tvTimer = findViewById(R.id.tvTimer)
        tvSessions = findViewById(R.id.tvSessions)
        tvQuote = findViewById(R.id.tvQuote)

        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)

        updateTimerText()
        setGreeting()

        btnPause.isEnabled = false
        btnReset.isEnabled = false

        btnStart.setOnClickListener {

            if (!timerRunning) {
                startTimer()
            }
        }

        btnPause.setOnClickListener {

            if (timerRunning) {

                countDownTimer?.cancel()

                timerRunning = false

                btnStart.isEnabled = true
                btnPause.isEnabled = false
            }
        }

        btnReset.setOnClickListener {

            countDownTimer?.cancel()

            timeLeftInMillis = 1500000

            updateTimerText()

            timerRunning = false

            btnStart.isEnabled = true
            btnPause.isEnabled = false
            btnReset.isEnabled = false
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

                btnStart.isEnabled = true
                btnPause.isEnabled = false
                btnReset.isEnabled = false

                sessionCount++

                tvSessions.text = "Sessions Completed: $sessionCount"

                tvQuote.text = quotes.random()

                timeLeftInMillis = 1500000

                updateTimerText()
            }
        }.start()

        timerRunning = true

        btnStart.isEnabled = false
        btnPause.isEnabled = true
        btnReset.isEnabled = true
    }

    private fun setGreeting() {

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val greeting = when {

            hour in 5..11 -> "Good Morning ☀️"

            hour in 12..16 -> "Good Afternoon 🌤️"

            hour in 17..21 -> "Good Evening 🌙"

            else -> "Late Night 🌌"
        }

        tvGreeting.text = greeting
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

        tvTimer.text = "⏳ $timeFormatted"
    }
}