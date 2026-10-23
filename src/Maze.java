/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {

        DisjointSet ds = new DisjointSet(w.cells());

        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int i = 0; i < w.count(); i++) {

            int a = w.a()[i];
            int b = w.b()[i];

            if (ds.union(a, b)) {
                broken[count] = i;
                count++;
            }
        }

        return java.util.Arrays.copyOf(broken, count);
    }



    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {

        Integer[] order = new Integer[w.count()];

        for (int i = 0; i < w.count(); i++) {
            order[i] = i;
        }

        // Sort wall indexes by cost
        java.util.Arrays.sort(
                order,
                java.util.Comparator.comparingInt(i -> w.cost()[i])
        );

        DisjointSet ds = new DisjointSet(w.cells());

        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int i : order) {
            if (ds.union(w.a()[i], w.b()[i])) {
                broken[count++] = i;

                if (count == w.cells() - 1) {
                    break;
                }
            }
        }

        return java.util.Arrays.copyOf(broken, count);
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {

        int n = w.cells();
        if (n == 0) return 0;

        // Build adjacency lists of wall indexes
        int[] degree = new int[n];

        for (int i = 0; i < w.count(); i++) {
            degree[w.a()[i]]++;
            degree[w.b()[i]]++;
        }

        int[][] edges = new int[n][];

        for (int i = 0; i < n; i++) {
            edges[i] = new int[degree[i]];
        }

        int[] pos = new int[n];

        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];

            edges[a][pos[a]++] = i;
            edges[b][pos[b]++] = i;
        }

        boolean[] visited = new boolean[n];

        java.util.PriorityQueue<Integer> pq =
                new java.util.PriorityQueue<>(
                        java.util.Comparator.comparingInt(i -> w.cost()[i])
                );

        visited[0] = true;
        int reached = 1;

        for (int i : edges[0]) {
            pq.add(i);
        }

        long total = 0;

        while (!pq.isEmpty() && reached < n) {

            int i = pq.poll();

            int a = w.a()[i];
            int b = w.b()[i];

            if (visited[a] && visited[b]) {
                continue;
            }

            int next = visited[a] ? b : a;

            visited[next] = true;
            reached++;

            total += w.cost()[i];

            for (int edge : edges[next]) {
                int other = w.a()[edge] == next
                        ? w.b()[edge]
                        : w.a()[edge];

                if (!visited[other]) {
                    pq.add(edge);
                }
            }
        }

        if (reached != n) {
            throw new IllegalArgumentException("Disconnected graph");
        }

        return total;
    }
}
