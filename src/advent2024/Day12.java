package advent2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class Day12 extends Day {
  protected Day12() {
    super(12);
  }

  record Plot(Pos pos, int sides, int corners) {
  }

  static boolean match(Map<Pos, Character> plots, Pos pos, Character c) {
    return plots.getOrDefault(pos, '*') == c;
  }

  record Region(List<Plot> plots) {
    static Region fromPos(Map<Pos, Character> allPlots, Pos start, Set<Pos> seen) {
      char ch = allPlots.get(start);
      Function<Pos, Boolean> matcher = pos -> match(allPlots, pos, ch);
      List<Pos> queue = List.of(start);
      Region region = new Region(new ArrayList<>());
      while (!queue.isEmpty()) {
        List<Pos> next = new ArrayList<>();
        for (var pos : queue) {
          if (seen.add(pos)) {
            var sides = Pos.DIRS.stream()
                                .map(pos::plus)
                                .filter(matcher::apply)
                                .toList();
            region.plots.add(new Plot(pos, 4 - sides.size(), corners(pos, matcher)));
            sides.stream().filter(Predicate.not(seen::contains)).forEach(next::add);
          }
        }
        queue = next;
      }
      return region;
    }

    static Pos[][] CORNERS = {{Pos.U, Pos.L}, {Pos.U, Pos.R}, {Pos.D, Pos.R}, {Pos.D, Pos.L}};

    private static int corners(Pos pos, Function<Pos, Boolean> matcher) {
      return (int) Arrays.stream(CORNERS)
                          .filter(corner -> {
                            // A corner is either AB  OR  AA
                            //                    B.      AB
                            Pos p1 = pos.plus(corner[0]), p2 = pos.plus(corner[1]), p3 = pos.plus(corner[0]).plus(corner[1]);
                            return !matcher.apply(p1) && !matcher.apply(p2)
                                || matcher.apply(p1) && matcher.apply(p2) && !matcher.apply(p3);
                          })
                          .count();
    }

    int cost(ToIntFunction<Plot> countFences) {
      return plots.size() * plots.stream().mapToInt(countFences).sum();
    }
  }

  int totalCost(ToIntFunction<Plot> countFences) {
    var plots = Pos.collectByPos(input);
    Set<Pos> seen = new HashSet<>();
    return plots.keySet().stream()
                .map(pos -> Region.fromPos(plots, pos, seen))
                .mapToInt(region -> region.cost(countFences)).sum();
  }

  @Override
  String part1() {
    return String.valueOf(totalCost(Plot::sides));
  }

  @Override
  String part2() {
    return String.valueOf(totalCost(Plot::corners));
  }

  public static void main(String[] args) {
    var day = new Day12() {
      @Override
      String getData() {
        return """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE""";
      }
    };
    System.out.println(day.part1().equals("1930"));
    System.out.println(day.part2().equals("1206"));
  }
}
