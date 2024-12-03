import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 implements Day {
    private static final String dir = "data/day3";
    private static Optional<Day3> instance = Optional.empty();

    public static Day3 getInstance() {
        if (instance.isEmpty()) {
            Day3 newDay = new Day3();
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
        private static final String file = dir + "/input.txt";

        public static void run() {
            try {
                File fileObj = new File(file);
                Scanner sc = new Scanner(fileObj);
                StringBuilder sb = new StringBuilder();
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                List<Mul> muls = Mul.findAll(sb.toString());
                int total = 0;
                for (Mul mul : muls) {
                    total += mul.calculate();
                }
                System.out.println("Total: " + total);
                System.out.println("Muls: " + muls);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + file);
                return;
            }
        }
    }


    private static class Mul {
        int[] args;
        String command;

        Mul(String commandStr) {
            command = commandStr;
            int endIndex = commandStr.length() - 1;
            int startIndex = 4;
            String sub = commandStr.substring(startIndex, endIndex);
            args = Arrays.stream(sub.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        @Override
        public String toString() {
            return command;
        }

        public int calculate() {
            int total = 1;
            for (int arg: args) {
                total *= arg;
            }
            return total;
        }

        public static List<Mul> findAll(String str) {
            List<Mul> muls = new ArrayList<>();
            Pattern p = Pattern.compile("mul\\(\\d+,\\d+\\)");
            Matcher m = p.matcher(str);

            while (m.find()) {
                String commandStr = str.substring(m.start(), m.end());
                muls.add(new Mul(commandStr));
            }

            return muls;
        }

        public static List<Mul> findAllAdvanced(String str) {
            List<Mul> muls = new ArrayList<>();
            Pattern p = Pattern.compile("(mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\))");
            Matcher m = p.matcher(str);

            boolean ignore = false;
            while (m.find()) {
                String commandStr = str.substring(m.start(), m.end());
                if (commandStr.equals("don't()")) {
                    ignore = true;
                    continue;
                } else if(commandStr.equals("do()")) {
                    ignore = false;
                    continue;
                }

                if (!ignore) muls.add(new Mul(commandStr));
            }
            return muls;
        }
    }

    private static class Part2 {
        private static final String file = dir + "/input.txt";

        public static void run() {
            try {
                File fileObj = new File(file);
                Scanner sc = new Scanner(fileObj);
                StringBuilder sb = new StringBuilder();
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                List<Mul> muls = Mul.findAllAdvanced(sb.toString());
                int total = 0;
                for (Mul mul : muls) {
                    total += mul.calculate();
                }
                System.out.println("Total: " + total);
                System.out.println("Muls: " + muls);
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + file);
                return;
            }
        }
    }
}
