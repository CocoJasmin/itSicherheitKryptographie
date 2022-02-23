package BTC_Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public enum Configuration {
    instance;

    Transaction genesisTransaction;
    HashMap<String, TransactionOutput> utx0Map = new HashMap<>();
    float minimumTransaction = 0.1f;
    ArrayList<Block> blockchain = new ArrayList<>();
    int difficulty = 4;
    int transactionSequence = 0;
}