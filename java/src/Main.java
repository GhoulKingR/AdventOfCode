public class Main {
    public static void main(String[] args) throws Exception {
        int day = -1, part = -1;
        Day[] days = getDays();

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

        if (day <= 0 || day > days.length)
            throw new Exception("Day " + day + " not available");

        days[day-1].runPart(part);
    }

    private static Day[] getDays() {
        return new Day[]{
            new Day1(), new Day2(),
            new Day3(), new Day4(),
            new Day5(), new Day6(),
            new Day7(), new Day8(),
        };
    }
}