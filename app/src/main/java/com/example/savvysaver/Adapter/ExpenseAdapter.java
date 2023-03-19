package com.example.savvysaver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savvysaver.DataBase;
import com.example.savvysaver.ModifyExpense;
import com.example.savvysaver.R;
import com.example.savvysaver.utilities.Utilities;

import java.util.HashMap;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> expenseList;
    private Context context;

    public ExpenseAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.expenseNameTV.setText(expenseList.get(position).getName());
        holder.expenseAmountTV.setText(expenseList.get(position).getAmount());
        holder.expenseCategoryTV.setText(expenseList.get(position).getType());
        holder.expenseDescriptionTV.setText(expenseList.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModifyExpense.class);
                String id = expenseList.get(position).getId();
                DataBase db = new DataBase(context);
                SharedPreferences preferences = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
                String user = preferences.getString("user", null);
                HashMap<String, String> data = db.getExpense(user, expenseList.get(position).getId());
                intent.putExtra("data", data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView expenseNameTV;
        TextView expenseAmountTV;
        TextView expenseCategoryTV;
        TextView expenseDescriptionTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseNameTV = itemView.findViewById(R.id.tvExpenseName);
            expenseAmountTV = itemView.findViewById(R.id.tvExpenseAmount);
            expenseCategoryTV = itemView.findViewById(R.id.tvExpenseType);
            expenseDescriptionTV = itemView.findViewById(R.id.tvExpenseDescription);
        }
    }
}
