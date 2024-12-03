import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        int day = -1, part = -1;
        Map<Integer, Day> map = initDays();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--day":
                    day = Integer.parseInt(args[i + 1]);
                    i++;
                    break;
                case "--part":
                    part = Integer.parseInt(args[i + 1]);
                    i++;
                    break;
                default:
                    throw new Exception("Invalid option: " + args[i]);
            }
        }
        if (day == - 1 || part == -1)
            throw new Exception("You need to initialize both day and part");

        if (!map.containsKey(day))
            throw new Exception("Day " + day + " not available");

        map.get(day).runPart(part);
    }

    private static Map<Integer, Day> initDays() {
        Map<Integer, Day> map = new HashMap<>();
        map.put(3, Day3.getInstance());
        return map;
    }
}