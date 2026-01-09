package main;

import main.Greedy.Greedy;
import main.Greedy.SackSelectionStrategy.LeastSpaceStrategy;
import main.Greedy.SackSelectionStrategy.RandomSackStrategy;
import main.Greedy.SackSelectionStrategy.SelectSackForItem;
import main.Greedy.TerminationCondition.NoMoreFitCondition;
import main.Greedy.TerminationCondition.ProfitThresholdCondition;
import main.Greedy.TerminationCondition.TerminationCondition;

import java.util.ArrayList;
import java.util.List;

//TODO: exchange items and sacks in test cases to better ones?
public class Main {
    public static void main(String[] args) {
        //Test case with a single sack, least space strategy, no more fit termination condition
        System.out.println(" -- Test Single Sack, Least Space, NoMoreFit -- ");
        testSingleSack(new LeastSpaceStrategy(), new NoMoreFitCondition());

        System.out.println("-----------------------");

        //Test case with a single sack, random sack strategy, profit threshold termination condition
        System.out.println(" -- Test Single Sack, Random Sack, Profit Threshold -- ");
        testSingleSack(new RandomSackStrategy(), new ProfitThresholdCondition(15));

        System.out.println("-----------------------");

        //Test case with multiple sacks, least space strategy, no more fit termination condition
        System.out.println(" -- Test Multiple Sacks, Least Space, NoMoreFit -- ");
        testMultipleSacks(new LeastSpaceStrategy(), new NoMoreFitCondition());

        System.out.println("-----------------------");

        //Test case with multiple sacks, random sack strategy, profit threshold termination condition
        System.out.println(" -- Test Multiple Sacks, Random Sack, Profit Threshold -- ");
        testMultipleSacks(new RandomSackStrategy(), new ProfitThresholdCondition(20));
    }

    //Test case with a single sack
    private static void testSingleSack(SelectSackForItem sackSelectionStrategy, TerminationCondition terminationCondition) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Book", 10, 4));
        items.add(new Item("Computer", 4, 12));
        items.add(new Item("Pencase", 2, 1));
        items.add(new Item("Ruler", 1, 1));
        items.add(new Item("Waterbottle", 2, 2));

        List<Sack> sacks = new ArrayList<>();
        sacks.add(new Sack(1, 15, new ArrayList<>()));

        Solution initialSolution = runGreedy(items, sacks, sackSelectionStrategy, terminationCondition);
        runNeighborhoodSearch(initialSolution, items);
    }

    //Test case with multiple sacks
    private static void testMultipleSacks(SelectSackForItem sackSelectionStrategy, TerminationCondition terminationCondition) {
        System.out.println("\nTest Multiple Sacks:");
/**
        List<Item> items = new ArrayList<>();
        items.add(new Item("Book", 10, 4));
        items.add(new Item("Computer", 4, 12));
        items.add(new Item("Pencase", 2, 1));
        items.add(new Item("Ruler", 1, 1));
        items.add(new Item("Waterbottle", 2, 2));
        items.add(new Item("Laptop", 7, 10));
        items.add(new Item("Headphones", 3, 5));

        List<Sack> sacks = new ArrayList<>();
        sacks.add(new Sack(1, 10, new ArrayList<>()));
        sacks.add(new Sack(2, 8, new ArrayList<>()));
        sacks.add(new Sack(3, 5, new ArrayList<>()));
        sacks.add(new Sack(4, 7, new ArrayList<>()));
 **/
        List<Item> items = new ArrayList<>();
        items.add(new Item("Book", 3, 2));        // low profit
        items.add(new Item("Laptop", 6, 6));      // medium profit
        items.add(new Item("Diamond", 2, 10));    // high profit, initially unused
        items.add(new Item("Notebook", 2, 2));    // low profit
        items.add(new Item("Tablet", 4, 5));      // medium profit


        List<Sack> sacks = new ArrayList<>();
        sacks.add(new Sack(1, 10, new ArrayList<>())); // small-medium
        sacks.add(new Sack(2, 10, new ArrayList<>()));
       // sacks.add(new Sack(3, 8, new ArrayList<>()));
      //  sacks.add(new Sack(4, 12, new ArrayList<>()));



        Solution initialSolution = runGreedy(items, sacks, sackSelectionStrategy, terminationCondition);
        runNeighborhoodSearch(initialSolution, items);
    }

    //Run the greedy algorithm and print the solution
    private static Solution runGreedy(List<Item> items, List<Sack> sacks, SelectSackForItem sackSelectionStrategy, TerminationCondition terminationCondition) {
        Greedy greedy = new Greedy(sackSelectionStrategy, terminationCondition);
        Solution solution = greedy.solve(sacks, items);

        System.out.println("\n Greedy solution: ");
        solution.printSolution();
        printUnusedItems(items);

        return solution;
    }

    //Run neighborhood search to improve the solution
    private static void runNeighborhoodSearch(Solution initialSolution, List<Item> items) {
      //  NeighborhoodSearch neighborhoodSearch = new NeighborhoodSearch();
       // Solution improvedSolution = neighborhoodSearch.improveSolution(initialSolution);

       // NeighborhoodSearch2 neighborhoodSearch = new NeighborhoodSearch2();
       // Solution improvedSolution = neighborhoodSearch.improveSolution(initialSolution);

        NeighborhoodSearch3 neighborhoodSearch = new NeighborhoodSearch3();
        Solution improvedSolution = neighborhoodSearch.improveSolution(initialSolution);

        System.out.println("\n Neighbourhood search solution: ");
        improvedSolution.printSolution();
        printUnusedItems(items);
    }

    //Print unused items that were not assigned to any sack
    private static void printUnusedItems(List<Item> items) {
        System.out.println("\nUnused items:");
        for (Item item : items) {
            if (item.getAssignedSack() == -1) {
                System.out.println("  Item: " + item.getName() +
                        "\n     Profit = " + String.format("%.2f", item.getProfit()));
            }
        }
    }
}