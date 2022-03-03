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
        val shareButton = findViewById<Button>(R.id.Share)

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
                Note(textTitleEdit.text.toString(),desc.text.toString(),false)
            }else{
                Note(textTitleView.text.toString(),desc.text.toString(),false)

            }
            if(actFlag){
                notesArray!![num!!]= notes!!
            }
            Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show()
            flag = true


        }
        //Share to other apps
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                // (Optional) Here we're setting the title of the content
                putExtra(Intent.EXTRA_TITLE, "Share note")
                try{
                    if(actFlag){
                        if (notesArray!![num!!]._noteTitle.isNotEmpty() && notesArray!![num!!]._noteText.isNotEmpty()){
                            putExtra(Intent.EXTRA_TEXT,notesArray!![num!!]._noteTitle+"\n"+notesArray!![num!!]._noteText)
                        }else{
                            Toast.makeText(this@TextEdit,"Fill All the fields to continue", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        if(notes!= null){
                            putExtra(Intent.EXTRA_TEXT,notes!!._noteTitle+"\n"+notes!!._noteText)
                        }else{
                            Toast.makeText(this@TextEdit,"Fill All the fields to continue", Toast.LENGTH_SHORT).show()

                        }
                    }
                }catch (e: Exception){
                    print(e.toString())
                }
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
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