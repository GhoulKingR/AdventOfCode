import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Day2 implements Day {
    private static final String dir = "data/day2";
    private static Optional<Day2> instance = Optional.empty();

    public static Day2 getInstance() {
        if (instance.isEmpty()) {
            Day2 newDay = new Day2();
            instance = Optional.of(newDay);
            return newDay;
        } else {
            return instance.get();
        }
    }

    @Override
    public void runPart(int part) {
        if (part > 2 || part < 1) {
            System.out.println("Part number has to be either 1 or 2");
            return;
        }

        if (part == 1) Part1.run();
        else Part2.run();
    }

    private static class Part2 {
        private static void run() {
            System.out.println("Part 2 not available");
        }
    }

    private static class Part1 {
        private static void run() {
            try {
                File file = new File(dir + "/input.txt");
                Scanner sc = new Scanner(file);

                int count = 0;
                while (sc.hasNextLine()) {
                    String lineStr = sc.nextLine();
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