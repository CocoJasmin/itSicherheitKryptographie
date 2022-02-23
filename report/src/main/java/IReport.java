import java.security.PublicKey;

public interface IReport {
    void encrypt();

    void decrypt(float credit);

    void showRecipientAddress();

}
