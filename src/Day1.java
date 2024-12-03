import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day1 implements Day {
    private static final String dir = "data/day1";
    private static Optional<Day1> instance = Optional.empty();

    public static Day1 getInstance() {
        if (instance.isEmpty()) {
            Day1 newDay = new Day1();
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

    private static class Part1 {
        private static final String filePath = dir + "/input.txt";

        private static void run() {
            try {
                File file = new File(filePath);
                Scanner scanner = new Scanner(file);

                Line first = new Line();
                Line second = new Line();

                while (scanner.hasNextLine()) {
                    int[] nums = Arrays.stream(
                            scanner.nextLine().split(" {3}")
                    ).mapToInt(Integer::parseInt).toArray();

                    first.addNum(nums[0]);
                    second.addNum(nums[1]);
                }

                first.sort();
                second.sort();

                System.out.println("Total distance: " + first.totalDistance(second));
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filePath);
            }
        }
    }

    private static class Line {
        private final List<Integer> nums = new ArrayList<>();

        public void addNum(int num) {
            nums.add(num);
        }

        public void sort() {
            Collections.sort(nums);
        }

        public int totalDistance(Line second) {
            int distance = 0;
            for (int i = 0; i < nums.size(); i++) {
                distance += Math.abs(nums.get(i) - second.nums.get(i));
            }
            return distance;
        }

        public int similarityScore(Line second) {
            Map<Integer, Integer> appearence = second.getAppearenceMap();
            int total = 0;
            for (int num : nums) {
                total += num * appearence.getOrDefault(num, 0);
            }
            return total;
        }

        private Map<Integer, Integer> getAppearenceMap() {
            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
            return map;
        }
    }

    private static class Part2 {
        private static final String filePath = dir + "/input.txt";

        private static void run() {
            try {
                File file = new File(filePath);
                Scanner scanner = new Scanner(file);

                Line first = new Line();
                Line second = new Line();

                while (scanner.hasNextLine()) {
                    int[] nums = Arrays.stream(
                            scanner.nextLine().split(" {3}")
                    ).mapToInt(Integer::parseInt).toArray();

                    first.addNum(nums[0]);
                    second.addNum(nums[1]);
                }

                System.out.println("Similarity : " + first.similarityScore(second));
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filePath);
            }
        }
    }
}
