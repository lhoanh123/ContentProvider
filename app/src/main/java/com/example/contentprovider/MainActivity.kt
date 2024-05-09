package com.example.contentprovider

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contentprovider.MyContentProvider


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        return true
    }

    fun onClickAddDetails(view: View?) {
        val values = ContentValues()
        values.put(MyContentProvider.name, (findViewById<View>(R.id.textName) as EditText).text.toString())
        values.put("age", (findViewById<View>(R.id.textAge) as EditText).text.toString().toInt())
        values.put("gender", (findViewById<View>(R.id.textGender) as EditText).text.toString())

        contentResolver.insert(MyContentProvider.CONTENT_URI, values)

        Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
    }


    fun onClickShowDetails(view: View?) {
        val resultView = findViewById<View>(R.id.res) as TextView
        val cursor = contentResolver.query(MyContentProvider.CONTENT_URI, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val strBuild = StringBuilder()
            do {
                val id = cursor.getString(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val age = cursor.getInt(cursor.getColumnIndex("age"))
                val gender = cursor.getString(cursor.getColumnIndex("gender"))

                // Append formatted text to the StringBuilder
                strBuild.append("$id - $name\n")
                strBuild.append("Age: $age\n")
                strBuild.append("Gender: $gender\n\n")
            } while (cursor.moveToNext())
            cursor.close()
            resultView.text = strBuild.toString()
        } else {
            resultView.text = "No Records Found"
        }
    }
}
