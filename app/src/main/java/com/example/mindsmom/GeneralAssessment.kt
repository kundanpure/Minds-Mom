package com.example.mindsmom

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GeneralAssessmentActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var totalScore = 0
    private lateinit var questions: List<Question>
    private lateinit var moodEmojiImageView: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_assessment)
        moodEmojiImageView = findViewById(R.id.mood_emoji_image_view)
        progressBar = findViewById(R.id.progress_bar)
        questions = listOf(

                Question("Have you been feeling this way for an extended period of time, or is it something that started recently?", listOf("Extended period", "Started recently"), listOf(0, 1)),
        Question("Can you identify any specific triggers or events that may have led to your current feelings?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Have you experienced similar feelings in the past, and if so, how frequently?", listOf("Frequently", "Occasionally", "Rarely", "Never"), listOf(0, 1, 2, 3)),
        Question("Are there any particular times of the day or situations where you feel better or worse?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Have there been any major changes or stressors in your life recently, such as work-related issues, relationship challenges, or personal losses?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Do you find it difficult to experience joy or interest in things that you used to enjoy?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Have you noticed changes in your sleep patterns, appetite, or energy levels?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Are you able to pinpoint any specific thoughts or concerns that are particularly troubling to you?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Do you find it challenging to concentrate or make decisions, even about simple things?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Have you had thoughts of self-harm or suicide, even if you don't have specific plans to act on them?", listOf("Yes", "No"), listOf(0, 1)),
        Question("Have you sought support or talked to someone about these feelings, such as a friend, family member, or professional?", listOf("Yes", "No"), listOf(0, 1)),
        Question("On a scale from 1 to 10, how would you rate the impact of these feelings on your daily functioning and overall well-being?", listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))

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

    // Define your own interpretation of the score for the general assessment
    private fun interpretGeneralScore(score: Int) {
        val interpretationTextView = findViewById<TextView>(R.id.result_text_view)

        when {
            score >= 0 && score <= 5 -> {
                interpretationTextView.text = "Mild distress. It seems like you might be experiencing a temporary low mood. Consider talking to someone for support."
            }
            score >= 6 && score <= 10 -> {
                interpretationTextView.text = "Moderate distress. It's important to reach out to someone you trust and consider seeking professional help."
            }
            score >= 11 && score <= 15 -> {
                interpretationTextView.text = "Significant distress. You should talk to a mental health professional as soon as possible for support and guidance."
            }
            score >= 16 && score <= 20 -> {
                interpretationTextView.text = "Severe distress. It's critical to seek immediate help from a mental health professional or a crisis hotline."
            }
            else -> {
                interpretationTextView.text = "Invalid score. Please ensure the score is within the valid range."
            }
        }
    }



    // Define your own update of the mood emoji for the general assessment
    private fun updateMoodEmoji() {
        val moodEmoji = when {
            totalScore >= 0 && totalScore <= 5 -> R.drawable.happy
            totalScore >= 6 && totalScore <= 10 -> R.drawable.sad
            totalScore >= 11 && totalScore <= 15 -> R.drawable.sad
            totalScore >= 16 && totalScore <= 20 -> R.drawable.sad
            else -> R.drawable.sad // Provide a default emoji if score is out of range
        }
        moodEmojiImageView.setImageResource(moodEmoji)
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
                interpretGeneralScore(totalScore)

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
