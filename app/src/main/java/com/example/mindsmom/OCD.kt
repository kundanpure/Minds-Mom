package com.example.mindsmom
import android.view.View
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OCDActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var totalScore = 0
    private lateinit var questions: List<Question>
    private lateinit var moodEmojiImageView: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocd)
        moodEmojiImageView = findViewById(R.id.mood_emoji_image_view)
        progressBar = findViewById(R.id.progress_bar)

        questions = listOf(
            Question(
                "How much time do you spend each day thinking about your obsessions or compulsions?",
                listOf("Less than 1 hour", "1-3 hours", "3-8 hours", "More than 8 hours"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "How much do your obsessions or compulsions interfere with your daily activities, work, school, or social interactions?",
                listOf("Not at all", "Mild interference", "Moderate interference", "Severe interference"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "How much distress do your obsessions or compulsions cause you?",
                listOf("Not at all", "Mild distress", "Moderate distress", "Severe distress"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "Do you actively resist performing your compulsions?",
                listOf("No", "Yes, but with some difficulty", "Yes, with a lot of difficulty", "Yes, unable to resist"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "Do you avoid situations that trigger your obsessions or compulsions?",
                listOf("No", "Yes, but with some difficulty", "Yes, with a lot of difficulty", "Yes, unable to avoid"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "How much control do you have over your obsessions or compulsions?",
                listOf("Complete control", "Moderate control", "Little control", "No control"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "How much time do you spend trying to resist your compulsions?",
                listOf("None", "Less than 1 hour", "1-3 hours", "More than 3 hours"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "Overall, how much do your obsessions or compulsions bother you?",
                listOf("Not at all", "A little", "Moderately", "A lot"),
                listOf(0, 1, 2, 3)
            )
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
        answerChoicesRadioGroup.clearCheck()

        for (choice in currentQuestion.answerChoices) {
            val radioButton = RadioButton(this)
            radioButton.text = choice
            answerChoicesRadioGroup.addView(radioButton)
        }

        val nextButton = findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            handleUserResponse()
        }
    }

    private fun interpretOCDScore(score: Int) {
        val moodEmojiImageView = findViewById<ImageView>(R.id.mood_emoji_image_view)
        val resultTextView = findViewById<TextView>(R.id.result_text_view)

        when {
            score <= 5 -> {
                moodEmojiImageView.setImageResource(R.drawable.happy)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have a low likelihood of OCD."
            }
            score <= 10 -> {
                moodEmojiImageView.setImageResource(R.drawable.natural)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have a moderate likelihood of OCD. Consult a professional for evaluation."
            }
            score <= 15 -> {
                moodEmojiImageView.setImageResource(R.drawable.sad)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have a high likelihood of OCD. Please seek professional help."
            }
            else -> {
                moodEmojiImageView.setImageResource(R.drawable.sad)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "Invalid score. Please ensure the score is within the valid range."
            }
        }
    }

    private fun updateMoodEmoji() {
        val emojiResource = when {
            totalScore <= 5 -> R.drawable.happy
            totalScore <= 10 -> R.drawable.natural
            else -> R.drawable.sad
        }

        moodEmojiImageView.setImageResource(emojiResource)
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
                interpretOCDScore(totalScore)

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
