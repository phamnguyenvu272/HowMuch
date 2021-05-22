package com.howmuch;

import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetViewHolder extends RecyclerView.ViewHolder {

    protected CardView cardBudget;
    protected TextView lblBudgetListName;
    protected TextView lblBudgetListCurrent;
    protected TextView lblBudgetListBudget;
    protected ImageButton ibtnBudgetListEdit;

    public BudgetViewHolder(@NonNull View view) {
        super(view);
        cardBudget = view.findViewById(R.id.cardBudget);
        lblBudgetListName = view.findViewById(R.id.lblBudgetListName);
        lblBudgetListCurrent = view.findViewById(R.id.lblBudgetListCurrent);
        lblBudgetListBudget = view.findViewById(R.id.lblBudgetListBudget);
        ibtnBudgetListEdit = view.findViewById(R.id.ibtnBudgetListEdit);
    }
}
