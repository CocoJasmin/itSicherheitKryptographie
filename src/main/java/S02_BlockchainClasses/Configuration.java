package S02_BlockchainClasses;

import java.util.ArrayList;
import java.util.HashMap;

public enum Configuration {
    instance;
    Transaction genesisTransaction;
    HashMap<String, TransactionOutput> utx0Map = new HashMap<>();
    ArrayList<Block> blockchain = new ArrayList<>();
    int difficulty = 4;
    int transactionSequence = 0;
    float reward = 0.025f;
}