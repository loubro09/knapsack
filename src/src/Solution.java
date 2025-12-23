import java.util.List;

public class Solution {
    private List<Sack> sacks;
    private List<Item> items;

    public Solution(List<Sack> sacks, List<Item> items) {
        this.sacks = sacks;
        this.items = items;
    }
    public int getProfit() {
        int profit = 0;
        for (Sack sack : sacks) {
            for (Item item : sack.getItems()) {
                profit += item.getValue();
            }

        }
        return profit;
    }
}