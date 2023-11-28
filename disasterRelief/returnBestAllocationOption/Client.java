import java.util.*;

/*
 * Disaster Relief Allocation Algorithm
 * by George Felton
 * 
 * This is a simplified tool for allocating resources in response to a disaster.
 * Given a list of locations and a budget, it will return the best possible
 * combination of those locations to maximise the budgetary impact. It specifically
 * focuses on helping the most people possible, and views spending in an
 * all-or-nothing matter (no partial funding for locations).
 * 
 */
public class Client {
    private static Random rand = new Random();

    // Main function to demonstrate use of allocateRelief functionality.
    // Feed in a set of Locations (either simple or randomly determined),
    // and a budget to feed into the algorithm, and print the results.
    public static void main(String[] args) throws Exception {
        // List<Location> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Location> scenario = createSimpleScenario();
        System.out.println(scenario);

        double budget = 2000;
        Allocation allocation = allocateRelief(budget, scenario);
        printResult(allocation, budget);
    }

    /*
     * allocateRelief() takes in a budget and list of locations, and returns the
     * best combination of those locations to allocate resources to (within the
     * budget provided). If two alloctions would help the same number of people,
     * return the cheaper one. If no locations or budget are provided, return an
     * empty Allocation.
     * 
     * @Return Allocation, containing the set of locations that allow for aid
     * to be given to the most people within the budget.
     */
    public static Allocation allocateRelief(double budget, List<Location> sites) {
        if (sites.size() == 0 || budget <= 0) {
            return new Allocation();
        }
        // Recursion using private allocateRelief method
        return allocateRelief(budget, sites, new Allocation(), 0);
    }

    /*
     * Private Helper Method allocateRelief uses recursion to consider all possible
     * permutations of locations within the budget, returning the best combination
     * found as an Allocation. Takes in the budget, list of locations, the current
     * allocation to be tested, and the index of sites from the previous recursive
     * iteration.
     * 
     * @Return an Allocation, containing the set of locations that allow for aid
     * to be given to the most people within the budget.
     */
    private static Allocation allocateRelief(double budget, List<Location> sites,
            Allocation allocation, int prevSiteIndex) {
        Allocation bestAllocation = allocation;

        // Loop through sites
        for (int i = prevSiteIndex; i < sites.size(); i++) {
            Location thisSite = sites.get(i);

            // recursion case: if prevSiteIndex hasn't reach end of sites and allocation
            // doesn't currently contain thisSite
            if (prevSiteIndex < sites.size() && !allocation.getLocations().contains(thisSite)) {
                // CHOOSE
                allocation = allocation.withLoc(thisSite);
                // base case: if allocation has exceeded budget, do nothing
                if (budget >= allocation.totalCost()) {

                    // EXPLORE
                    // capture best allocation of further recursive calls for comparison against
                    // current best
                    Allocation result = allocateRelief(budget, sites, allocation, i);

                    // bestAllocation is one that helps the most people
                    if (result.totalPeople() > bestAllocation.totalPeople()) {
                        bestAllocation = result;
                        // if two Allocations help the same number of people, the cheaper one is
                        // preferred
                    } else if (result.totalPeople() == bestAllocation.totalPeople()
                            && result.totalCost() < bestAllocation.totalCost()) {
                        bestAllocation = result;
                    }
                }

                // UNCHOOSE
                allocation = allocation.withoutLoc(thisSite);
            }
        }

        // Returns the best allocation found
        return bestAllocation;

    }
    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!**

    public static void printResult(Allocation alloc, double budget) {
        System.out.println("Result: ");
        System.out.println("  " + alloc);
        System.out.println("  People helped: " + alloc.totalPeople());
        System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
        System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
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
