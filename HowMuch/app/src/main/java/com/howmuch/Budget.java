package com.howmuch;

public class Budget {

    private double budgetAmount;
    private int category;
    private double currentAmount;

    public Budget() {

    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public Budget(double budgetAmount, int category, double currentAmount) {
        this.budgetAmount = budgetAmount;
        this.category = category;
        this.currentAmount = currentAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }
}
