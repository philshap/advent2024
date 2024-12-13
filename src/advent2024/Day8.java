package advent2024;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day8 extends Day {
  protected Day8() {
    super(8);
  }

  record City(int height, int width, Collection<List<Pos>> antennaGroups) {
    static City fromInput(List<String> input) {
      Map<Character, List<Pos>> groups = new HashMap<>();
      Pos.streamOf(input)
          .forEach(pair -> {
            char ch = pair.r();
            if (ch != '.') {
              groups.computeIfAbsent(ch, ArrayList::new).add(pair.l());
            }
          });
      return new City(input.size(), input.getFirst().length(), groups.values());
    }

    boolean valid(Pos pos) {
      return 0 <= pos.x() && pos.x() < width && 0 <= pos.y() && pos.y() < height;
    }

    Stream<Pos> extend1(List<Pos> pair) {
      Pos p1 = pair.getFirst(), p2 = pair.getLast();
      Pos delta = p1.minus(p2);
      return Stream.of(p1.plus(delta), p2.minus(delta)).filter(this::valid);
    }

    long countAntiNodes(Function<List<Pos>, Stream<Pos>> extend) {
      return
          antennaGroups.stream()
                       .flatMap(positions ->
                                    Support.combinations(positions, 2)
                                           .flatMap(extend))
                       .distinct()
                       .count();
    }

    Stream<Pos> extend2(List<Pos> pair) {
      Pos p1 = pair.getFirst(), p2 = pair.getLast();
      List<Pos> anti = new ArrayList<>();
      Pos delta = p1.minus(p2);
      while (valid(p1)) {
        anti.add(p1);
        p1 = p1.plus(delta);
      }
      while (valid(p2)) {
        anti.add(p2);
        p2 = p2.minus(delta);
      }
      return anti.stream();
    }
  }

  @Override
  String part1() {
    City city = City.fromInput(input);
    return String.valueOf(city.countAntiNodes(city::extend1));
  }

  @Override
  String part2() {
    City city = City.fromInput(input);
    return String.valueOf(city.countAntiNodes(city::extend2));
  }

  public static void main(String[] args) {
    var day = new Day8() {
      @Override
      String getData() {
        return """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............""";
      }
    };
    System.out.println(day.part1().equals("14"));
    System.out.println(day.part2().equals("34"));
  }
}
