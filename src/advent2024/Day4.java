package advent2024;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Day4 extends Day {

  protected Day4() {
    super(4);
  }

  char getChar(int i, int j) {
    if (0 <= j && j < input.size()) {
      var line = input.get(j);
      if (0 <= i && i < line.length()) {
        return line.charAt(i);
      }
    }
    return 0;
  }

  static int[][] DIRS = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};

  boolean matchWord(int i, int j, int[] delta, String word) {
    for (char ch : word.toCharArray()) {
      i += delta[0];
      j += delta[1];
      if (getChar(i, j) != ch) {
        return false;
      }
    }
    return true;
  }

  int countMatches(int i, int j) {
    if (getChar(i, j) == 'X') {
      return (int) Arrays.stream(DIRS).filter(delta -> matchWord(i, j, delta, "MAS")).count();
    }
    return 0;
  }

  int total(BiFunction<Integer, Integer, Integer> counter) {
    return IntStream.range(0, input.size())
                    .flatMap(j -> IntStream.range(0, input.getFirst().length())
                                           .map(i -> counter.apply(i, j)))
                    .sum();
  }

  @Override
  String part1() {
    return String.valueOf(total(this::countMatches));
  }

  static List<String> MS = List.of("MMSS", "SSMM", "MSMS", "SMSM");

  int countMatches2(int i, int j) {
    if (getChar(i, j) == 'A') {
      for (String ms : MS) {
        if (getChar(i - 1, j - 1) == ms.charAt(0) &&
            getChar(i - 1, j + 1) == ms.charAt(1) &&
            getChar(i + 1, j - 1) == ms.charAt(2) &&
            getChar(i + 1, j + 1) == ms.charAt(3)) {
          return 1;
        }
      }
    }
    return 0;
  }

  @Override
  String part2() {
    return String.valueOf(total(this::countMatches2));
  }

  public static void main(String[] args) {
    var day = new Day4() {
      @Override
      String getData() {
        return """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX""";
      }
    };
    System.out.println(day.part1().equals("18"));
    System.out.println(day.part2().equals("9"));
  }
}
