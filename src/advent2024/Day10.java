package advent2024;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day10 extends Day {
  protected Day10() {
    super(10);
  }

  record TopoMap(Map<Pos, Integer> topo, boolean shared) {
    static TopoMap fromInput(List<String> input, boolean shared) {
      return new TopoMap(Pos.collectByPos(input, ch -> ch - '0'), shared);
    }

    Pair<List<Pos>, Integer> extend(Pair<List<Pos>, Integer> pair) {
      return Pair.of(pair.l().stream()
                         .flatMap(pos -> Pos.DIRS.stream().map(pos::plus))
                         .filter(pos -> topo.getOrDefault(pos, -1) == pair.r())
                         .toList(),
                     pair.r() + 1);
    }

    int countTrails(Pos trailHead) {
      var trails = Stream.iterate(Pair.of(List.of(trailHead), 1), this::extend)
                         .dropWhile(pair -> pair.r() != 10)
                         .findFirst().orElseThrow().l();
      return shared ? trails.size() : Set.copyOf(trails).size();
    }

    int countTrails() {
      return topo.entrySet().stream()
                 .filter(entry -> entry.getValue() == 0)
                 .map(Map.Entry::getKey)
                 .mapToInt(this::countTrails)
                 .sum();
    }
  }

  @Override
  String part1() {
    return String.valueOf(TopoMap.fromInput(input, false).countTrails());
  }

  @Override
  String part2() {
    return String.valueOf(TopoMap.fromInput(input, true).countTrails());
  }

  public static void main(String[] args) {
    var day = new Day10() {
      @Override
      String getData() {
        return """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732""";
      }
    };
    System.out.println(day.part1().equals("36"));
    System.out.println(day.part2().equals("81"));
  }
}
