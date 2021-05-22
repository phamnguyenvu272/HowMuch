package com.howmuch;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String email;
    private String name;
    private String id;
    private ArrayList<Transaction> transactions;
    private ArrayList<Budget> budgets = new ArrayList<>();

    public User(String email, String name, String id, ArrayList<Transaction> transactions) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.transactions = transactions;
    }

    public User(String email, String name, String id) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.transactions = new ArrayList<Transaction>();
    }

    public User() {

    }

//    public User(String email, String name, String id) {
//        this.email = email;
//        this.name = name;
//        this.id = id;
//    }

//    public void addTransaction(Transaction transaction) {
//        transactions.add(transaction);
//    }
//
//    public void getTransactions() {
//
//    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(ArrayList<Budget> budgets) {
        this.budgets = budgets;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}
