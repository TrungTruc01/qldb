package com.example.danhba
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var contactListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        contactListView = findViewById(R.id.listViewContacts)
        searchEditText = findViewById(R.id.editTextSearch)
        addButton = findViewById(R.id.buttonAdd)

        addButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)
        }

        loadContacts()

        // Search contacts as the user types
        searchEditText.addTextChangedListener {
            loadContacts(it.toString())
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadContacts() // Refresh the contact list after a new contact is added
        }
    }

    private fun loadContacts(searchQuery: String = "") {
        val contacts = dbHelper.getAllContacts(searchQuery)
        val simpleAdapter = SimpleAdapter(
            this,
            contacts,
            android.R.layout.simple_list_item_2,
            arrayOf("name", "phone"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        contactListView.adapter = simpleAdapter

        contactListView.setOnItemClickListener { _, _, position, _ ->
            val contactId = contacts[position]["id"] as Int
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("CONTACT_ID", contactId)
            startActivity(intent)
        }
    }

    companion object {
        private const val ADD_CONTACT_REQUEST_CODE = 1
    }
}
