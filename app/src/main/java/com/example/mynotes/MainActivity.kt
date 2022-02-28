package com.example.mynotes

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
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
        //Button to add new note
        val floatingButtonAdd: View = findViewById(R.id.floatingActionButton)

        //Relative layout for managing multiple selection items
        val relativeLayout = findViewById<View>(R.id.delMenu) as RelativeLayout
        //Button to delete items when selected
        val delButton = findViewById<View>(R.id.delButton)
        //Closing the option to select mutliple items
        val closeButton = findViewById<View>(R.id.imageButton)
        //Recyler view
        try {
            var recylerview = findViewById<RecyclerView>(R.id.recyclerView)
            viewAdapter = RecylerviewAdapter(noteList,this, relativeLayout)
            recylerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            recylerview.adapter = viewAdapter


            val itemtouchhelperCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }
                //Swipe right to remove a single item
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    noteList.removeAt(viewHolder.adapterPosition)
                    viewAdapter!!.notifyDataSetChanged()
                }
            }

            //Dell button on click listner
            delButton.setOnClickListener {
                deleteSelectedItems()
                SetSelectable(false)
                relativeLayout.visibility= View.GONE
            }
            val itemTouchHelper = ItemTouchHelper(itemtouchhelperCallback)
            itemTouchHelper.attachToRecyclerView(recylerview)
        }catch (e: Exception){
            throw e
        }
        //Button to add new data
        floatingButtonAdd.setOnClickListener{
            val intent = Intent(this,TextEdit::class.java)
            flag = true
            startActivityForResult(intent, FILE_PICKER_ID)
        }

        //Button to close the multi selection option
        closeButton.setOnClickListener {
            relativeLayout.visibility = View.GONE
            SetSelectable(false)
            viewAdapter!!.notifyDataSetChanged()
        }
    }
    //Check box on Click listner
    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.checkBox -> {
                    if (checked) {
                        SetSelectable(true)
                        viewAdapter!!.notifyDataSetChanged()
                    } else {
                        SetSelectable(false)
                        viewAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    //Deletes the selected items from the list
    private fun deleteSelectedItems() {
        var i = 0
        while(i<noteList.size){
            if(noteList[i].Selected){
                noteList.removeAt(i)
                if(i==0){
                    continue
                }else{
                    i-1
                }
            }else{
                i++
            }
        }
        SetSelectable(false)
        SavePrevferences()
        viewAdapter!!.notifyDataSetChanged()
    }

    //Sets true or false on the isSelectable which make the multi selection option availabe and
    // Selected to which if true then it shows the certain item is selected
    private fun SetSelectable(value: Boolean){
        if(noteList.size>0){
            for(i in noteList){
                i.isSelectable = value
                i.Selected = value
            }
        }
    }
    //Saving list data
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
    //Reading list data
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