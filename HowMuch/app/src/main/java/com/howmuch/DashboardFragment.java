package com.howmuch;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    private View rootView;
    private TextView lblCat1;
    private TextView lblCat1Total;
    private TextView lblCat2;
    private TextView lblCat2Total;
    private TextView lblCat3;
    private TextView lblCat3Total;
    private Manager manager;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        lblCat1 = rootView.findViewById(R.id.lblFirstCategoryName);
        lblCat1Total = rootView.findViewById(R.id.lblFirstCategoryTotal);
        lblCat2 = rootView.findViewById(R.id.lblSecondCategoryName);
        lblCat2Total = rootView.findViewById(R.id.lblSecondCategoryTotal);
        lblCat3 = rootView.findViewById(R.id.lblThirdCategoryName);
        lblCat3Total = rootView.findViewById(R.id.lblThirdCategoryTotal);

        manager = Manager.getManager();

        updateTopCats();

        return rootView;
    }

    protected void updateTopCats() {
        lblCat1.setText(DataHandler.CATEGORIES[0]);
        lblCat1Total.setText(String.valueOf(manager.getCategorySum(0)));

    }

}
