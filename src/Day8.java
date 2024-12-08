import java.io.FileNotFoundException;
import java.util.*;

public class Day8 extends Day {
    String filePath = "data/day8/input.txt";

    @Override
    protected void part1() {
        try{
            List<String> lines = getFile(filePath);
            BoardMap map = new BoardMap(lines);
            System.out.println("Unique antinodes: " + map.antiNodeCount());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    @Override
    protected void part2() {
        try{
            List<String> lines = getFile(filePath);
            BoardMap map = new BoardMap(lines);
            System.out.println("Unique antinodes: " + map.updatedAntiNodeCount());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private static class BoardMap {
        Map<Character, List<Vector>> antennas = new HashMap<>();
        Vector size;


        BoardMap(List<String> map) {
            int height = map.size();
            int width = map.getFirst().length();
            size = new Vector(width, height);

            for (int y = 0; y < height; y++) {
                String line = map.get(y);

                for (int x = 0; x < width; x++) {
                    char freq = line.charAt(x);
                    if (freq == '.') continue;

                    if (antennas.containsKey(freq)) {
                        antennas.get(freq).add(new Vector(x, y));
                    } else {
                        List<Vector> newList = new ArrayList<>();
                        newList.add(new Vector(x, y));
                        antennas.put(freq, newList);
                    }
                }
            }
        }

        public int antiNodeCount() {
            Set<Vector> antiNodes = new HashSet<>();

            for (List<Vector> positions : antennas.values()) {
                for (int i = 0; i < positions.size(); i++) {
                    for (int j = i+1; j < positions.size(); j++) {
                        antiNodes.addAll(getAntiNodesNotOutOfBounds(positions.get(i), positions.get(j)));
                    }
                }
            }

            return antiNodes.size();
        }

        public int updatedAntiNodeCount() {
            Set<Vector> antiNodes = new HashSet<>();

            for (List<Vector> positions : antennas.values()) {
                for (int i = 0; i < positions.size(); i++) {
                    for (int j = i+1; j < positions.size(); j++) {
                        antiNodes.addAll(getNewValidAntiNodes(positions.get(i), positions.get(j)));
                    }
                }
            }

            return antiNodes.size();
        }

        private List<Vector> getAntiNodesNotOutOfBounds(Vector antenna1, Vector antenna2) {
            List<Vector> antiNodes = new ArrayList<>();

            Vector antiNode1 = antenna1.plus(antenna1.minus(antenna2));
            Vector antiNode2 = antenna2.plus(antenna2.minus(antenna1));

            if (antiNode1.inRange(Vector.ZERO, size.minus(1)))
                antiNodes.add(antiNode1);
            if (antiNode2.inRange(Vector.ZERO, size.minus(1)))
                antiNodes.add(antiNode2);

            return antiNodes;
        }

        private List<Vector> getNewValidAntiNodes(Vector antenna1, Vector antenna2) {
            List<Vector> antiNodes = new ArrayList<>();

            Vector displacement = antenna1.minus(antenna2);
            Vector antiNode = antenna1;
            while (antiNode.inRange(Vector.ZERO, size.minus(1))) {
                antiNodes.add(antiNode);
                antiNode = antiNode.plus(displacement);
            }

            displacement = antenna2.minus(antenna1);
            antiNode = antenna2;
            while (antiNode.inRange(Vector.ZERO, size.minus(1))) {
                antiNodes.add(antiNode);
                antiNode = antiNode.plus(displacement);
            }

            return antiNodes;
        }
    }

    private record Vector(int x, int y) {
        public static final Vector ZERO = new Vector(0, 0);

        public Vector minus(Vector second) {
            int xReturn = x - second.x;
            int yReturn = y - second.y;
            return new Vector(xReturn, yReturn);
        }

        public Vector plus(Vector second) {
            int xReturn = x + second.x;
            int yReturn = y + second.y;
            return new Vector(xReturn, yReturn);
        }

        public Vector minus(int scalar) {
            int xReturn = x - scalar;
            int yReturn = y - scalar;
            return new Vector(xReturn, yReturn);
        }

        public boolean inRange(Vector boundary1, Vector boundary2) {
            if (y < boundary1.y) return false;
            if (x < boundary1.x) return false;
            if (y > boundary2.y) return false;
            if (x > boundary2.x) return false;

            return true;
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
