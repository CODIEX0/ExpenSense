package com.example.expensense.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.expensense.Global
import com.example.expensense.Global.expenses
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewExpenseBinding.inflate(inflater, container, false)


        binding.btnEdit.setOnClickListener{
            editExpense()
        }

        binding.btnDelete.setOnClickListener {
            deleteExpense()
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
            val amount = arguments.getDouble("amount")
            val date = arguments.getString("date")
            val category = arguments.getString("category")
            //Update UI elements---

            Id = id.toString()
            Date = date.toString()
            binding.edtDescription.setText(description)
            binding.edtAmount.setText(amount.toString())
            Category = category.toString()
        }

        val categories = arrayOf(
            "FOOD",
            "TRANSPORTATION",
            "ACCOMMODATION",
            "ENTERTAINMENT",
            "SHOPPING",
            "UTILITIES",
            "HEALTHCARE",
            "EDUCATION",
            "TRAVEL",
            "MISCELLANEOUS"
        )
        binding.CategorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        binding.CategorySpinner.setSelection(0) // Set the default selected item if needed
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun editExpense(){
        val description = binding.edtDescription.text.toString()
        val amount = binding.edtAmount.text.toString()

        val expense = Expense(
            Id,
            Date,
            amount.toDouble(),
            description,
            Category
        )

        val index = expenses.indexOfFirst { it.id == expense.id }

        if(index != -1){
            expenses[index] = expense //replace the old expense with the updated one
        }
        Toast.makeText(context, "Expense edited successfully.", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Navigate back to the previous screen
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteExpense(){
        val description = binding.edtDescription.text.toString()
        val amount = binding.edtAmount.text.toString()

        val expense = Expense(
            Id,
            Date,
            amount.toDouble(),
            description,
            Category
        )

        val index = expenses.indexOfFirst { it.id == expense.id }

        if(index != -1){
            expenses.removeAt(index) //remove the expense from the list
        }

        ExpenseTrackingFragment().balance += expense.amount // Add the expense amount back to the balance
        ExpenseTrackingFragment().updateBalanceText()

        Toast.makeText(context, "Expense deleted successfully.", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressed() // Navigate back to the previous screen
    }
}