package com.example.khatabook;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Transaction {

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    User sender;
    User receiver;
    int amount;
    String time;
    boolean approved;
    boolean paid;

    public Transaction(User sender, User receiver, int amount,String time, boolean approved, boolean paid) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.time = time;
        this.approved = approved;
        this.paid = paid;
    }

    public Transaction() {

    }
}
