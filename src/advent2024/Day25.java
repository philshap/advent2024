package advent2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day25 extends Day {
  protected Day25() {
    super(25);
  }

  record Heights(int[] heights, List<String> input) {
    static Heights fromInput(List<String> input) {
      int[] heights = {0, 0, 0, 0, 0};
      for (int y = 0; y < input.getFirst().length(); y++) {
        for (int x = 0; x < 5; x++) {
          if (input.get(y).charAt(x) == '#') {
            heights[x]++;
          }
        }
      }
      return new Heights(heights, input);
    }

    int max() {
      return input.getFirst().length();
    }

    boolean fit(Heights lock) {
      return heights[0] + lock.heights[0] <= max()
          && heights[1] + lock.heights[1] <= max()
          && heights[2] + lock.heights[2] <= max()
          && heights[3] + lock.heights[3] <= max()
          && heights[4] + lock.heights[4] <= max();
    }

    @Override
    public String toString() {
      return String.join("\n", input);
    }
  }

  @Override
  String part1() {
    List<Heights> keys = new ArrayList<>();
    List<Heights> locks = new ArrayList<>();
    Arrays.stream(data.split("\n\n"))
          .forEach(keyLock -> {
            var heights = Heights.fromInput(Support.splitInput(keyLock));
            if (keyLock.charAt(0) == '#') {
              locks.add(heights);
            } else {
              keys.add(heights);
            }
          });
    var total = keys.stream().flatMap(key -> locks.stream().filter(key::fit)).count();
    return String.valueOf(total);
  }

  @Override
  String part2() {
    return "";
  }

  public static void main(String[] args) {
    var day = new Day25() {
      @Override
      String getData() {
        return """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....

            #####
            ##.##
            .#.##
            ...##
            ...#.
            ...#.
            .....

            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####

            .....
            .....
            #.#..
            ###..
            ###.#
            ###.#
            #####

            .....
            .....
            .....
            #....
            #.#..
            #.#.#
            #####""";
      }
    };
    System.out.println(day.part1().equals("3"));
  }
}
