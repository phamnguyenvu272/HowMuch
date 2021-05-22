package com.howmuch;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity   implements DatePickerDialog.OnDateSetListener{

    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private TextInputEditText txtDate;
    private TextInputEditText txtTotal;
    private TextInputEditText txtDescription;
    private DatePickerDialog datePicker;

    private Manager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dm = Manager.getManager();

        Calendar newDate = Calendar.getInstance();
        year = newDate.get(Calendar.YEAR);
        monthOfYear = newDate.get(Calendar.MONTH);
        dayOfMonth = newDate.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(
                this, AddTransactionActivity.this, year, monthOfYear, dayOfMonth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtDate = findViewById(R.id.txtNewTransactionDate);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    datePicker.show();
                }
            }
        });

        txtTotal = findViewById(R.id.txtNewTransactionTotal);
        txtDescription = findViewById(R.id.txtNewTransactionDescription);
    }

    public void btnNewTransactionCameraOnClick(View view) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = month;
        this.dayOfMonth = dayOfMonth;
        txtDate.setText(month +"/" +dayOfMonth + "/" + year);
    }

    public void btnAddTransactionOnClick(View view) {
        Transaction transaction = new Transaction();
        double total = 0.0;
        if (txtTotal.getText().toString().trim().isEmpty()) {
            txtTotal.setError("Please don't leave the total empty");
            return;
        } else {
            total = Double.valueOf(txtTotal.getText().toString());
        }
        String description = txtDescription.getText().toString();
        String date = txtDate.getText().toString();

        transaction.setDate(date);
        transaction.setDescription(description);
        transaction.setTotal(total);

        dm.addTransaction(transaction);

        Toast.makeText(this, "New transaction added!", Toast.LENGTH_LONG).show();
        finish();
    }
}
