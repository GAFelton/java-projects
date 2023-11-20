import java.util.*;

/*
 * This method takes a budget and a list of Location objects as parameter.
 * The method will compute and return all possible allocations of resources within the given budget.
 * 
 * For the purposes of our simulation, we will assume that providing
 * relief to a location is atomic, meaning that either all people in the
 * location are helped and the full cost is paid, or no relief is allocated
 * to that location. We will not deal with the possibility of providing
 * partial relief to a particular location.
 */

public class Client {
    private static Random rand = new Random();

    public static void main(String[] args) throws Exception {
        // List<Location> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Location> scenario = createSimpleScenario();
        System.out.println(scenario);

        double budget = 2000;
        Set<Allocation> allocations = generateOptions(budget, scenario);
        printResult(allocations, budget);
    }

    public static Set<Allocation> generateOptions(double budget, List<Location> sites) {
        Set<Allocation> allocations = new HashSet<Allocation>();
        if (sites.size() == 0 || budget <= 0) {
            return allocations;
        }

        return generateOptions(budget, sites, allocations, new Allocation(), 0);
    }
    // Recursive backtracking is paired with data structures. If you're using some
    // kind of data structure to track your results, you're likely to be using
    // recursive backtracking.
    // CHOOSE: modify your data structure.
    // EXPLORE: recurse, using your newly modified data structure.
    // UNCHOOSE: revert your data structure.

    private static Set<Allocation> generateOptions(double budget, List<Location> sites, Set<Allocation> allocations,
            Allocation allocation, int prevSiteIndex) {
        // loop through sites

        for (int i = 0; i < sites.size(); i++) {

            Location thisSite = sites.get(i);
            // base case: allocations does not yet contain this allocation (and allocation is under budget)
            if (budget >= allocation.totalCost()) {
                allocations.add(allocation);
            }

            // base case: allocation has exceeded budget
            if (budget < allocation.totalCost()) {
                allocations.add(allocation.withoutLoc(sites.get(prevSiteIndex)));
            }

            // recursion case: if prevSiteIndex hasn't reach end of sites and allocation
            // doesn't currently contain thisSite
            else if (prevSiteIndex < sites.size() && !allocation.getLocations().contains(thisSite)) {

                // CHOOSE
                allocation = allocation.withLoc(thisSite);

                // EXPLORE
                generateOptions(budget, sites, allocations, allocation, i);

                // UNCHOOSE
                allocation = allocation.withoutLoc(thisSite);
            }
        }

        return allocations;
    }

    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!**

    public static void printResult(Set<Allocation> allocs, double budget) {
        for (Allocation alloc : allocs) {
            System.out.println("Result: ");
            System.out.println("  " + alloc);
            System.out.println("  People helped: " + alloc.totalPeople());
            System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
            System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
        }
    }

    public static List<Location> createRandomScenario(int numLocs, int minPop, int maxPop, double minCostPer,
            double maxCostPer) {
        List<Location> result = new ArrayList<>();

        for (int i = 0; i < numLocs; i++) {
            int pop = rand.nextInt(minPop, maxPop + 1);
            double cost = rand.nextDouble(minCostPer, maxCostPer) * pop;
            result.add(new Location("Location #" + i, pop, round2(cost)));
        }

        return result;
    }

    public static List<Location> createSimpleScenario() {
        List<Location> result = new ArrayList<>();

        result.add(new Location("Location #1", 50, 500));
        result.add(new Location("Location #2", 100, 700));
        result.add(new Location("Location #3", 60, 1000));
        result.add(new Location("Location #4", 20, 1000));
        result.add(new Location("Location #5", 200, 900));

        return result;
    }

    private static double round2(double num) {
        return Math.round(num * 100) / 100.0;
    }
}
