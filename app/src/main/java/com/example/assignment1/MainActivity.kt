package com.example.assignment1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.airbnb.lottie.LottieAnimationView

class MainActivity : ComponentActivity() {

    private var score: Int = 0
    private var wallHold: Int = 0
    private var hasFallen: Boolean = false
    private lateinit var scoreTextView: TextView
    private lateinit var fireworksAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.scoreTextView)
        fireworksAnimationView = findViewById(R.id.fireworksAnimationView)
        val climbButton: Button = findViewById(R.id.climbButton)
        val fallButton: Button = findViewById(R.id.fallButton)
        val resetButton: Button = findViewById(R.id.resetButton)

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score", 0)
            wallHold = savedInstanceState.getInt("wallHold", 0)
            hasFallen = savedInstanceState.getBoolean("hasFallen", false)
            updateScore()
        }

        climbButton.setOnClickListener {
            if (!hasFallen && wallHold < 9) {
                wallHold += 1
                when (wallHold) {
                    in 1..3 -> score += 1  // Blue zone
                    in 4..6 -> score += 2  // Green zone
                    in 7..9 -> score += 3  // Red zone
                }
                score = score.coerceAtMost(18)  // Max score 18
                updateScore()
                triggerFireworks()
            }
            Log.d("MainActivity", "Climb clicked, wallHold: $wallHold, score: $score")
        }

        fallButton.setOnClickListener {
            if (wallHold in 1..8 && !hasFallen) {
                score = (score - 3).coerceAtLeast(0)
                wallHold = 0
                hasFallen = true
                updateScore()
            }
            Log.d("MainActivity", "Fall clicked, wallHold: $wallHold, score: $score")
        }

        resetButton.setOnClickListener {
            score = 0
            wallHold = 0
            hasFallen = false
            fireworksAnimationView.visibility = View.GONE
            fireworksAnimationView.cancelAnimation()
            updateScore()
            Log.d("MainActivity", "Reset clicked, wallHold: $wallHold, score: $score")
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateScore() {
        scoreTextView.text = "Score: $score"
        when (wallHold) {
            in 1..3 -> scoreTextView.setTextColor(getColor(R.color.blue))
            in 4..6 -> scoreTextView.setTextColor(getColor(R.color.green))
            in 7..9 -> scoreTextView.setTextColor(getColor(R.color.red))
        }
    }

    private fun triggerFireworks() {
        if (score > 0) {
            when (wallHold) {
                in 1..3 -> fireworksAnimationView.setAnimation(R.raw.blue_fireworks)
                in 4..6 -> fireworksAnimationView.setAnimation(R.raw.green_fireworks)
                in 7..9 -> fireworksAnimationView.setAnimation(R.raw.red_fireworks)
            }
            fireworksAnimationView.visibility = View.VISIBLE
            fireworksAnimationView.playAnimation()
        } else {
            fireworksAnimationView.visibility = View.GONE
            fireworksAnimationView.cancelAnimation()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("score", score)
        outState.putInt("wallHold", wallHold)
        outState.putBoolean("hasFallen", hasFallen)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score")
        wallHold = savedInstanceState.getInt("wallHold")
        hasFallen = savedInstanceState.getBoolean("hasFallen")
        updateScore()
    }
}
