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

class BipolarAssessmentActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var totalScore = 0
    private lateinit var questions: List<Question>
    private lateinit var moodEmojiImageView: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bipolar_assessment)

        moodEmojiImageView = findViewById(R.id.mood_emoji_image_view)
        progressBar = findViewById(R.id.progress_bar)
        questions = listOf(
            Question(
                "Has there ever been a period of time when you were not your usual self and...\n" +
                        "You felt so good or hyper that other people thought you were not your normal self or were so hyper that you got into trouble?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You were so irritable that you shouted at people or started fights or arguments?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You felt much more self-confident than usual?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You got much less sleep than usual and found you didn’t really miss it?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You were much more talkative or spoke much faster than usual?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "Thoughts raced through your head or you couldn’t slow your mind down?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You were so easily distracted by things around you that you had trouble concentrating or staying on track?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You had much more energy than usual?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You were much more social or outgoing than usual, for example, you telephoned friends in the middle of the night?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You were much more interested in sex than usual?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "You did things that were unusual for you or that other people might have thought were excessive, foolish, or risky?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "Spending money got you or your family into trouble?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "If you checked YES to more than one of the above, have several of these ever happened during the same period of time?",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "How much of a problem did any of these cause you?\n" +
                        "Like being unable to work; having family, money or legal troubles; getting into arguments or fights?",
                listOf("NO PROBLEM", "MINOR PROBLEM", "MODERATE PROBLEM", "SERIOUS PROBLEM"),
                listOf(0, 1, 2, 3)
            ),
            Question(
                "Have any of your blood relatives had manic-depressive illness or bipolar disorder?\n" +
                        "i.e. Children, siblings, parents, grandparents, aunts, and uncles.",
                listOf("YES", "NO"),
                listOf(1, 0)
            ),
            Question(
                "Has a health professional ever told you that you have manic-depressive illness or bipolar disorder?",
                listOf("YES", "NO"),
                listOf(1, 0)
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
        answerChoicesRadioGroup.clearCheck() // Uncheck all radio buttons

        for (choice in currentQuestion.answerChoices) {
            val radioButton = RadioButton(this)
            radioButton.text = choice
            answerChoicesRadioGroup.addView(radioButton)
        }

        val progress = ((index + 1) * 100) / questions.size

        // Update ProgressBar
        progressBar.progress = progress

        val nextButton = findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            handleUserResponse()
        }
    }

    private fun interpretBipolarScore(score: Int) {
        val moodEmojiImageView = findViewById<ImageView>(R.id.mood_emoji_image_view)
        val resultTextView = findViewById<TextView>(R.id.result_text_view)

        when {
            score >= 0 && score <= 5 -> {
                moodEmojiImageView.setImageResource(R.drawable.happy)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have a low likelihood of bipolar disorder."
            }
            score >= 6 && score <= 10 -> {
                moodEmojiImageView.setImageResource(R.drawable.natural)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have a moderate likelihood of bipolar disorder. Consult a professional for evaluation."
            }
            score >= 11 && score <= 15 -> {
                moodEmojiImageView.setImageResource(R.drawable.sad)
                moodEmojiImageView.visibility = View.VISIBLE
                resultTextView.text = "You have a high likelihood of bipolar disorder. Please seek professional help."
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
            totalScore <= 4 -> R.drawable.happy
            totalScore <= 9 -> R.drawable.natural
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
            if (currentQuestionIndex < questions.size) {
                loadQuestion(currentQuestionIndex)
            } else {
                // Assessment is complete, calculate final score or show results
                interpretBipolarScore(totalScore)

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
