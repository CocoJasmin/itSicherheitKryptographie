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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S01_Application {
    boolean dataEncrypted;
    BankAccount bankAccountVictimCL;
    private final Console console;
    private Object port;
    private Object instance;
    private Method method;
    private Class report;
    private final Wallet walletVictimClueL;
    private final Wallet walletAttackerEd;
    private final BlockchainSimulationConfig configurationForTransactions;
    private float ransomAmount;
    private float transferredAmount;

    private S01_Application() {
        Security.addProvider(new BouncyCastleProvider());
        this.console = new Console();
        this.walletVictimClueL = new Wallet();
        this.walletAttackerEd = new Wallet();
        this.configurationForTransactions = new BlockchainSimulationConfig();
        this.bankAccountVictimCL = new BankAccount("Clue Less", 5000.00);
        this.transferredAmount = 0;
        configureReportJar();
        checkTerminalInput();
    }

    public static void main(String[] args) {
        new S01_Application();
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
        bankAccountVictimCL.exchangeEuroToBTC(walletVictimClueL, amountInBTC);
    }

    private void showBalance() {
        System.out.println("---------------------------------");
        System.out.println("Current BTC-balance in wallet: " + walletVictimClueL.getBalance() + " BTC");
        System.out.printf("Current BankAccount-balance: %.2f Euro\n", bankAccountVictimCL.getBalance());
        System.out.println("---------------------------------");
    }

    private void showRecipient() {
        if (dataEncrypted) {
            try {
                method = port.getClass().getMethod("showReceiverAttacker");
                method.invoke(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("No one wants your money! (so far....)");
    }

    private void payAmountToAddress(float amount, String addressReceiver) {
        try {
            method = port.getClass().getMethod("getPublicKeyAttacker");
            if (addressReceiver.equals(method.invoke(port))) {
                transferredAmount += amount;
                configurationForTransactions.transferBTC(walletVictimClueL, walletAttackerEd.getPublicKey(), amount);
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
            if (transferredAmount >= getRansomAmount()) {
                launchJarDecrypt();
                transferredAmount = 0;
            } else {
                try {
                    method = port.getClass().getMethod("updateRansomAmountAttacker", float.class);
                    float stillNeedToPay = (float) method.invoke(port, transferredAmount);
                    System.out.println("---------------------------------");
                    System.out.println("You need to transfer more BTC to encrypt your data!");
                    System.out.println(stillNeedToPay + "BTC are missing!");
                    System.out.println("---------------------------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else System.out.println("unsuccessful");
    }

    public void configureReportJar() {
        try {
            URL[] urls = {new File(ReportJarConfiguration.instance.subFolderPathOfJavaArchive).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, S01_Application.class.getClassLoader());
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
        if (!dataEncrypted) {
            try {
                dataEncrypted = true;
                method = port.getClass().getMethod("encrypt");
                method.invoke(port);
                getRansomAmount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public float getRansomAmount() {
        try {
            method = port.getClass().getMethod("getRansomAmountAttacker");
            return ransomAmount = (float) method.invoke(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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