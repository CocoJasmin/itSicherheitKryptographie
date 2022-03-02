import java.security.PublicKey;

public interface IReport {
    void encrypt();

    void decrypt();

    String getPublicKeyAttacker();

    void setPublicKeyAttacker(PublicKey publicKey);

    void showReceiverAttacker();

    //for Task S03
    float getRansomAmountAttacker();

    float updateRansomAmountAttacker(float alreadyPayed);
}
