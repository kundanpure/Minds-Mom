package com.example.mindsmom

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AnxietyActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var totalScore = 0
    private lateinit var questions: List<Question>
    private lateinit var moodEmojiImageView: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anxiety)
        moodEmojiImageView = findViewById(R.id.mood_emoji_image_view)
        progressBar = findViewById(R.id.progress_bar)
        questions = listOf(
            Question("Feeling nervous, anxious, or on edge", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3)),
            Question("Not being able to stop or control worrying", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3)),
            Question("Worrying too much about different things", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3)),
            Question("Trouble relaxing", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3)),
            Question("Being so restless that it is hard to sit still", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3)),
            Question("Becoming easily annoyed or irritable", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3)),
            Question("Feeling afraid, as if something awful might happen", listOf("Not at all", "Several days", "More than half the days", "Nearly every day"), listOf(0, 1, 2, 3))
        )


        progressBar.max = questions.size * 100

        loadQuestion(currentQuestionIndex)
    }

    private fun loadQuestion(index: Int) {
        val currentQuestion = questions[index]
        val questionTextView = findViewById<TextView>(R.id.question_text_view)
        questionTextView.text = currentQuestion.questionText

        val answerChoicesRadioGroup = findViewById<RadioGroup>(R.id.answer_choices_radio_group)
        answerChoicesRadioGroup.removeAllViews()
        answerChoicesRadioGroup.clearCheck() // Uncheck all radio buttons

        for (choice in currentQuestion.answerChoices) {
            val radioButton = RadioButton(this)
            radioButton.text = choice
            answerChoicesRadioGroup.addView(radioButton)
        }

        val progress = ((index + 1) * 100) / questions.size

        // Update ProgressBar
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        val nextButton = findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            handleUserResponse()
        }
    }

    // Define your own interpretation of the score for anxiety assessment
    private fun interpretAnxietyScore(score: Int) {
        val moodEmojiImageView = findViewById<ImageView>(R.id.mood_emoji_image_view)
        val resultTextView = findViewById<TextView>(R.id.result_text_view)

        when (score) {
            in 0..4 -> {
                // Minimal anxiety symptoms
                moodEmojiImageView.setImageResource(R.drawable.happy)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have minimal anxiety symptoms."
            }
            in 5..9 -> {
                // Mild anxiety
                moodEmojiImageView.setImageResource(R.drawable.natural)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have mild anxiety."
            }
            in 10..14 -> {
                // Moderate anxiety
                moodEmojiImageView.setImageResource(R.drawable.natural)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have moderate anxiety."
            }
            in 15..19 -> {
                // Moderately severe anxiety
                moodEmojiImageView.setImageResource(R.drawable.sad)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have moderately severe anxiety."
            }
            else -> {
                // Severe anxiety
                moodEmojiImageView.setImageResource(R.drawable.sad)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have severe anxiety."
            }
        }
    }


    private fun updateMoodEmoji() {
        val moodEmojiImageView = findViewById<ImageView>(R.id.mood_emoji_image_view)

        val emojiResource = when (totalScore) {
            in 0..4 -> R.drawable.happy // Use your own resource IDs
            in 5..9 -> R.drawable.natural
            else -> R.drawable.sad
        }

        moodEmojiImageView.setImageResource(emojiResource)
        moodEmojiImageView.visibility = View.VISIBLE
    }


    private fun handleUserResponse() {
        val answerChoicesRadioGroup = findViewById<RadioGroup>(R.id.answer_choices_radio_group)
        val selectedRadioButtonId = answerChoicesRadioGroup.checkedRadioButtonId

        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            totalScore += questions[currentQuestionIndex].getScoreForAnswer(selectedRadioButton.text.toString())

            currentQuestionIndex++
            progressBar.progress += 100
            if (currentQuestionIndex < questions.size) {
                loadQuestion(currentQuestionIndex)
            } else {
                // Assessment is complete, calculate final score or show results
                interpretAnxietyScore(totalScore)

                updateMoodEmoji()

                // Disable next button to prevent further interactions
                findViewById<Button>(R.id.next_button).isEnabled = false
            }
        } else {
            // Show a toast if no answer is selected
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show()
        }
    }
}
