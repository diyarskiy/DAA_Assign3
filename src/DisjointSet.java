/**
 * Mission 0. Disjoint set union (DSU).
 * Cells are numbered 0 .. n-1. Write the whole class here.
 * Needed: union by size (the smaller tree goes under the larger one) and path compression in find.
 */

public class DisjointSet {

    private int[] parent;
    private int[] size;
    private int sets;

    /** Constructor */
    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        sets = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /** Find the root of an element */
    public int find(int x) {
        int root = x;

        while (parent[root] != root) {
            root = parent[root];
        }

        // Path compression
        while (x != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
    }

    /** Merge two sets */
    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);

        if (ra == rb) {
            return false;
        }

        /** Union by size */
        if (size[ra] < size[rb]) {
            int temp = ra;
            ra = rb;
            rb = temp;
        }

        parent[rb] = ra;
        size[ra] += size[rb];
        sets--;

        return true;
    }

    /** Check whether two elements are connected */
    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    /** Return the number of separate sets */
    public int count() {
        return sets;
    }
}
