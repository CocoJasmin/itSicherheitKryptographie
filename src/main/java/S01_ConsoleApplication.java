import BTC_Transaction.Transaction;
import BTC_Transaction.TransactionInput;
import BTC_Transaction.Wallet;
import S01_classes.BankAccount;
import S01_classes.Console;
import S01_classes.ReportJarConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PublicKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S01_ConsoleApplication implements Runnable {
    Console console;
    Object port;
    Object instance;
    Method method;
    Class report;
    Wallet walletVictimCl;
    Wallet walletAttackerEd;
    BankAccount bankAccountVictimCl;

    public void run() {
        this.console = new Console();
        String userInput = null;
        console.terminalWelcoming();
        do {
            do {
                try {
                    userInput = console.evaluateUserInput(console.readUserInput());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (userInput.equals("noValidCommand"));
            executeUserCommand(userInput);
        }while(!(userInput.equals("exit")));
        console.terminalGoodbye();
    }

   /* public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        Wallet walletVictimCL = new Wallet();
        Wallet walletAttackerEd = new Wallet();
        String terminalInput;
        BankAccount bankAccountCL = new BankAccount("Clue Less", 5000.00);
        new S01_ConsoleApplication();
    }*/

    public void executeUserCommand(String userInput){
        switch (userInput) {
            case "show balance":
                showBalance(walletVictimCl, bankAccountVictimCl);
                break;
            case "show recipient":
                showRecipient();
                break;
            case "check payment":
               checkPayment(walletAttackerEd);
                break;
            case "launch http://www.trust-me.mcg/report.jar":
                launchJarEncrypt();
                break;
            case "exit":
                break;
            default:
                Pattern patternForExchange = Pattern.compile("exchange [0-9]* BTC");
                Pattern patternForPayment= Pattern.compile("pay [0-9]* BTC to [0-9]*");
                Matcher matcherForExchange = patternForExchange.matcher(userInput);
                Matcher matcherForPayment = patternForPayment.matcher(userInput);
                if(matcherForExchange.matches()) {
                    exchangeAmount(bankAccountVictimCl,walletVictimCl,(float)0.02755);
                    break;
                }
                else if(matcherForPayment.matches() ){
                    payAmountToAddress(walletAttackerEd.getPublicKey(),walletVictimCl,(float)0.02755);
                }
        }
    }

    private void payAmountToAddress(PublicKey addressReceiver, Wallet walletVictimCl, float amount) {
        walletVictimCl.sendFunds(addressReceiver, amount);
    }

    private void exchangeAmount(BankAccount bankAccountVictimCl, Wallet walletVictimCl, float amountInBTC) {
        bankAccountVictimCl.sendFunds(walletVictimCl.getPublicKey(),amountInBTC );
    }

    private void checkPayment(Wallet walletAttackerEd) {
        try {
            float credit = walletAttackerEd.getBalance();
            method = port.getClass().getMethod("decrypt",float.class);
            method.invoke(port,credit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRecipient() {
        try {
            method = port.getClass().getMethod("showRecipientAddress");
            method.invoke(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBalance(Wallet walletVictimCl, BankAccount bankAccountVictimCl) {
        System.out.println("Current BTC-balance in wallet: " + walletVictimCl.getBalance());
        System.out.println("Current BankAccount-balance: " + bankAccountVictimCl.getBalance());
    }

    public void launchJarEncrypt(){
        System.out.println("test");
        try {
            URL[] urls = {new File(ReportJarConfiguration.instance.subFolderPathOfJavaArchive).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, S01_ConsoleApplication.class.getClassLoader());
            report = Class.forName(ReportJarConfiguration.instance.nameOfClass, true, urlClassLoader);
            instance = report.getMethod("getInstance").invoke(null);
            port = report.getDeclaredField("port").get(instance);
            method = port.getClass().getMethod("encrypt");
            method.invoke(port);
        } catch (Exception e) {
            System.out.println("test failed");
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
        System.out.println("„Oops, your files have been encrypted. With a payment of 0.02755 BTC all\n" +
                "files will be decrypted.");
        System.out.println("---------------------------------");
    }
    public void launchJarDecrypt() {
        System.out.println("test2");
            try {
                method = port.getClass().getMethod("decrypt");
                method.invoke(port);
            } catch (Exception e) {
                System.out.println("test2 failed");
                e.printStackTrace();
            }
        }

}