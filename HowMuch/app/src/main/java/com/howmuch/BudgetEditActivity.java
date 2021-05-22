package com.howmuch;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class BudgetEditActivity extends AppCompatActivity {

    private EditText txtBudgetAmount;
    private Manager dm;
    private int category;
    private Budget budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dm = Manager.getManager();

        Intent intent = getIntent();
        category = intent.getIntExtra("categoryInt", -1);
            if (category < 0) {
                finish();
            }

        for (int i = 0; i < dm.getBudgets().size(); i++) {
            if (dm.getBudgets().get(i).getCategory() == category) {
                budget = dm.getBudgets().get(i);
            }
        }

        txtBudgetAmount = findViewById(R.id.txtEditBudget);

    }


    public void btnSaveOnClick(View view) {
        if (txtBudgetAmount.getText().toString().trim().isEmpty()) {
            txtBudgetAmount.setError("Cannot be empty.");
            return;
        }
        double newAmount = Double.valueOf(txtBudgetAmount.getText().toString());
        budget.setBudgetAmount(newAmount);
        dm.updateBudget(budget);
        Toast.makeText(this, "Edited Budget", Toast.LENGTH_LONG).show();
        finish();
    }
}
