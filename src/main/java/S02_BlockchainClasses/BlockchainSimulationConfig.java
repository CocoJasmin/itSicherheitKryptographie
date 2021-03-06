package S02_BlockchainClasses;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.PublicKey;
import java.security.Security;
import java.util.HashMap;

public class BlockchainSimulationConfig {
    Wallet walletA;
    Wallet walletB;
    Wallet satoshiNakamotoWallet;
    Block genesisBlock;
    Wallet helpWalletForConfig;
    Wallet satoshiNakamoto;
    HashMap<String, Wallet> miners;

    public BlockchainSimulationConfig() {
        Security.addProvider(new BouncyCastleProvider());
        helpWalletForConfig = new Wallet();
        //Create miners and Wallet of Satoshi Nakamoto
        satoshiNakamoto = new Wallet();
        miners = new HashMap<>();
        miners.put("Bob", new Wallet());
        miners.put("Eve", new Wallet());
        miners.put("Sam", new Wallet());
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet WalletC = new Wallet();
        satoshiNakamotoWallet = new Wallet();

        Configuration.instance.genesisTransaction = new Transaction(satoshiNakamotoWallet.getPublicKey(), WalletC.getPublicKey(), 1f, null);
        Configuration.instance.genesisTransaction.generateSignature(satoshiNakamotoWallet.getPrivateKey());
        Configuration.instance.genesisTransaction.setId("0");
        Configuration.instance.genesisTransaction.getOutputs().add(
                new TransactionOutput(Configuration.instance.genesisTransaction.getRecipient(),
                        Configuration.instance.genesisTransaction.getValue(), Configuration.instance.genesisTransaction.getId())
        );

        Configuration.instance.utx0Map.put(
                Configuration.instance.genesisTransaction.getOutputs().get(0).getID(),
                Configuration.instance.genesisTransaction.getOutputs().get(0));

        genesisBlock = new Block("0");
        genesisBlock.addTransaction(Configuration.instance.genesisTransaction);
        addBlock(genesisBlock);
    }

    public static void main(String... args) {
        new BlockchainSimulationConfig();
    }

    public static boolean isChainValid() {
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
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("#trevious hashes not equal");
                return false;
            }

            if (!currentBlock.getHash().substring(0, Configuration.instance.difficulty).equals(hashTarget)) {
                System.out.println("#block not mined");
                return false;
            }

            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if (currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are not equal to oututs on Transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getId());

                    if (tempOutput == null) {
                        System.out.println("#referenced input on transaction(" + t + ") is missing");
                        return false;
                    }

                    if (input.getUTX0().getValue() != tempOutput.getValue()) {
                        System.out.println("#referenced input on transaction(" + t + ") value invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.getId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getID(), output);
                }

                if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#transaction(" + t + ") output recipient is invalid");
                    return false;
                }

                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("#transaction(" + t + ") output 'change' is not sender");
                    return false;
                }
            }
        }
        System.out.println("blockchain valid");
        return true;
    }

    public void transferBTC(Wallet walletSender, PublicKey addressReceiver, float amount) {
        if (walletSender.getBalance() < amount) {
            System.out.println("transaction discarded - not enough funds to send transaction");
            return;
        }
        int currentBlockIndex = Configuration.instance.blockchain.size() - 1;
        Block currentBlock = Configuration.instance.blockchain.get(currentBlockIndex);
        Block block01 = new Block(currentBlock.getHash());
        block01.addTransaction(walletSender.sendFunds(addressReceiver, amount));
        addBlock(block01);
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(Configuration.instance.difficulty);
        Configuration.instance.blockchain.add(newBlock);
        if (!newBlock.equals(genesisBlock)) {
            PoW.selectRandomMiner(miners);
        }
    }
}
