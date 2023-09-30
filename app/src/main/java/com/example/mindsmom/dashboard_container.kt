package com.example.mindsmom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class dashboard_container : AppCompatActivity() {

    private lateinit var Ocd_btn:Button
    private lateinit var biopolar_access:Button
    private lateinit var buttonAssess: Button
    private lateinit var generalAssess:Button
    private lateinit var anxietyassess:Button
    private lateinit var btnChatbot: ImageButton
    private lateinit var logoutButton: Button
    private lateinit var userDetailsTextView: TextView
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_container)

        Ocd_btn = findViewById(R.id.Ocd_btn)
        biopolar_access = findViewById(R.id.biopolar_access)
        anxietyassess = findViewById(R.id.anxietyaccess)
        buttonAssess = findViewById(R.id.buttonAssess)
        btnChatbot = findViewById(R.id.btn_chatbot)
        logoutButton = findViewById(R.id.logout)
        userDetailsTextView = findViewById(R.id.user_details)
        generalAssess = findViewById(R.id.generalAssess)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        if (user == null) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            userDetailsTextView.text = user!!.email
        }
        generalAssess.setOnClickListener{
            val intent = Intent(this, GeneralAssessmentActivity::class.java)
            startActivity(intent)
        }
        anxietyassess.setOnClickListener{
            val intent = Intent(this,AnxietyActivity::class.java)
            startActivity(intent)
        }
        biopolar_access.setOnClickListener{
            val intent = Intent(this,BipolarAssessmentActivity::class.java)
            startActivity(intent)
        }
        Ocd_btn.setOnClickListener{
            val intent = Intent(this,OCDActivity::class.java)
            startActivity(intent)
        }


        buttonAssess.setOnClickListener {
            val intent = Intent(this, MentalHealthAssessmentActivity::class.java)
            startActivity(intent)
        }

        btnChatbot.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}
