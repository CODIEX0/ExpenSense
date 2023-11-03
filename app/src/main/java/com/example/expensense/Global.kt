package com.example.expensense

import com.example.expensense.data.Expense

//Singleton object for storing live data
object Global {
    var expenses: MutableList<Expense> = mutableListOf()

}
