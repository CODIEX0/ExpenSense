package com.example.expensense.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.expensense.databinding.FragmentBudgetManagmentBinding


class BudgetManagmentFragment : Fragment() {

    private lateinit var budgetAdapter: ArrayAdapter<String>

    private lateinit var binding: FragmentBudgetManagmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetManagmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize UI elements
        val budgetNameEditText = binding.editTextBudgetName
        val budgetAmountEditText = binding.editTextBudgetAmount
        val setBudgetButton = binding.buttonSetBudget
        val listViewBudgets = binding.listViewBudgets

        // Create an empty list of budgets
        val budgets = mutableListOf<String>()
        budgetAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, budgets)
        listViewBudgets.adapter = budgetAdapter

        // Set a budget when the button is clicked
        setBudgetButton.setOnClickListener {
            val budgetName = budgetNameEditText.text.toString()
            val budgetAmount = budgetAmountEditText.text.toString()
            if (budgetName.isNotEmpty() && budgetAmount.isNotEmpty()) {
                val budgetText = "$budgetName: $budgetAmount"
                budgets.add(budgetText)
                budgetAdapter.notifyDataSetChanged()
                clearEditTexts(budgetNameEditText, budgetAmountEditText)
            }
        }
        return root
    }

    private fun clearEditTexts(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }

}