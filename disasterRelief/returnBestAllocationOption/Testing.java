import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {

    @Test
    @DisplayName("Only one location given")
    public void testSingleLocation() {
        int budget = 500;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 100, 400);
        loci.add(firstLoc);

        Set<Location> expected = new HashSet<Location>();
        expected.add(firstLoc);

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

    @Test
    @DisplayName("Only one valid location given")
    public void testSingleValidLocation() {
        int budget = 500;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 100, 400);
        Location secondLoc = new Location("Location #2", 150, 600);
        loci.add(firstLoc);
        loci.add(secondLoc);

        Set<Location> expected = new HashSet<Location>();
        expected.add(firstLoc);

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

    @Test
    @DisplayName("Zero valid locations given")
    public void testNoValidLocations() {
        int budget = 100;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 100, 400);
        Location secondLoc = new Location("Location #2", 150, 600);
        loci.add(firstLoc);
        loci.add(secondLoc);

        Set<Location> expected = new HashSet<Location>();

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

    @Test
    @DisplayName("No locations given")
    public void testNoLocations() {
        int budget = 500;
        List<Location> locus = new ArrayList<>();

        Set<Location> actual = Client.allocateRelief(budget, locus).getLocations();
        Set<Location> expected = new HashSet<Location>();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

    @Test
    @DisplayName("Zero Budget given")
    public void testZeroBudget() {
        int budget = 0;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 100, 400);
        Location secondLoc = new Location("Location #2", 150, 600);
        loci.add(firstLoc);
        loci.add(secondLoc);

        Set<Location> expected = new HashSet<Location>();

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

@Test
    @DisplayName("Choosng best of multiple valid locations")
    public void testMultipleValid() {
        int budget = 500;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 100, 400);
        Location secondLoc = new Location("Location #2", 500, 300);
        Location thirdLoc = new Location("Location #3", 200, 450);
        loci.add(firstLoc);
        loci.add(secondLoc);
        loci.add(thirdLoc);

        Set<Location> expected = new HashSet<Location>();
        expected.add(secondLoc);

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

    @Test
    @DisplayName("If two allocations cost the same, help more people")
    public void testEqualAllocations() {
        int budget = 1000;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 100, 400);
        Location secondLoc = new Location("Location #2", 150, 600);
        Location thirdLoc = new Location("Location #3", 500, 1000);
        loci.add(firstLoc);
        loci.add(secondLoc);
        loci.add(thirdLoc);

        Set<Location> expected = new HashSet<Location>();
        expected.add(thirdLoc);

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }

    @Test
    @DisplayName("Test of deeply recursed best")
    public void testManyRecursions() {
        int budget = 1000;
        List<Location> loci = new ArrayList<>();
        Location firstLoc = new Location("Location #1", 1000, 200);
        Location secondLoc = new Location("Location #2", 150, 600);
        Location thirdLoc = new Location("Location #3", 500, 1000);
        Location fourthLoc = new Location("Location #4", 200, 250);
        Location fifthLoc = new Location("Location #5", 125, 400);
        Location sixthLoc = new Location("Location #6", 5000, 600);
        Location seventhLoc = new Location("Location #7", 2000, 200);
        loci.add(firstLoc);
        loci.add(secondLoc);
        loci.add(thirdLoc);
        loci.add(fourthLoc);
        loci.add(fifthLoc);
        loci.add(sixthLoc);
        loci.add(seventhLoc);

        Set<Location> expected = new HashSet<Location>();
        expected.add(firstLoc);
        expected.add(sixthLoc);
        expected.add(seventhLoc);

        Set<Location> actual = Client.allocateRelief(budget, loci).getLocations();
        assertEquals(expected, actual, "Allocate Relief picked " + actual + " instead of " + expected);
    }
}
