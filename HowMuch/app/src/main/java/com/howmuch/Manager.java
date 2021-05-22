package com.howmuch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;

public class Manager {

    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<Budget> budgets = new ArrayList<>();
    private static Manager manager;
    private DataHandler dh;
    private User user;

    private FirebaseFirestore firebaseDB;

    private Manager() {
        dh = new DataHandler();
        firebaseDB = FirebaseFirestore.getInstance();

    }

    public static Manager getManager() {
        if (manager == null) {
            manager = new Manager();
            return manager;
        } else {
            return manager;
        }
    }

    public void setUser(User u) {
        this.user = u;
        setTransactions(u.getTransactions());
        setBudgets(u.getBudgets());
    }

    public void setTransactions(ArrayList<Transaction> list) {
        transactions = list;
    }

    public void saveUser(User u) {
        budgets.add(new Budget(0, 0, 0));
        budgets.add(new Budget(0, 1, 0));
        budgets.add(new Budget(0, 2, 0));
        budgets.add(new Budget(0, 3, 0));
        budgets.add(new Budget(0, 4, 0));
        budgets.add(new Budget(0, 5, 0));
        u.setBudgets(budgets);
        dh.addUser(u);
    }

    public void addTransaction(Transaction transaction) {
        transaction.setId(dh.getTransactionId(user.getId()));
        for (Budget budget : budgets) {
            if (budget.getCategory() == transaction.getCategory()) {
                budget.setCurrentAmount(budget.getCurrentAmount() + transaction.getTotal());
            }
        }
        transactions.add(transaction);
        user.setTransactions(transactions);
        user.setBudgets(budgets);
        dh.addUser(user);
    }

    public ArrayList<Transaction> allTransactions() {
        return transactions;
    }

    public ArrayList<Transaction> getListOfCategory(int category) {
        ArrayList<Transaction> categoryList = new ArrayList<>();
        for (Transaction trans : transactions) {
            if (trans.getCategory() == category) {
                categoryList.add(trans);
            }
        }
        return categoryList;
    }

    public double getCategorySum(int c) {
        double total = 0;
        for (Transaction trans : getListOfCategory(c)) {
            total += trans.getTotal();
        }
        return total;
    }

    public void removeTransaction(String id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(id)) {
                transactions.remove(i);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void loadUser(String id, OnCompleteListener<DocumentSnapshot> listener) {
        dh.getUser(id, listener);
    }

    public ArrayList<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(ArrayList<Budget> budgets) {
        this.budgets = budgets;
    }

    public void updateBudget(Budget budget) {
        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getCategory() == budget.getCategory()) {
                budgets.set(i, budget);
                user.setBudgets(budgets);
                dh.addUser(user);
            }
        }
    }
}
