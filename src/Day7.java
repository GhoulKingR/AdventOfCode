import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class Day7 extends Day {
    String filePath = "data/day7/input.txt";

    @Override
    protected void part1() {
        try{
            List<String> lines = getFile(filePath);
            long total = 0;
            for (String line : lines) {
                TestCase testCase = new TestCase(line);
                if (testCase.isTrue()) {
                    total += testCase.getTestNum();
                }
            }
            System.out.println("Total true cases: " + total);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return;
        }
    }

    @Override
    protected void part2() {
        try{
            List<String> lines = getFile(filePath);
            long total = 0;
            for (String line : lines) {
                TestCase testCase = new TestCase(line);
                if (testCase.isTrue3()) {
                    total += testCase.getTestNum();
                }
            }
            System.out.println("Total true cases: " + total);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return;
        }
    }

    private static class TestCase {
        private final long testNum;
        private final int[] testNums;

        TestCase(String testCase) {
            String[] nums = testCase.split(": ");
            testNum = Long.parseLong(nums[0]);
            testNums = Arrays.stream(nums[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        }

        @Override
        public String toString() {
            return "{testNum: " + testNum + " nums: " + Arrays.toString(testNums) + "}";
        }

        public boolean isTrue() {
            int len = testNums.length - 1;
            int permutation = (int) Math.pow(2, len);
            for (int i = 0; i < permutation; i++) {
                int keepingTrack = i;
                long totalNum = testNums[0];
                for (int j = 1; j < len + 1 ; j++) {
                    int operation = keepingTrack % 2;
                    keepingTrack /= 2;

                    if (operation == 0) {
                        totalNum *= testNums[j];
                    } else {
                        totalNum += testNums[j];
                    }
                }

                if (totalNum == testNum) {
                    return true;
                }
            }
            return false;
        }

        public boolean isTrue3() {
            int len = testNums.length - 1;
            long permutation = (long) Math.pow(3, len);
            for (long i = 0; i < permutation; i++) {
                long keepingTrack = i;
                long totalNum = testNums[0];
                for (int j = 1; j < len + 1 ; j++) {
                    int operation = (int) keepingTrack % 3;
                    keepingTrack /= 3;

                    if (operation == 0) {
                        totalNum *= testNums[j];
                    } else if (operation == 1) {
                        totalNum += testNums[j];
                    } else {
                        totalNum = Long.parseLong(String.valueOf(totalNum) + String.valueOf(testNums[j]));
                    }
                }

                if (totalNum == testNum) {
                    return true;
                }
            }
            return false;
        }

        public long getTestNum() {
            return testNum;
        }
    }
}
