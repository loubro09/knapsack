package main;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    // Generate all combinations of size k
    public static <T> List<List<T>> getCombinations(List<T> items, int k) {
        List<List<T>> result = new ArrayList<>();
        combine(items, 0, k, new ArrayList<>(), result);
        return result;
    }

    private static <T> void combine(List<T> items, int start, int k, List<T> temp, List<List<T>> result) {
        if (temp.size() == k) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i < items.size(); i++) {
            temp.add(items.get(i));
            combine(items, i + 1, k, temp, result);
            temp.remove(temp.size() - 1);
        }
    }
}
