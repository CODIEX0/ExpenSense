package com.example.expensense.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.expensense.Global
import com.example.expensense.R
import com.example.expensense.data.Expense
import com.example.expensense.databinding.FragmentExpenseTrackingBinding
import com.example.expensense.databinding.FragmentViewExpenseBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class ViewExpenseFragment : Fragment() {

    private lateinit var binding: FragmentViewExpenseBinding
    private lateinit var Date: String
    private lateinit var Id: String
    private lateinit var Category: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewExpenseBinding.inflate(inflater, container, false)

        val description = binding.edtDescription.text.toString()
        val amountText = binding.edtAmount.text.toString()
        var amount: Double

        try {
            amount = amountText.toDouble()
            // Conversion was successful, and 'amount' contains the double value.
        } catch (e: NumberFormatException) {
            // Handle the case where the conversion failed (invalid input)
            // You can show a Toast or display an error message to the user
            // For example:
            Toast.makeText(context, "Invalid amount format. Please enter a valid number.", Toast.LENGTH_SHORT).show()
            amount = 0.0
        }
        Category = binding.CategorySpinner.selectedItem.toString()

        val expense = Expense(
            Id,
            Date,
            amount,
            description,
            Category
        )

        binding.btnEdit.setOnClickListener{
            editExpense(expense)
        }

        binding.btnDelete.setOnClickListener {
            deleteExpense(expense)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the arguments Bundle
        val arguments = arguments

        // Check if arguments exist
        if (arguments != null) {
            // Retrieve the data from the bundle
            val id = arguments.getString("UID")
            val description = arguments.getString("description")
            val amount = arguments.getString("amount")
            val date = arguments.getString("date")
            val category = arguments.getString("category")
            //Update UI elements---

            Id = id.toString()
            Date = date.toString()
            binding.edtDescription.setText(description)
            binding.edtAmount.setText(amount)
            Category = category.toString()
        }
    }

    fun editExpense(updatedExpense: Expense){
        val index = Global.expenses.indexOfFirst { it.id == updatedExpense.id }

        if(index != -1){
            Global.expenses[index] = updatedExpense //replace the old expense with the updated one
        }
        Toast.makeText(context, "Expense edited successfully.", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Navigate back to the previous screen
    }

    fun deleteExpense(expense: Expense){
        val index = Global.expenses.indexOfFirst { it.id == expense.id }

        if(index != -1){
            Global.expenses.removeAt(index) //remove the expense from the list
        }
        Toast.makeText(context, "Expense deleted successfully.", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Navigate back to the previous screen
    }
}