import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day {
    public void runPart(int part) {
        if (part > 2 || part < 1) {
            System.out.println("Part number has to be either 1 or 2");
            return;
        }

        if (part == 1) part1();
        else part2();
    }

    protected List<String> getFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        List<String> result = new ArrayList<>();

        while (scanner.hasNextLine()) {
            result.add(scanner.nextLine());
        }

        return result;
    }

    protected void part1() {
        System.out.println("Part 1 not available");
    }

    protected void part2() {
        System.out.println("Part 2 not available");
    }
}
