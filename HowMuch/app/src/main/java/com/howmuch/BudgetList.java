package com.howmuch;


import android.os.Bundle;

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
public class BudgetList extends Fragment {

    private RecyclerView recBudgets;
    private Manager dm;
    private List<Budget> budgets = new ArrayList<>();
    private View rootView;
    private BudgetAdapter adapter;

    public BudgetList() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dm = Manager.getManager();
        rootView = inflater.inflate(R.layout.fragment_budget_list, container, false);
        recBudgets = rootView.findViewById(R.id.recBudgets);
        adapter = new BudgetAdapter(budgets, rootView.getContext());
        recBudgets.setAdapter(adapter);
        recBudgets.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(200);
        itemAnimator.setRemoveDuration(200);
        recBudgets.setItemAnimator(itemAnimator);
        updateData();
        return rootView;
    }

    public void updateData() {
        budgets = dm.getBudgets();
        adapter = new BudgetAdapter(budgets, rootView.getContext());
        recBudgets.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

}
