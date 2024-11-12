@file:Suppress("DEPRECATION")

package com.example.danhba

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendEmailActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var subjectField: EditText
    private lateinit var messageField: EditText
    private lateinit var sendButton: Button
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)

        // Khởi tạo các view
        emailField = findViewById(R.id.emailField)
        subjectField = findViewById(R.id.subjectField)
        messageField = findViewById(R.id.messageField)
        sendButton = findViewById(R.id.sendButton)
        statusTextView = findViewById(R.id.statusTextView)

        // Thiết lập sự kiện click cho nút gửi email
        sendButton.setOnClickListener {
            val email = emailField.text.toString()
            val subject = subjectField.text.toString()
            val message = messageField.text.toString()
            sendEmail(email, subject, message)
        }
    }

    private fun sendEmail(email: String, subject: String, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = sendEmailViaJavaMail(email, subject, message)
            runOnUiThread {
                statusTextView.text = result
            }
        }
    }

    private fun sendEmailViaJavaMail(email: String, subject: String, message: String): String {
        // Thiết lập thuộc tính cho session
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        // Tạo session với thông tin xác thực
        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("truc241102@gmail.com", "TrungTruc@2411") // Thay thế bằng thông tin tài khoản của bạn
            }
        })

        return try {
            // Tạo message
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress("truc241102@gmail.com"))
                addRecipient(Message.RecipientType.TO, InternetAddress(email))
                this.subject = subject
                setText(message)
            }

            // Gửi message
            Transport.send(mimeMessage)
            "Email đã được gửi"
        } catch (e: Exception) {
            e.printStackTrace()
            "Không thể gửi email: ${e.message}"
        }
    }
}
