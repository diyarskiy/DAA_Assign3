/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {

        int n = w.cells();

        if (n == 0) {
            return new Result(new int[0], 0, 0);
        }

        int[][] open = Graphs.open(w, broken);

        // Count dead ends
        int deadEnds = 0;

        for (int[] neighbours : open) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }

        // DFS preparation
        boolean[] seen = new boolean[n];
        int[] parent = new int[n];
        java.util.Arrays.fill(parent, -1);

        int[] stack = new int[n];
        int[] direction = new int[n];

        int top = 0;
        int visited = 1;

        stack[0] = 0;
        seen[0] = true;

        // Iterative DFS
        while (top >= 0 && !seen[n - 1]) {

            int u = stack[top];

            if (direction[top] == 4) {
                top--;
                continue;
            }

            int v = switch (direction[top]++) {
                case 0 -> u - w.cols();

                case 1 -> u % w.cols() + 1 < w.cols()
                        ? u + 1 : -1;

                case 2 -> u + w.cols();

                default -> u % w.cols() > 0
                        ? u - 1 : -1;
            };

            if (v < 0 || v >= n || seen[v]) {
                continue;
            }

            boolean door = false;

            for (int neighbour : open[u]) {
                if (neighbour == v) {
                    door = true;
                    break;
                }
            }

            if (!door) {
                continue;
            }

            parent[v] = u;
            seen[v] = true;
            visited++;

            stack[++top] = v;
            direction[top] = 0;
        }

        // Reconstruct the path
        if (!seen[n - 1]) {
            return new Result(new int[0], visited, deadEnds);
        }

        int length = 0;

        for (int v = n - 1; v != -1; v = parent[v]) {
            length++;
        }

        int[] path = new int[length];

        for (int v = n - 1, i = length - 1;
             i >= 0;
             i--, v = parent[v]) {

            path[i] = v;
        }

        return new Result(path, visited, deadEnds);
    }
}
