package com.meghana.focusflow

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvSessions: TextView
    private lateinit var tvQuote: TextView
    private lateinit var tvSessionType: TextView
    private lateinit var btnResetSessions: Button

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private lateinit var spinnerTime: Spinner

    private var sessionCount = 0

    private var isBreakTime = false

    private val quotes = listOf(
        "Stay focused and never quit.",
        "Discipline beats motivation.",
        "Small progress is still progress.",
        "One session at a time.",
        "Consistency creates success."
    )

    private var selectedTimeInMillis: Long = 1500000
    private var timeLeftInMillis: Long = selectedTimeInMillis

    private var timerRunning = false

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvGreeting = findViewById(R.id.tvGreeting)
        tvTimer = findViewById(R.id.tvTimer)
        tvSessions = findViewById(R.id.tvSessions)
        tvQuote = findViewById(R.id.tvQuote)
        tvSessionType = findViewById(R.id.tvSessionType)

        btnStart = findViewById(R.id.btnStart)
        btnPause = findViewById(R.id.btnPause)
        btnReset = findViewById(R.id.btnReset)
        btnResetSessions = findViewById(R.id.btnResetSessions)

        spinnerTime = findViewById(R.id.spinnerTime)

        val timeOptions = arrayOf("25 min", "45 min", "60 min")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            timeOptions
        )

        spinnerTime.adapter = adapter

        updateTimerText()
        setGreeting()

        btnPause.isEnabled = false
        btnReset.isEnabled = false

        btnStart.setOnClickListener {

            selectedTimeInMillis = when (spinnerTime.selectedItem.toString()) {

                "25 min" -> 1500000
                "45 min" -> 2700000
                else -> 3600000
            }

            if (!timerRunning && timeLeftInMillis <= 0) {

                timeLeftInMillis = if (isBreakTime) {
                    300000
                } else {
                    selectedTimeInMillis
                }
            }

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

            isBreakTime = false

            tvSessionType.text = "Focus Session"

            timeLeftInMillis = selectedTimeInMillis

            updateTimerText()

            timerRunning = false

            btnStart.isEnabled = true
            btnPause.isEnabled = false
            btnReset.isEnabled = false
        }
        btnResetSessions.setOnClickListener {

            sessionCount = 0

            tvSessions.text = "Sessions Completed: 0"
        }
    }

    private fun startTimer() {

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                timeLeftInMillis = millisUntilFinished

                updateTimerText()
            }

            override fun onFinish() {

                MediaPlayer.create(this@MainActivity, R.raw.bell).start()

                timerRunning = false

                btnStart.isEnabled = true
                btnPause.isEnabled = false
                btnReset.isEnabled = false

                if (!isBreakTime) {

                    sessionCount++

                    tvSessions.text = "Sessions Completed: $sessionCount"

                    tvQuote.text = quotes.random()

                    isBreakTime = true

                    tvSessionType.text = "Break Time ☕"

                    timeLeftInMillis = 300000

                } else {

                    isBreakTime = false

                    tvSessionType.text = "Focus Session"

                    timeLeftInMillis = selectedTimeInMillis
                }

                updateTimerText()

                startTimer()
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