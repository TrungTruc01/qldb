package com.example.danhba

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddContactActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        emailEditText = findViewById(R.id.editTextEmail)
        saveButton = findViewById(R.id.buttonSave)
        dbHelper = DatabaseHelper(this)

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                dbHelper.addContact(Contact(0, name, phone, email))

                // Set result to OK to indicate success
                setResult(Activity.RESULT_OK)
                finish() // Go back to MainActivity
            }
        }
    }
}
