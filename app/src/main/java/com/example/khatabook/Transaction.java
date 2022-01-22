package com.example.khatabook;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Transaction {

    User sender;
    User receiver;
    int amount;
    LocalDateTime time;
    boolean approved;
    boolean paid;

    public Transaction(User sender, User receiver, int amount, LocalDateTime time, boolean approved, boolean paid) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.time = time;
        this.approved = approved;
        this.paid = paid;
    }

}
