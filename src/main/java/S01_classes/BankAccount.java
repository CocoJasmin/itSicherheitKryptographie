package S01_classes;

import BTCTransaction.Wallet;

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
        return credit;
    }


    public void exchangeEuroToBTC(Wallet walletVictimCl, float amountInBTC) {
        if (getBalance() < (amountInBTC/0.000019)) {
            System.out.println("#not enough funds to send transaction - transaction discarded");
        }else{
            credit-=(amountInBTC/0.000019);
            walletVictimCl.getBTCFromBankAccount(amountInBTC);
        }
    }
}