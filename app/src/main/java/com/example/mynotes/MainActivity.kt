package com.example.mynotes

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

const val FILE_PICKER_ID =12
class MainActivity : AppCompatActivity() {
    private var noteList = ArrayList<Note>()
    private var viewAdapter: RecylerviewAdapter?=null
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val floatingButtonAdd: View = findViewById(R.id.floatingActionButton)
        //Recyler view
        try {
            var recylerview = findViewById<RecyclerView>(R.id.recyclerView)
            viewAdapter = RecylerviewAdapter(noteList,this)
            recylerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            recylerview.adapter = viewAdapter
        }catch (e: Exception){
            throw e
        }
        //Button to add new data
        floatingButtonAdd.setOnClickListener{
            val intent = Intent(this,TextEdit::class.java)
            Toast.makeText(this,"Button Clicked", Toast.LENGTH_LONG).show()
            flag = true
            startActivityForResult(intent, FILE_PICKER_ID)
        }
    }

    private fun SavePrevferences(){
        try {
            var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            var gson = Gson()
            var json: String? = gson.toJson(noteList)

            editor.putString("notes",json)
            editor.apply()
        }catch (e: Exception){
            throw e
        }

    }
    private fun readPreferences(){

        try {
            if(!flag){
                var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
                var gson = Gson()
                var json: String? = sharedPreferences.getString("notes",null)

                val turnType = object : TypeToken<ArrayList<Note>>(){}.type
                var tempNote = ArrayList<Note>()
                tempNote = gson.fromJson(json,turnType)

                noteList.clear()
                for(i in tempNote){
                    noteList.add(i)
                }

                viewAdapter!!.notifyDataSetChanged()
            }
        }catch (e: Exception){
            throw e
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == FILE_PICKER_ID && resultCode == RESULT_OK){
            var flagNote = data!!.getSerializableExtra("note")
            print(flagNote.toString())

            noteList.add(flagNote as Note)
            if (viewAdapter != null) {
                viewAdapter!!.notifyDataSetChanged();


            }
            SavePrevferences()
            flag =false
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewAdapter != null) {
            readPreferences()
            viewAdapter!!.notifyDataSetChanged()

        }
    }
}