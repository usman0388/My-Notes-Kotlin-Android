package com.example.mynotes

import java.io.Serializable


class Note constructor(var _noteTitle: String,var _noteText: String, var isSelectable: Boolean): Serializable{
    var Selected: Boolean
    init {
        this.Selected = false
    }
}