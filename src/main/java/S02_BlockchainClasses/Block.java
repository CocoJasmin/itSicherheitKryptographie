package S02_BlockchainClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    private final String previousHash;
    private final long timeStamp;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private String merkleRoot;
    private String hash;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public String calculateHash() {
        return StringUtility.applySha256(previousHash + timeStamp + nonce + merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtility.getMerkleRoot(transactions);
        String target = StringUtility.getDifficultyString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            return;
        }

        if (!Objects.equals(previousHash, "0")) {
            if (!transaction.processTransaction()) {
                System.out.println("transaction failed to process");
                return;
            }
        }

        transactions.add(transaction);
    }
}
