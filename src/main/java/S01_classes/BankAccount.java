package S01_classes;

import BTC_Transaction.Transaction;
import BTC_Transaction.TransactionInput;
import BTC_Transaction.TransactionOutput;
import BTC_Transaction.Wallet;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map;

public class BankAccount {
    String userName;
    double credit;

    public BankAccount(String userName, double credit) {
        this.userName = userName;
        this.credit = credit;
    }

    public void deposit(double amount){
        this.credit+=amount;
    }
    public void withdraw(double amount){
        this.credit-=amount;
    }

    public double getBalance(){
        return this.credit;
   }


    public Transaction sendFunds(PublicKey recipient, float amountInBTC) {
        if (getBalance() < amountInBTC) {
            System.out.println("#not enough funds to send transaction - transaction discarded");
            return null;
        }

        Transaction transaction = new Transaction(null, recipient, amountInBTC, null);
        transaction.generateSignature(null);
        withdraw(amountInBTC/0.000019);
        return transaction;
    }
}