package advent2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 extends Day {
  protected Day19() {
    super(19);
  }

  record Onsen(Set<String> towels, Map<String, Long> cache) {
    static Onsen fromInput(String input) {
      var towels = Arrays.stream(input.split(", ")).collect(Collectors.toSet());
      return new Onsen(towels, new HashMap<>());
    }

    long countMatches(String design) {
      if (design.isEmpty()) {
        return 1;
      }
      return IntStream.range(1, design.length() + 1)
                      .mapToObj(i -> {
                        // If the prefix is a match, return the tail so it can be matched.
                        if (towels.contains(design.substring(0, i))) {
                          return design.substring(i);
                        }
                        return null;
                      })
                      .filter(Objects::nonNull)
                      .mapToLong(this::memoCountMatches)
                      .sum();
    }

    boolean anyMatches(String design) {
      return countMatches(design) != 0;
    }

    long memoCountMatches(String design) {
      return Support.computeIfAbsent(cache, design, this::countMatches);
    }
  }

  @Override
  String part1() {
    var split = data.split("\n\n");
    var onsen = Onsen.fromInput(split[0]);
    var count = Support.splitInput(split[1]).stream().filter(onsen::anyMatches).count();
    return String.valueOf(count);
  }

  @Override
  String part2() {
    var split = data.split("\n\n");
    var onsen = Onsen.fromInput(split[0]);
    var count = Support.splitInput(split[1]).stream().mapToLong(onsen::countMatches).sum();
    return String.valueOf(count);
  }

  public static void main(String[] args) {
    var day = new Day19() {
      @Override
      String getData() {
        return """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb""";
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
