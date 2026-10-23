/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */

public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String message) {
        System.out.println((ok ? "OK: " : "FAIL: ") + message);

        if (!ok) {
            failed++;
        }
    }

    public static void main(String[] args) {

        DisjointSet ds = new DisjointSet(6);

        check(ds.count() == 6, "Initial count");
        check(!ds.connected(0, 1), "Initially separated");

        check(ds.union(0, 1), "First union");
        check(ds.connected(0, 1), "Connected after union");
        check(ds.count() == 5, "Count after union");

        check(!ds.union(1, 0), "Repeated union");
        check(ds.count() == 5, "Count unchanged");

        ds.union(2, 3);
        ds.union(1, 3);

        check(ds.connected(0, 2), "Transitive connection");
        check(ds.count() == 3, "Three components");

        check(!ds.union(4, 4), "Union with itself");

        DisjointSet large = new DisjointSet(100_000);

        for (int i = 1; i < 100_000; i++) {
            large.union(i - 1, i);
        }

        check(large.count() == 1, "Large merge");
        check(large.connected(0, 99_999), "Large connectivity");

        DisjointSet balanced = new DisjointSet(16);

        for (int width = 1; width < 16; width *= 2) {
            for (int i = 0; i < 16; i += 2 * width) {
                balanced.union(i, i + width);
            }
        }

        check(balanced.count() == 1, "Balanced merges");

        boolean sameRoot = true;

        for (int i = 0; i < 16; i++) {
            if (balanced.find(i) != balanced.find(0)) {
                sameRoot = false;
            }
        }

        check(sameRoot, "Path compression consistency");
        check(new DisjointSet(0).count() == 0, "Empty DSU");

        DisjointSet single = new DisjointSet(1);

        check(single.find(0) == 0, "Singleton root");
        check(!single.union(0, 0), "Singleton self-union");

        if (failed == 0) {
            System.out.println("All checks passed.");
        } else {
            throw new AssertionError(failed + " checks failed");
        }
    }
}
