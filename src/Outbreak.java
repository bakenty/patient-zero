/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] tested = new boolean[n];
        if (n == 0) return tested;

        long[] withTest = new long[n];
        long[] withoutTest = new long[n];
        testingCosts(0, -1, nb, cost, withTest, withoutTest);
        chooseTests(0, -1, false, nb, withTest, withoutTest, tested);
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        if (n == 0) return 0;
        int[] diameter = {0};
        longestDown(0, -1, nb, diameter);
        return diameter[0];
    }

    private static void testingCosts(int person, int parent, int[][] nb, int[] cost,
                                     long[] withTest, long[] withoutTest) {
        withTest[person] = cost[person];
        for (int child : nb[person]) {
            if (child == parent) continue;
            testingCosts(child, person, nb, cost, withTest, withoutTest);
            withTest[person] += Math.min(withTest[child], withoutTest[child]);
            withoutTest[person] += withTest[child];
        }
    }

    private static void chooseTests(int person, int parent, boolean forced, int[][] nb,
                                    long[] withTest, long[] withoutTest, boolean[] tested) {
        tested[person] = forced || withTest[person] < withoutTest[person];
        for (int child : nb[person]) {
            if (child != parent) {
                chooseTests(child, person, !tested[person], nb, withTest, withoutTest, tested);
            }
        }
    }

    private static int longestDown(int person, int parent, int[][] nb, int[] diameter) {
        int longest = 0;
        int second = 0;
        for (int child : nb[person]) {
            if (child == parent) continue;
            int length = 1 + longestDown(child, person, nb, diameter);
            if (length > longest) {
                second = longest;
                longest = length;
            } else if (length > second) {
                second = length;
            }
        }
        diameter[0] = Math.max(diameter[0], longest + second);
        return longest;
    }
}
