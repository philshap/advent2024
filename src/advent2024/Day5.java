package advent2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day5 extends Day {
  protected Day5() {
    super(5);
  }

  record Update(Map<Integer, Integer> pages, List<Integer> rawPages) {
    static Update fromLine(String line) {
      var pages = Support.integers(line);
      var index = new HashMap<Integer, Integer>();
      for (int i = 0; i < pages.size(); i++) {
        index.put(pages.get(i), i);
      }
      return new Update(index, pages);
    }

    int middle() {
      return rawPages.get(rawPages.size() / 2);
    }

    boolean valid(List<Integer> rule) {
      return !pages.keySet().containsAll(rule) || (pages.get(rule.get(0)) < pages.get(rule.get(1)));
    }

    boolean allValid(List<List<Integer>> rules) {
      return rules.stream().allMatch(this::valid);
    }

    int fixMiddle(List<List<Integer>> rules) {
      var subRules = rules.stream().filter(rule -> pages.keySet().containsAll(rule)).collect(Collectors.toSet());
      var sorted = rawPages.stream().sorted((p1, p2) -> subRules.contains(List.of(p1, p2)) ? -1 : 1).toList();
      return sorted.get(sorted.size() / 2);
    }
  }

  @Override
  String part1() {
    String[] split = data.split("\n\n");
    List<List<Integer>> rules = Support.partition(Support.integers(split[0]), 2).toList();
    var total = Support.splitInput(split[1]).stream()
                       .map(Update::fromLine)
                       .filter(update -> update.allValid(rules))
                       .mapToInt(Update::middle)
                       .sum();
    return String.valueOf(total);
  }

  @Override
  String part2() {
    String[] split = data.split("\n\n");
    List<List<Integer>> rules = Support.partition(Support.integers(split[0]), 2).toList();
    var total = Support.splitInput(split[1]).stream()
                       .map(Update::fromLine)
                       .filter(update -> !update.allValid(rules))
                       .mapToInt(update -> update.fixMiddle(rules))
                       .sum();
    return String.valueOf(total);
  }

  public static void main(String[] args) {
    var day = new Day5() {
      @Override
      String getData() {
        return """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47""";
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
