import java.util.Random;

public class CoinFlipGame {
    public static String flipCoin() {
        Random random = new Random();
        int randomInt = random.nextInt(0, 2);

        if (randomInt == 0) return "HEADS";
        else return "TAILS";
    }
}
