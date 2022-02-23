package com.example.mynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

const val FILE_PICKER_ID =12
class MainActivity : AppCompatActivity() {
    private val noteList = ArrayList<Note>()
    private var viewAdapter: RecylerviewAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val floatingButtonAdd: View = findViewById(R.id.floatingActionButton)
        noteList.add(Note("Title 1","This is for test one check"))
        noteList.add(Note("Title 2","This is for test two check"))
        noteList.add(Note("Title 3","This is for test three check"))
        noteList.add(Note("Title 4","This is for test four check"))
        noteList.add(Note("Title 5","This is for test five check"))
        noteList.add(Note("Title 6","This is for test six check"))
        noteList.add(Note("Title 7","This is for test seven check"))

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
            startActivityForResult(intent, FILE_PICKER_ID)
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
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewAdapter != null) {
            for(i in 0..noteList!!.size){
                viewAdapter!!.notifyItemChanged(i);

            }

        }
    }
}