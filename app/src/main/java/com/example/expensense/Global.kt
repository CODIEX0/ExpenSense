package com.example.expensense

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.expensense.data.Expense
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

//Singleton object for storing live data
object Global {

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val todayDate = currentDate.format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    var expenses = mutableListOf(
        Expense(
            UUID.randomUUID().toString(),
            todayDate,
            2984.09,
            "Jacket",
            "SHOPPING"
        ),
        Expense(
            UUID.randomUUID().toString(),
            todayDate,
            17.0,
            "Bread",
            "FOOD"
        ),
        Expense(
            UUID.randomUUID().toString(),
            todayDate,
            2500.0,
            "Rent",
            "ACCOMMODATION"
        )

    )

}
