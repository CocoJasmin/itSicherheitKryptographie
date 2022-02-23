import S01_classes.TerminalSimulator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import BTC_Transaction.Block;
import BTC_Transaction.Wallet;
import S01_classes.BankAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class situation_TaskS01 {
    Wallet walletVictimCL;
    Wallet walletAttackerEd;
    BankAccount bankAccountVictimCL;

    @BeforeEach
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
        walletVictimCL = new Wallet();
        walletAttackerEd = new Wallet();
        bankAccountVictimCL = new BankAccount("Clue Less", 5000.00);
    }

    @Test
    public void situationS01(){
        assertEquals(5000.00,bankAccountVictimCL.getBalance());
        S01_ConsoleApplication application = new S01_ConsoleApplication();
        Thread thread = new Thread(application);
        thread.run();
        String terminalInput = "launch http://www.trust-me.mcg/report.jar";
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(terminalInput.getBytes());
        System.setIn(in);
        thread.interrupt();
    }
}
