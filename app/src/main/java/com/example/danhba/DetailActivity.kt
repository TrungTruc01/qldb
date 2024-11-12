package com.example.danhba

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var callButton: Button
    private lateinit var emailButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var dbHelper: DatabaseHelper
    private var contactId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        nameTextView = findViewById(R.id.textViewName)
        phoneTextView = findViewById(R.id.textViewPhone)
        emailTextView = findViewById(R.id.textViewEmail)
        callButton = findViewById(R.id.buttonCall)
        emailButton = findViewById(R.id.buttonEmail)
        updateButton = findViewById(R.id.buttonUpdate)
        deleteButton = findViewById(R.id.buttonDelete)
        dbHelper = DatabaseHelper(this)

        contactId = intent.getIntExtra("CONTACT_ID", 0)
        val contact = dbHelper.getContactById(contactId)

        nameTextView.text = contact.name
        phoneTextView.text = contact.phone
        emailTextView.text = contact.email

        callButton.setOnClickListener {
            // Yêu cầu quyền trước khi thực hiện cuộc gọi
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                val callIntent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:${contact.phone}")
                }
                startActivity(callIntent)
            } else {
                // Nếu chưa có quyền, yêu cầu quyền
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
        }

        emailButton.setOnClickListener {
            val intent = Intent(this, SendEmailActivity::class.java)
            intent.putExtra("CONTACT_EMAIL", contact.email)
            startActivity(intent)
        }



        updateButton.setOnClickListener {
            val intent = Intent(this, UpdateContactActivity::class.java)
            intent.putExtra("CONTACT_ID", contactId)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            dbHelper.deleteContact(contactId)
            finish() // Quay lại MainActivity sau khi xóa
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, thực hiện cuộc gọi
                callButton.performClick() // Gọi lại sự kiện bấm nút
            } else {
                // Thông báo cho người dùng rằng quyền không được cấp
                Toast.makeText(this, "Quyền gọi điện không được cấp", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
