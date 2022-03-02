package S02_BlockchainClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PoW {
    public static void selectRandomMiner(HashMap<String, Wallet> minerList) {
        int numberOfMiners = minerList.size() - 1;
        Random rand = new Random();
        int chooseRandomly = rand.nextInt(numberOfMiners);
        int counter = 0;
        for (Map.Entry<String, Wallet> entry : minerList.entrySet()) {
            if (counter == chooseRandomly) {
                getReward(entry.getKey(), entry.getValue());
                return;
            }
            counter++;
        }
    }

    public static void getReward(String minerName, Wallet minerWallet) {
        int currentBlockIndex = Configuration.instance.blockchain.size() - 1;
        Block currentBlock = Configuration.instance.blockchain.get(currentBlockIndex);
        //Generate Reward
        TransactionOutput rewardTransaction = new TransactionOutput(minerWallet.getPublicKey(), Configuration.instance.reward, "Reward:" + currentBlock.getPreviousHash());
        Configuration.instance.utx0Map.put(rewardTransaction.getID(), rewardTransaction);
        System.out.println("New balance of the Wallet of miner " + minerName + " : " + minerWallet.getBalance() + "BTC");
    }
}