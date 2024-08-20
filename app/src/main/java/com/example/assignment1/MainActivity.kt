package com.example.assignment1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var score: Int = 0
    private var wallHold: Int = 0
    private var hasFallen: Boolean = false // Flag to track if the user has fallen
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout depending on the orientation (landscape or portrait)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.scoreTextView)
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
            // If the user has fallen, prevent them from climbing until reset
            if (!hasFallen && wallHold < 9) {
                wallHold += 1
                when (wallHold) {
                    in 1..3 -> score += 1  // Blue zone
                    in 4..6 -> score += 2  // Green zone
                    in 7..9 -> score += 3  // Red zone
                }
                score = score.coerceAtMost(18)  // Max score 18
                updateScore()
            } else if (hasFallen) {
                Log.d("MainActivity", "Climb blocked due to fall. Reset required.")
            }
            Log.d("MainActivity", "Climb clicked, wallHold: $wallHold, score: $score")
        }

        fallButton.setOnClickListener {
            if (wallHold in 1..8 && !hasFallen) {  // Only allow fall if between holds 1-8 and user hasn't fallen yet
                score = (score - 3).coerceAtLeast(0)  // No negative score
                wallHold = 0  // Reset wallHold after fall
                hasFallen = true  // User has fallen, so they can't climb further
                updateScore()
            }
            Log.d("MainActivity", "Fall clicked, wallHold: $wallHold, score: $score")
        }

        resetButton.setOnClickListener {
            score = 0
            wallHold = 0
            hasFallen = false  // Reset the fall status
            scoreTextView.setTextColor(getColor(R.color.black))
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

    // Save the state when rotating the device
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("score", score)
        outState.putInt("wallHold", wallHold)
        outState.putBoolean("hasFallen", hasFallen) // Save fall status
        super.onSaveInstanceState(outState)
        Log.d("MainActivity", "State saved")
    }

    // Restore the state after rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score")
        wallHold = savedInstanceState.getInt("wallHold")
        hasFallen = savedInstanceState.getBoolean("hasFallen")
        updateScore()
        Log.d("MainActivity", "State restored, wallHold: $wallHold, score: $score")
    }
}
