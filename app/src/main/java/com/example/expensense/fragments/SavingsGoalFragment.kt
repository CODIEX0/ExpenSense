package com.example.expensense.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.example.expensense.R
import com.example.expensense.databinding.FragmentSavingsGoalBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SavingsGoalFragment : Fragment() {
    private lateinit var binding: FragmentSavingsGoalBinding
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavingsGoalBinding.inflate(inflater, container, false)

        //val startDateButton = binding.btnStartDate
        //val endDateButton = binding.btnEndDate
        val startDateTextView = binding.tvStartDate
        val endDateTextView = binding.tvEndDate

        //startDateButton.setOnClickListener { showDatePicker(true) }
        //endDateButton.setOnClickListener { showDatePicker(false) }

        return binding.root
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate = dateFormatter.format(selectedDate.time)

                if (isStartDate) {
                    binding.tvStartDate.text = "Start Date: $formattedDate"
                } else {
                    binding.tvEndDate.text = "End Date: $formattedDate"
                }
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

}