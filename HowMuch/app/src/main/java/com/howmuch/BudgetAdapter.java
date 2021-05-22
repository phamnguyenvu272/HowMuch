package com.howmuch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetViewHolder> {

    List<Budget> budgets;
    Context context;
    int lastPosition = -1;
    private View view;
    public BudgetAdapter(List<Budget> budgets, Context context) {
        this.budgets = budgets;
        this.context = context;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_budget, parent, false);
        BudgetViewHolder holder = new BudgetViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        final Budget budget = budgets.get(position);

        holder.lblBudgetListName.setText(DataHandler.CATEGORIES[budget.getCategory()]);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        holder.lblBudgetListBudget.setText(format.format(budget.getBudgetAmount()));
        holder.lblBudgetListCurrent.setText(format.format(budget.getCurrentAmount()));
        holder.cardBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle budget view.
            }
        });
        holder.ibtnBudgetListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BudgetEditActivity.class);
                intent.putExtra("categoryInt", budget.getCategory());
                context.startActivity(intent);
            }
        });

        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }
}
