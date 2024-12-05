import java.io.FileNotFoundException;
import java.util.*;

public class Day5 extends Day {
    private static final String filePath1 = "data/day5/input1.txt";
    private static final String filePath2 = "data/day5/input2.txt";

    @Override
    protected void part1() {
        try {
            Rules rules = new Rules(getFile(filePath1));
            Update[] updates = Update.getUpdates(getFile(filePath2));

            int count = 0;
            for (Update update : updates) {
                if (update.follows(rules)) {
                    count += update.getMiddlePageNumber();
                }
            }

            System.out.println("Count: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    private static class Rules {
        public Map<Integer, Set<Integer>> orders = new HashMap<>();

        Rules(List<String> rules) {
            for (String rule: rules) {
                int[] parts = Arrays.stream(rule.split("\\|")).mapToInt(Integer::parseInt).toArray();
                if (orders.containsKey(parts[0])) {
                    orders.get(parts[0]).add(parts[1]);
                } else {
                    Set<Integer> newList = new HashSet<>();
                    newList.add(parts[1]);
                    orders.put(parts[0], newList);
                }
            }
        }
    }

    private static class Update {
        private final int[] elements;

        Update(String update) {
            elements = Arrays.stream(update.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
        }

        public static Update[] getUpdates(List<String> updates) {
            int count = updates.size();
            Update[] result = new Update[count];
            for (int i = 0; i < count; i++) {
                String update = updates.get(i);
                result[i] = new Update(update);
            }
            return result;
        }

        public boolean follows(Rules rules) {
            for (int i = 0; i < elements.length; i++) {
                if (rules.orders.containsKey(elements[i])) {
                    for (int j = i-1; j >= 0; j--) {
                        if (rules.orders.get(elements[i]).contains(elements[j])) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public int getMiddlePageNumber() {
            return elements[Math.floorDiv(elements.length, 2)];
        }
    }
}

// Puzzle input: "Page ordering rules" and the "pages to produce in each update"