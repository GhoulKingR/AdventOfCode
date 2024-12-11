import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class Day2 extends Day {
    private static final String filePath = "data/day2/input.txt";

    @Override
    protected void part1() {
        try {
            List<String> lines = getFile(filePath);

            int count = 0;
            for (String lineStr : lines) {
                Line line = new Line(lineStr);
                if (line.isSafe()) {
                    count++;
                }
            }

            System.out.println(count);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private static class Line {
        private final int[] line;
        private final boolean ascending;

        Line(String line) {
            this.line = Arrays.stream(line.split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            int direction = 0;
            for (int i = 1; i < this.line.length; i++) {
                if (this.line[i-1] < this.line[i]) {
                    direction += 1;
                } else if (this.line[i-1] > this.line[i]) {
                    direction -= 1;
                }
            }
            ascending = direction > 0;
        }

        public boolean isSafe() {
            for (int i = 1; i < line.length; i++) {
                int sub = Math.abs(line[i - 1] - line[i]);

                if (sub > 3)
                    return  false;
                if (line[i - 1] >= line[i] && ascending)
                    return  false;
                if (line[i - 1] <= line[i] && !ascending)
                    return  false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int k : line) {
                result.append(k);
                result.append(" ");
            }
            return result.toString().trim();
        }
    }
}