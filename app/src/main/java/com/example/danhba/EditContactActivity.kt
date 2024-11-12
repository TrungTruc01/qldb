package com.example.danhba

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditContactActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var dbHelper: DatabaseHelper
    private var contactId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        updateButton = findViewById(R.id.buttonUpdate)
        dbHelper = DatabaseHelper(this)

        contactId = intent.getIntExtra("CONTACT_ID", 0)
        val contact = dbHelper.getContactById(contactId)

        nameEditText.setText(contact.name)
        phoneEditText.setText(contact.phone)
        emailEditText.setText(contact.email)

        updateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()

            dbHelper.updateContact(Contact(contactId, name, phone, email))
            finish()
        }
    }
}
