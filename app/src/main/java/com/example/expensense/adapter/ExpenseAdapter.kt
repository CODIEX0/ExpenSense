package com.example.expensense.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensense.R
import com.example.expensense.data.Expense

class ExpenseAdapter(private val expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val amount: TextView = itemView.findViewById(R.id.expenseAmount)
        val date: TextView = itemView.findViewById(R.id.expenseDate)
        val description: TextView = itemView.findViewById(R.id.expenseDescription)
        val category: TextView = itemView.findViewById(R.id.expenseCategory)

        fun bind(expense: Expense) {
            amount.text = "R " + expense.amount.toString()
            date.text = expense.date
            description.text = expense.description
            category.text = expense.category
        }

        init {
            // Set click listener for the itemView
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val bird = expenses[position]
                    onItemClickListener?.onItemClick(bird)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.bind(expense)
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    // Define an interface for item click handling
    interface OnItemClickListener {
        fun onItemClick(expense: Expense)
    }

    private var onItemClickListener: OnItemClickListener? = null

    // Setter for the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}