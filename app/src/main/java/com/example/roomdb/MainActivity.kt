package com.example.roomdb

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.room.Room
import com.example.roomdb.room.User
import com.example.roomdb.room.UserDao
import com.example.roomdb.room.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val titles = mutableListOf<String>()
    private lateinit var userDatabase: UserDatabase
    private lateinit var dao: UserDao

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val editText = findViewById<EditText>(R.id.edit_text_id)
        val saveButton = findViewById<Button>(R.id.btn_save)
        val navigateToActivity2Btn = findViewById<Button>(R.id.navigate_to_activity2)
        textView = findViewById<TextView>(R.id.text)

        userDatabase =
            Room.databaseBuilder(this, UserDatabase::class.java, "user-database").build()

        dao = userDatabase.userDao()

        saveButton.setOnClickListener {
            // saveDataToDb()

            CoroutineScope(IO).launch {
                val providerData = fetchDataFromProvider()

                withContext(Main) {
                    textView.text = providerData
                }
            }
        }
    }

    private fun fetchDataFromProvider(): String {
        val cursor = contentResolver.query(
            CPsContract.DomainEntry.DOMAIN_URI,
            null,
            null,
            null,
            null
        )

        cursor?.let {
            val titleIndex = it.getColumnIndex(CPsContract.DomainEntry.TITLE)
            while (it.moveToNext()) {
                titles.add(it.getString(titleIndex))
            }


        }

        cursor?.close()
        return titles[0]
    }

    fun saveDataToDb() {
        val user =
            User(name = "Amit", address = "Kerela", city = "Trivendrum", phone = "123456789")
        // async
        // runBlocking
        val job = CoroutineScope(IO).launch {
            dao.insert(user)
            val allData = dao.getAll()

            withContext(Main) {
                textView.text = allData.toString()
            }
        }
    }
}


//with(sharedPref.edit()) {
//    putString("key", "Value")
//    apply()
//}