import BTC_Transaction.Wallet;
import S01_classes.BankAccount;
import S01_classes.Console;
import S01_classes.ReportJarConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PublicKey;
import java.security.Security;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S01_ConsoleApplication{
    private  Console console;
    private  Object port;
    private  Object instance;
    private  Method method;
    private  Class report;
    private  Wallet walletVictimCL ;
    private  Wallet walletAttackerEd;
    BankAccount bankAccountVictimCL;

    private S01_ConsoleApplication(){
        Security.addProvider(new BouncyCastleProvider());
        this.console = new Console();
        this.walletVictimCL = new Wallet();
        this.walletAttackerEd = new Wallet();
        this.bankAccountVictimCL = new BankAccount("Clue Less", 5000.00);
        checkTerminalInput();
    }
    public static void main(String[] args) {
        new S01_ConsoleApplication();
    }
    public void checkTerminalInput(){
        String userInput="noValidCommand";
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

    public void executeUserCommand(String userInput){
        switch (userInput) {
            case "show balance":
                showBalance(walletVictimCL, bankAccountVictimCL);
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
                Pattern patternForExchange = Pattern.compile("exchange [0-9]*.[0-9]* BTC");
                Pattern patternForPayment= Pattern.compile("pay [0-9]*.[0-9]* BTC to [0-9]*");
                Matcher matcherForExchange = patternForExchange.matcher(userInput);
                Matcher matcherForPayment = patternForPayment.matcher(userInput);
                if(matcherForExchange.matches()) {
                    exchangeAmount(bankAccountVictimCL,walletVictimCL,(float)0.02755);
                    break;
                }
                else if(matcherForPayment.matches() ){
                    payAmountToAddress(walletAttackerEd.getPublicKey(),walletVictimCL,(float)0.02755);
                }
        }
    }

    private void payAmountToAddress(PublicKey addressReceiver, Wallet walletVictimCl, float amount) {
        walletVictimCl.sendFunds(addressReceiver, amount);
    }

    private static void exchangeAmount(BankAccount bankAccountVictimCl, Wallet walletVictimCl, float amountInBTC) {
        bankAccountVictimCl.exchangeEuroToBTC(walletVictimCl,amountInBTC );
    }

    private void checkPayment(Wallet walletAttackerEd) {
        if(blockchainEvaluation()){
            System.out.println("successful");
            launchJarDecrypt();

        }else System.out.println("unsuccessful");
    }

    private void showRecipient() {
        System.out.println("---------------------------------");
        System.out.println("Please transfer the BTC to the wallet with the address: "+ walletAttackerEd.getPublicKey());
        System.out.println("---------------------------------");
    }

    private static void showBalance(Wallet walletVictimCl, BankAccount bankAccountVictimCl) {
        System.out.println("---------------------------------");
        System.out.println("Current BTC-balance in wallet: " + walletVictimCl.getBalance()+" BTC");
        System.out.printf("Current BankAccount-balance: %.2f Euro\n", bankAccountVictimCl.getBalance());
        System.out.println("---------------------------------");
    }

    public void launchJarEncrypt(){
        try {
            URL[] urls = {new File(ReportJarConfiguration.instance.subFolderPathOfJavaArchive).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, S01_ConsoleApplication.class.getClassLoader());
            report = Class.forName(ReportJarConfiguration.instance.nameOfClass, true, urlClassLoader);
            instance = report.getMethod("getInstance").invoke(null);
            port = report.getDeclaredField("port").get(instance);
            method = port.getClass().getMethod("encrypt");
            method.invoke(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public boolean blockchainEvaluation(){
        boolean validBlockchain = true;
        //here needs to be checked with the help of task S02 if blockchain is valid
        // true initialization needs to be removed
        return validBlockchain;
    }

}