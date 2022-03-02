import java.security.PublicKey;

public interface IReport {
    void encrypt();

    void decrypt();

    String getPublicKeyAttacker();

    void setPublicKeyAttacker(PublicKey publicKey);

    void showReceiverAttacker();
}
