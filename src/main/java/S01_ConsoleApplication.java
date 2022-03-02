import S01_ConsoleClasses.BankAccount;
import S01_ConsoleClasses.Console;
import S01_ConsoleClasses.ReportJarConfiguration;
import S02_BlockchainClasses.BlockchainSimulationConfig;
import S02_BlockchainClasses.Wallet;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S01_ConsoleApplication {
    boolean dataEncrypted;
    BankAccount bankAccountVictimCL;
    private Console console;
    private Object port;
    private Object instance;
    private Method method;
    private Class report;
    private Wallet walletVictimCL;
    private Wallet walletAttackerEd;
    private BlockchainSimulationConfig configurationForTransactions;

    private S01_ConsoleApplication() {
        Security.addProvider(new BouncyCastleProvider());
        this.console = new Console();
        this.walletVictimCL = new Wallet();
        this.walletAttackerEd = new Wallet();
        this.configurationForTransactions = new BlockchainSimulationConfig();
        this.bankAccountVictimCL = new BankAccount("Clue Less", 5000.00);
        configureReportJar();
        checkTerminalInput();
    }

    public static void main(String[] args) {
        new S01_ConsoleApplication();
    }

    public void checkTerminalInput() {
        String userInput = "noValidCommand";
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
        } while (!(userInput.equals("exit")));
        console.terminalGoodbye();
    }

    public void executeUserCommand(String userInput) {
        switch (userInput) {
            case "show balance":
                showBalance();
                break;
            case "show recipient":
                showRecipient();
                break;
            case "check payment":
                checkPayment();
                break;
            case "launch http://www.trust-me.mcg/report.jar":
                launchJarEncrypt();
                break;
            case "exit":
                break;
            default:
                Pattern patternForExchange = Pattern.compile("exchange [0-9]*.[0-9]* BTC");
                Pattern patternForPayment = Pattern.compile("pay [0-9]*.[0-9]* BTC to \\p{Graph}*");
                Matcher matcherForExchange = patternForExchange.matcher(userInput);
                Matcher matcherForPayment = patternForPayment.matcher(userInput);
                if (matcherForExchange.matches()) {
                    exchangeAmount((float) 0.02755);
                    break;
                } else if (matcherForPayment.matches()) {
                    String[] userInputSplit = userInput.split(" ");
                    float amount = Float.parseFloat(userInputSplit[1]);
                    payAmountToAddress(amount, userInputSplit[4]);
                }
        }
    }

    private void exchangeAmount(float amountInBTC) {
        bankAccountVictimCL.exchangeEuroToBTC(walletVictimCL, amountInBTC);
    }

    private void showBalance() {
        System.out.println("---------------------------------");
        System.out.println("Current BTC-balance in wallet: " + walletVictimCL.getBalance() + " BTC");
        System.out.printf("Current BankAccount-balance: %.2f Euro\n", bankAccountVictimCL.getBalance());
        System.out.println("---------------------------------");
    }

    private void showRecipient() {
        if (dataEncrypted) {
            try {
                method = port.getClass().getMethod("showReceiverAttacker");
                method.invoke(port);
                System.out.println(Base64.getEncoder().encodeToString(walletAttackerEd.getPublicKey().getEncoded()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("No one wants your money! (so far....)");
    }

    private void payAmountToAddress(float amount, String addressReceiver) {
        try {
            method = port.getClass().getMethod("getPublicKeyAttacker");
            if (addressReceiver.equals(method.invoke(port))) {
                configurationForTransactions.transferBTC(walletVictimCL, walletAttackerEd.getPublicKey(), amount);
                System.out.println("New balance on the recipient's Wallet: " + walletAttackerEd.getBalance() + " BTC");
            } else {
                System.out.println("No valid address! Please try a second time!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPayment() {
        if (BlockchainSimulationConfig.isChainValid()) {
            System.out.println("successful");
            launchJarDecrypt();

        } else System.out.println("unsuccessful");
    }

    public void configureReportJar() {
        try {
            URL[] urls = {new File(ReportJarConfiguration.instance.subFolderPathOfJavaArchive).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, S01_ConsoleApplication.class.getClassLoader());
            report = Class.forName(ReportJarConfiguration.instance.nameOfClass, true, urlClassLoader);
            instance = report.getMethod("getInstance").invoke(null);
            port = report.getDeclaredField("port").get(instance);
            method = port.getClass().getMethod("setPublicKeyAttacker", PublicKey.class);
            method.invoke(port, walletAttackerEd.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchJarEncrypt() {
        try {
            dataEncrypted = true;
            method = port.getClass().getMethod("encrypt");
            method.invoke(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchJarDecrypt() {
        if (dataEncrypted) {
            dataEncrypted = false;
            try {
                method = port.getClass().getMethod("decrypt");
                method.invoke(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}