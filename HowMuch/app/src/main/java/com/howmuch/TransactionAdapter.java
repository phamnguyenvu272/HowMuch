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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    List<Transaction> transactions;
    Context context;
    int lastPosition = -1;

    public TransactionAdapter(List<Transaction> units, Context context) {
        this.transactions = units;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction, parent, false);
        TransactionViewHolder holder = new TransactionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        final Transaction transaction = transactions.get(position);

        holder.lblTransactionListName.setText(transaction.getDescription());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        holder.getLblTransactionListAmount.setText(format.format(transaction.getTotal()));
        holder.lblCategory.setText(DataHandler.CATEGORIES[transaction.getCategory()]);
        holder.cardTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle transaction view.
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
        //returns the number of elements the RecyclerView will display
        return transactions.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Transaction data) {
        transactions.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Transaction data) {
        int position = transactions.indexOf(data);
        Manager.getManager().removeTransaction(data.getId());
        notifyItemRemoved(position);
    }

}
