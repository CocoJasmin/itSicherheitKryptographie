package S02_BlockchainClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PoW {
    public static void selectRandomMiner(HashMap<String,Wallet> minerList,Block newBlock){
        int numberOfMiners = minerList.size()-1;
        Random rand = new Random();
        int chooseRandomly = rand.nextInt(numberOfMiners);
        int counter =0;
        for (Map.Entry<String, Wallet> entry : minerList.entrySet()) {
            if(counter==chooseRandomly)
            {
                mine(entry.getKey(),entry.getValue(),newBlock);
                return;
            }
            counter++;
        }
    }
    public static void mine(String minerName, Wallet minerWallet, Block newBlock){
        HashMap<String, TransactionOutput> utx0Map = new HashMap<>();;
        newBlock.mineBlock(Configuration.instance.difficulty);
        Configuration.instance.blockchain.add(newBlock);
        //Generate Reward
        TransactionOutput rewardTransaction = new TransactionOutput(minerWallet.getPublicKey(), Configuration.instance.reward, "Reward:" + newBlock.getPreviousHash());
        Configuration.instance.utx0Map.put(rewardTransaction.getID(), rewardTransaction);
        System.out.println("New balance of the Wallet of miner "+minerName+" : " + minerWallet.getBalance() +"BTC");
    }
}