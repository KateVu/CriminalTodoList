package com.katevu.criminalintent

import java.util.*

data class Crime (
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false) {


    override fun toString(): String {
        return "Crime(id=$id, title='$title', date=$date, isSolved=$isSolved)"
    }
}