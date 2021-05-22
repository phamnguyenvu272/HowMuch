package com.howmuch;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    private Manager dm;
    private List<Transaction> transactions = new ArrayList<>();
    private View rootView;
    private RecyclerView recTransactions;
    private TransactionAdapter adapter;


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dm = Manager.getManager();
        rootView = inflater.inflate(R.layout.fragment_transaction, container, false);
        recTransactions = rootView.findViewById(R.id.recTransactions);
        adapter = new TransactionAdapter(transactions, rootView.getContext());
        recTransactions.setAdapter(adapter);
        recTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(200);
        itemAnimator.setRemoveDuration(200);
        recTransactions.setItemAnimator(itemAnimator);
        updateData();
        return rootView;
    }

    public void updateData() {
        transactions = dm.allTransactions();
        adapter = new TransactionAdapter(transactions, rootView.getContext());
        recTransactions.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

}
