package com.example.expensense.fragments

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensense.Global
import com.example.expensense.Global.expenses
import com.example.expensense.R
import com.example.expensense.adapter.ExpenseAdapter
import com.example.expensense.data.Expense
import com.example.expensense.databinding.FragmentExpenseTrackingBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class ExpenseTrackingFragment : Fragment(), ExpenseAdapter.OnItemClickListener {

    private lateinit var binding: FragmentExpenseTrackingBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenseTrackingBinding.inflate(inflater, container, false)

        binding.btnAddExpense.setOnClickListener{

            val description = binding.edtExpenseDescription.text.toString()
            val amount = binding.edtExpenseAmount.text.toString().toDouble()
            // Get the current date
            val calendar = Calendar.getInstance()
            // Format the date to display in your desired format (e.g., "dd/MM/yyyy")
            val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
            val date = dateFormat.format(calendar.time)

            val category = binding.CategorySpinner.selectedItem.toString()

            val expense = Expense(
                UUID.randomUUID().toString(),
                date,
                amount,
                description,
                category
            )
            addExpense(expense)
        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lstExpenses = binding.rvExpenses
        // Set up the LinearLayoutManager for the RecyclerView
        val expenseLayoutManager = LinearLayoutManager(requireContext())
        lstExpenses.layoutManager = expenseLayoutManager

        try{
            // Create an instance of an ExpenseAdapter and pass the OnItemClickListener
            val expenseAdapter = ExpenseAdapter(expenses)
            expenseAdapter.setOnItemClickListener(this)
            // Set the adapter to the RecyclerView
            lstExpenses.adapter = expenseAdapter
        }catch (e:Exception){
            Toast.makeText(activity,e.message,Toast.LENGTH_SHORT).show()
            Log.d(ContentValues.TAG, e.message.toString())
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
    fun addExpense(expense: Expense){
        expenses.add(expense) //add the new expense to the list
        Toast.makeText(context, "Expense edited successfully.", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(expense: Expense) {

        // Handle the click event and navigate to a different fragment
        //Add data to bundle
        val bundle = Bundle()
        bundle.putString("UID", expense.id)
        bundle.putString("description", expense.description)
        bundle.putDouble("amount", expense.amount)
        bundle.putString("date", expense.date)
        bundle.putString("category", expense.category)

        try{
            val fragment = ExpenseTrackingFragment()
            fragment.arguments = bundle

            //Navigate to fragment, passing bundle
            findNavController().navigate(R.id.action_ExpenseTrackingFragment_to_ExpenseFragment, bundle)
        }catch (e:Exception){
            Toast.makeText(activity,e.message, Toast.LENGTH_SHORT).show()
            Log.d(ContentValues.TAG, e.message.toString())
        }
    }

}