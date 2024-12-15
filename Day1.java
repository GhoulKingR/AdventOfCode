import java.io.FileNotFoundException;
import java.util.*;

public class Day1 extends Day {
    private static final String filePath = "data/day1/input.txt";

    @Override
    protected void part1() {
        try {
            List<String> lines = getFile(filePath);

            Line first = new Line();
            Line second = new Line();

            for (String line : lines) {
                int[] nums = Arrays.stream(line.split(" {3}"))
                        .mapToInt(Integer::parseInt).toArray();

                first.addNum(nums[0]);
                second.addNum(nums[1]);
            }

            first.sort();
            second.sort();

            System.out.println("Total distance: " + first.totalDistance(second));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    @Override
    protected void part2() {
        try {
            List<String> lines = getFile(filePath);

            Line first = new Line();
            Line second = new Line();

            for (String line : lines) {
                int[] nums = Arrays.stream(line.split(" {3}"))
                        .mapToInt(Integer::parseInt).toArray();

                first.addNum(nums[0]);
                second.addNum(nums[1]);
            }

            System.out.println("Similarity : " + first.similarityScore(second));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
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
}
