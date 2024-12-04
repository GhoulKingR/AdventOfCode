import java.io.FileNotFoundException;
import java.util.List;

public class Day4 extends Day {
    private static final String filePath = "data/day4/input.txt";

    @Override
    protected void part1() {
        try {
            List<String> grid = getFile(filePath);
            
            Board board = new Board(grid);
            System.out.println("XMAX Count: " + board.countXMAS());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
    }

    @Override
    protected void part2() {
        try {
            List<String> grid = getFile(filePath);

            Board board = new Board(grid);
            System.out.println("XMAX Count: " + board.countAdvancedXmas());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
    }

    private enum Direction {
        Up, Down, Left, Right, None
    }

    private static class Board {
        private final List<String> grid;

        Board(List<String> board) {
            grid = board;
        }

        public int countXMAS() {
            int amount = 0;
            for (int i = 0; i < grid.size(); i++) {
                for (int j = 0; j < grid.get(i).length(); j++) {
                    amount += count(i, j);
                }
            }
            return amount;
        }

        private boolean canCheck(int i, int j, Direction direction) {
            // 4 is the size of "XMAS"
            if (direction == Direction.Up) {
                return i >= 3;
            } else if (direction == Direction.Down) {
                return  i <= grid.size() - 4;
            } else if (direction == Direction.Left) {
                return j >= 3;
            } else if (direction == Direction.Right){
                return j <= grid.getFirst().length() - 4;
            } else {
                return true;
            }
        }

        private int count(int i, int j) {
            boolean up =    canCheck(i, j, Direction.Up);
            boolean left =  canCheck(i, j, Direction.Left);
            boolean down =  canCheck(i, j, Direction.Down);
            boolean right = canCheck(i, j, Direction.Right);

            int count = 0;

            if (up)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i - 1).charAt(j) && 'A' == grid.get(i - 2).charAt(j) && 'S' == grid.get(i - 3).charAt(j))
                    count++;
            if (down)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i + 1).charAt(j) && 'A' == grid.get(i + 2).charAt(j) && 'S' == grid.get(i + 3).charAt(j))
                    count++;
            if (left)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i).charAt(j - 1) && 'A' == grid.get(i).charAt(j - 2) && 'S' == grid.get(i).charAt(j - 3))
                    count++;
            if (right)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i).charAt(j + 1) && 'A' == grid.get(i).charAt(j + 2) && 'S' == grid.get(i).charAt(j + 3))
                    count++;
            if (up && left)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i - 1).charAt(j - 1) && 'A' == grid.get(i - 2).charAt(j - 2) && 'S' == grid.get(i - 3).charAt(j - 3))
                    count++;
            if (up && right)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i - 1).charAt(j + 1) && 'A' == grid.get(i - 2).charAt(j + 2) && 'S' == grid.get(i - 3).charAt(j + 3))
                    count++;
            if (down && left)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i + 1).charAt(j - 1) && 'A' == grid.get(i + 2).charAt(j - 2) && 'S' == grid.get(i + 3).charAt(j - 3))
                    count++;
            if (down && right)
                if ('X' == grid.get(i).charAt(j) && 'M' == grid.get(i + 1).charAt(j + 1) && 'A' == grid.get(i + 2).charAt(j + 2) && 'S' == grid.get(i + 3).charAt(j + 3))
                    count++;

            return count;
        }

        private int countAdvancedXmas() {
            int count = 0;
            for (int i = 0; i < grid.size() - 2; i++) {
                for (int j = 0; j < grid.getFirst().length() - 2; j++) {
                    boolean diagLeftSAM = grid.get(i).charAt(j) == 'S' && grid.get(i+1).charAt(j+1) == 'A' && grid.get(i+2).charAt(j+2) == 'M';
                    boolean diagLeftMAS = grid.get(i).charAt(j) == 'M' && grid.get(i+1).charAt(j+1) == 'A' && grid.get(i+2).charAt(j+2) == 'S';
                    boolean diagRightSAM = grid.get(i).charAt(j+2) == 'S' && grid.get(i+1).charAt(j+1) == 'A' && grid.get(i+2).charAt(j) == 'M';
                    boolean diagRightMAS = grid.get(i).charAt(j+2) == 'M' && grid.get(i+1).charAt(j+1) == 'A' && grid.get(i+2).charAt(j) == 'S';

                    if ((diagLeftSAM || diagLeftMAS) && (diagRightMAS || diagRightSAM)) {
                        count++;
                    }
                }
            }
            return count;
        }
    }
}