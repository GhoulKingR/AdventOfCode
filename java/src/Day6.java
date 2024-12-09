import java.io.FileNotFoundException;
import java.util.*;

public class Day6 extends Day {
    private final String filePath = "data/day6/input.txt";

    @Override
    protected void part1() {
        try {
            List<String> file = getFile(filePath);
            Board board = new Board(file);
            int visited = board.startSimulation();
            System.out.println("Visited: " + visited);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    @Override
    protected void part2() {
        try {
            List<String> file = getFile(filePath);
            int height = file.size();
            int width = file.getFirst().length();

            int count = 0;

            for (int y = 0; y < height; y++) {  // infinite loop on 20 confirmed
                for (int x = 0; x < width; x++) {   // infinite loop on 68 confirmed
                    Board board = new Board(file);
                    Vector pos = new Vector(x, y);
                    if (board.obstacleAt(pos)) {
                        continue;
                    }

                    board.placeObstacle(pos);

                    if (board.willLoop()) {
                        count++;
                    }
                }
            }

            System.out.println("Visited: " + count);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private enum Item {
        Nothing, Obstacle, Visited
    }

    private static class Board {

        private final Item[][] items;
        private Vector guardPos;
        private Vector guardDirection;
        private final Guard guard;
        private final Map<Vector, Set<Vector>> visited;

        private Board(Item[][] items, Vector guardPos, Vector guardDirection, Map<Vector, Set<Vector>> visited) {
            this.items = items;
            guard = new Guard(guardPos, guardDirection, this);
            this.visited = visited;
        }

        Board(List<String> map) {
            items = new Item[map.size()][map.getFirst().length()];
            visited = new HashMap<>();

            for (int i = 0; i < items.length; i++) {
                String str = map.get(i);
                for (int j = 0; j < str.length(); j++) {
                    Item currentItem;
                    char itemChar = str.charAt(j);

                    if (itemChar == '#')
                        currentItem = Item.Obstacle;
                    else if (itemChar == '^') {
                        currentItem = Item.Visited;
                        guardPos = new Vector(j, i);
                        guardDirection = new Vector(0, -1);
                    } else
                        currentItem = Item.Nothing;

                    items[i][j] = currentItem;
                }
            }

            guard = new Guard(guardPos, guardDirection, this);
        }

        public Board clone() {
            return new Board(items.clone(), guardPos, guardDirection, new HashMap<>());
        }

        public void placeObstacle(Vector position) {
            items[position.y][position.x] = Item.Obstacle;
        }

        public int startSimulation() {
            while (!outOfBoard(guard.getPosition())) {
                if (!guard.moveForward()) {
                    guard.rotate90();
                }
            }
            return numberOfVisited();
        }

        private int numberOfVisited() {
            int visited = 0;
            for (Item[] row : items) {
                for (Item item : row) {
                    if (item == Item.Visited) {
                        visited++;
                    }
                }
            }
            return visited;
        }

        public boolean willLoop() {
            while (!outOfBoard(guard.getPosition())) {
                if (!guard.moveForward()) {
                    guard.rotate90();
                    // rather than checking if the next turn has been made already
                    // check if the current position and direction has already been moved before

                    Vector position = guard.getPosition();
                    Vector direction = guard.getDirection();

                    if (visited.containsKey(position)) {
                        if (visited.get(position).contains(direction)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public boolean obstacleAt(Vector position) {
            if (outOfBoard(position)) {
                return false;
            } else {
                return items[position.y][position.x] == Item.Obstacle;
            }
        }

        public void markVisited(Vector position) {
            if (visited.containsKey(position)) {
                Set<Vector> directions = visited.get(position);
                directions.add(guardDirection);
            } else {
                Set<Vector> directions = new HashSet<>();
                directions.add(guard.getDirection());
                visited.put(position, directions);
            }
            items[position.y][position.x] = Item.Visited;
        }

        public Vector size() {
            return new Vector(items[0].length, items.length);
        }

        public boolean outOfBoard(Vector position) {
            Vector size = size();
            return (position.x < 0 || position.y < 0 || position.x >= size.x || position.y >= size.y);
        }
    }

    private static class Guard {
        private Vector position;
        private Vector direction;
        private final Board board;

        Guard(Vector position, Vector direction, Board board) {
            this.position = position;
            this.board = board;
            this.direction = direction;
        }

        public boolean moveForward() {
            Vector currentPos = position;
            Vector newPosition = position.add(direction);

            if (board.obstacleAt(newPosition)) {
                return false;
            } else {
                position = newPosition;
                board.markVisited(currentPos);
                return true;
            }
        }

        public void rotate90() {
            int x = direction.x, y = direction.y;
            direction = new Vector(-y, x);
        }

        public Vector getPosition() {
            return position;
        }

        public Vector getDirection() {
            return direction;
        }
    }

    private static class Vector {
        public int x, y;

        Vector(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vector add(Vector second) {
            return new Vector(this.x + second.x, this.y + second.y);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Vector vector = (Vector) o;
            return x == vector.x && y == vector.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
