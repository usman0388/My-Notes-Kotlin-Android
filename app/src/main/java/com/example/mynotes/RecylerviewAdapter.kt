package com.example.mynotes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class RecylerviewAdapter(private val dataSet: ArrayList<Note>, private val mcontext: Context, private val relativeLayout: RelativeLayout) :
    RecyclerView.Adapter<RecylerviewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitle: TextView
        var textViewText: TextView
        var selectionCard: CardView

        var radioButton: RadioButton
        init {
            // Define click listener for the ViewHolder's View.
            textViewTitle = view.findViewById(R.id.noteTitle)
            textViewText = view.findViewById(R.id.noteText)
            selectionCard = view.findViewById(R.id.cardView)

            radioButton = view.findViewById(R.id.itemSlectionView)
        }


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.notetitle, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(dataSet[position].isSelectable){
            viewHolder.radioButton.setVisible(true)
            viewHolder.radioButton.isChecked = dataSet[position].Selected
            viewHolder.selectionCard.setOnClickListener {
                viewHolder.radioButton.isChecked = !viewHolder.radioButton.isChecked
                dataSet[position].Selected = !dataSet[position].Selected
                notifyDataSetChanged()
                return@setOnClickListener
            }
        }else{
            viewHolder.radioButton.setVisible(false)
            viewHolder.radioButton.isChecked = false
            setAllSelectable(false)
            viewHolder.textViewTitle.text = dataSet[position]._noteTitle
            viewHolder.textViewText.text = dataSet[position]._noteText
            viewHolder.selectionCard.setOnClickListener(){
                var intet = Intent(mcontext,TextEdit::class.java)
                intet.putExtra("note",dataSet)
                intet.putExtra("pos",position)
                mcontext.startActivity(intet)

            }
            /*Long click listener on the card view which makes the radio button visible along with the multi selection view
             (relative view) shown to the user. Also it checks the radio button true, and also sets the Selected in the dataset to
             true according to the position
            * */
            viewHolder.selectionCard.setOnLongClickListener {
                viewHolder.radioButton.isChecked = true
                viewHolder.radioButton.setVisible(true)
                setAllSelectable(true)
                relativeLayout.visibility=View.VISIBLE
                dataSet[position].Selected = true
                notifyDataSetChanged()
                return@setOnLongClickListener true
            }
        }
    }
    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    //Iterates through the entire list and sets the isSelectable true which enables the multi select feature on
    private fun setAllSelectable(value: Boolean) {
        for(i in dataSet){
            i.isSelectable= value
        }
    }
}


