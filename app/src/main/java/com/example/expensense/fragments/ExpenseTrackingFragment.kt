package com.example.expensense.fragments

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensense.Global
import com.example.expensense.Global.expenses
//import com.example.expensense.Global.expenses
import com.example.expensense.R
import com.example.expensense.adapter.ExpenseAdapter
import com.example.expensense.data.BudgetItem
import com.example.expensense.data.Expense
import com.example.expensense.databinding.FragmentExpenseTrackingBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class ExpenseTrackingFragment : Fragment(), ExpenseAdapter.OnItemClickListener {

    private lateinit var binding: FragmentExpenseTrackingBinding
    private lateinit var budgetItems: List<BudgetItem>
    private lateinit var budgetSpinner: Spinner
    private lateinit var balanceTextView: TextView
    var balance = 0.0

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

        budgetSpinner = binding.GrantSpinner
        balanceTextView = binding.txtBalance

        val budgetItems = listOf(
            BudgetItem("old-age pension(60 and 74 yrs)", 2090.0),
            BudgetItem("old-age pension(greater than 74 yrs)", 2110.0),
            BudgetItem("Disability Grant", 2090.0),
            BudgetItem("Child Support Grant", 510.0),
            BudgetItem("Foster Child Grant", 1130.0),
            BudgetItem("Child Care Dependency", 2090.0),
            BudgetItem("War Veterans Grant", 2110.0),
            BudgetItem("The Grant-In-Aid", 510.0),
            BudgetItem("Child Support Grant Top-Up", 250.0),
            BudgetItem("SRD", 350.0)
        )
        binding.CategorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, budgetItems)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            budgetItems.map { it.description }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        budgetSpinner.adapter = adapter

        budgetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Update the balance variable when an item is selected
                balance = budgetItems[position].amount
                updateBalanceText()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                balance += 0
            }
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

    fun updateBalanceText() {
        binding.txtBalance.text = "R $balance"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addExpense(expense: Expense){
        expenses.add(expense) //add the new expense to the list
        balance -= expense.amount // Subtract the expense amount from the balance
        updateBalanceText()
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