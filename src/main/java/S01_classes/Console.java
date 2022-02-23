package S01_classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {
    public Console() {
    }

    public void terminalWelcoming() {
        System.out.println("Welcome User!");
    }

    public String evaluateUserInput(String input) {
        switch (input) {
            case "show balance" -> System.out.println("Dein Befehl war show balance!");
            case "show recipient" -> System.out.println("Dein Befehl war show recipient ");
            case "check payment" -> System.out.println("Dein Befehl war check payment");
            case "launch http://www.trust-me.mcg/report.jar" -> System.out.println("Dein Befehl war launch http://www.trust-me.mcg/report.jar");
            case "exit" -> System.out.println("Dein Befehl war exit");
            default -> {
                Pattern patternForExchange = Pattern.compile("exchange [0-9]* BTC");
                Pattern patternForPayment = Pattern.compile("pay [0-9]* BTC to [0-9]*");
                Matcher matcherForExchange = patternForExchange.matcher(input);
                Matcher matcherForPayment = patternForPayment.matcher(input);
                if (matcherForExchange.matches() || matcherForPayment.matches()) {
                    System.out.println("Dein Befehl war: " + input);
                    return input;
                } else {
                    System.out.println("Your command is not valid! Please enter a new one.");
                    validCommends();
                    return "noValidCommand";
                }
            }
        }
        return input;
    }

    public String readUserInput() throws IOException {
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("To terminate the Terminal enter exit.");
        System.out.print("Enter your command: ");
        String userInput = reader.readLine();
        System.out.println("Your command was: " + userInput);
        return userInput;
    }

    public void validCommends() {
        System.out.println("---------------------------------");
        System.out.println("This are the possible Commands:");
        System.out.println("\t1.show balance");
        System.out.println("\t2.show recipient");
        System.out.println("\t3.pay [amount] BTC to [address]");
        System.out.println("\t4.exchange [amount] BTC");
        System.out.println("\t5.check payment");
        System.out.println("\t6.launch http://www.trust-me.mcg/report.jar");
        System.out.println("\t7.exit");
        System.out.println("---------------------------------");
    }

    public void terminalGoodbye() {
        System.out.println("The operation has been terminated!");
        System.out.println("Goodbye!");
    }
}
