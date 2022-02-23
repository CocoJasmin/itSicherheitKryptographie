package BTC_Transaction;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.HashMap;

public class Application {
    public static void main(String... args) {
        Security.addProvider(new BouncyCastleProvider());

        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet coinbase = new Wallet();

        Configuration.instance.genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        Configuration.instance.genesisTransaction.generateSignature(coinbase.getPrivateKey());
        Configuration.instance.genesisTransaction.setId("0");
        Configuration.instance.genesisTransaction.getOutputs().add(
                new TransactionOutput(Configuration.instance.genesisTransaction.getRecipient(),
                        Configuration.instance.genesisTransaction.getValue(), Configuration.instance.genesisTransaction.getId())
        );

        Configuration.instance.utx0Map.put(
                Configuration.instance.genesisTransaction.getOutputs().get(0).getID(),
                Configuration.instance.genesisTransaction.getOutputs().get(0));

        System.out.println("creating and mining genesis block");
        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(Configuration.instance.genesisTransaction);
        addBlock(genesisBlock);

        Block block01 = new Block(genesisBlock.getHash());
        System.out.println("\nwalletA transfers funds (40) to walletB");
        block01.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        addBlock(block01);
        System.out.println("walletA (balance) | " + walletA.getBalance());
        System.out.println("walletB (balance) | " + walletB.getBalance());

        Block block02 = new Block(block01.getHash());
        System.out.println("walletA transfers funds (1000)");
        block02.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
        addBlock(block02);
        System.out.println("walletA (balance) | " + walletA.getBalance());
        System.out.println("walletB (balance) | " + walletB.getBalance());

        Block block03 = new Block(block02.getHash());
        System.out.println("walletB transfers funds (20) to walletA");
        block03.addTransaction(walletB.sendFunds(walletA.getPublicKey(), 20f));

        System.out.println("walletA (balance) | " + walletA.getBalance());
        System.out.println("walletB (balance) | " + walletB.getBalance());

        isChainValid();
    }

    public static void isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = StringUtility.getDifficultyString(Configuration.instance.difficulty);
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(Configuration.instance.genesisTransaction.getOutputs().get(0).getID(), Configuration.instance.genesisTransaction.getOutputs().get(0));

        for (int i = 1; i < Configuration.instance.blockchain.size(); i++) {
            currentBlock = Configuration.instance.blockchain.get(i);
            previousBlock = Configuration.instance.blockchain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("#current hashes not equal");
                return;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("#trevious hashes not equal");
                return;
            }

            if (!currentBlock.getHash().substring(0, Configuration.instance.difficulty).equals(hashTarget)) {
                System.out.println("#block not mined");
                return;
            }

            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if (currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return;
                }

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are not equal to oututs on Transaction(" + t + ")");
                    return;
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getId());

                    if (tempOutput == null) {
                        System.out.println("#referenced input on transaction(" + t + ") is missing");
                        return;
                    }

                    if (input.getUTX0().getValue() != tempOutput.getValue()) {
                        System.out.println("#referenced input on transaction(" + t + ") value invalid");
                        return;
                    }

                    tempUTXOs.remove(input.getId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getID(), output);
                }

                if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#transaction(" + t + ") output recipient is invalid");
                    return;
                }

                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("#transaction(" + t + ") output 'change' is not sender");
                    return;
                }
            }
        }
        System.out.println("blockchain valid");
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(Configuration.instance.difficulty);
        Configuration.instance.blockchain.add(newBlock);
    }
}
