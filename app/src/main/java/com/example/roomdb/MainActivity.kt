package com.example.roomdb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferencesName
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.roomdb.room.User
import com.example.roomdb.room.UserDatabase
import com.example.roomdb.ui.theme.RoomDBTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        val editText = findViewById<EditText>(R.id.edit_text_id)
        val saveButton = findViewById<Button>(R.id.btn_save)
        val navigateToActivity2Btn = findViewById<Button>(R.id.navigate_to_activity2)
        val textView = findViewById<TextView>(R.id.text)

        val userDatabase =
            Room.databaseBuilder(this, UserDatabase::class.java, "user-database").build()

        val dao = userDatabase.userDao()

        saveButton.setOnClickListener {
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
}


//with(sharedPref.edit()) {
//    putString("key", "Value")
//    apply()
//}