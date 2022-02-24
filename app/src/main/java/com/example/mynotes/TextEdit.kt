package com.example.mynotes

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import java.io.Serializable
import java.lang.Exception

class TextEdit : AppCompatActivity() {
    var flag: Boolean = false
    var notes: Note?=null
    var notesArray: ArrayList<Note>?=null
    var actFlag: Boolean = false

    var num: Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_edit)
        val textTitleView = findViewById<TextView>(R.id.changeTitleView)
        val textTitleEdit = findViewById<EditText>(R.id.changeTitleEdit)
        val saveButton = findViewById<Button>(R.id.saveData)
        val desc = findViewById<EditText>(R.id.desc)

        try {
            notesArray = intent.getSerializableExtra("note") as ArrayList<Note>
            num = intent.getIntExtra("pos",-1)
            textTitleView.text = notesArray!![num!!]!!._noteTitle
            textTitleEdit.setText(notesArray!![num!!]!!._noteTitle)
            desc.setText(notesArray!![num!!]!!._noteText)
            actFlag = true

        }catch (e: Exception) {
            notes = null
        }


        textTitleView.setOnClickListener(){

            textTitleView.setVisible((false))
            textTitleEdit.setVisible(true)
        }
        saveButton.setOnClickListener(){
            notes = if(textTitleEdit.text.toString().isNotEmpty() && desc.text.toString().isNotEmpty()){
                Note(textTitleEdit.text.toString(),desc.text.toString())
            }else{
                Note(textTitleView.text.toString(),desc.text.toString())

            }
            if(actFlag){
                notesArray!![num!!]= notes!!
            }
            Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show()
            flag = true


        }
    }

    override fun onBackPressed() {

        if(flag){
            val resultIntent = Intent()
            resultIntent.putExtra("note",notes)

            setResult(RESULT_OK, resultIntent);
            finish()
            SavePrevferences()
        }
        super.onBackPressed()
    }
    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    private fun SavePrevferences(){
        try {
            var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            var gson = Gson()
            var json: String? = gson.toJson(notesArray)

            editor.putString("notes",json)
            editor.apply()
        }catch (e: Exception){
            throw e
        }

    }
}