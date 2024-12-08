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

  record Pos(int x, int y) {
    Pos shift(int dx, int dy) {
      return new Pos(x + dx, y + dy);
    }
  }

  record City(int height, int width, Collection<List<Pos>> antennaGroups) {
    static City fromInput(List<String> input) {
      int width = input.getFirst().length();
      int height = input.size();
      Map<Character, List<Pos>> groups = new HashMap<>();
      for (int y = 0; y < height; y++) {
        String line = input.get(y);
        for (int x = 0; x < width; x++) {
          char ch = line.charAt(x);
          if (ch != '.') {
            groups.computeIfAbsent(ch, ArrayList::new).add(new Pos(x, y));
          }
        }
      }
      return new City(height, width, groups.values());
    }

    boolean valid(Pos pos) {
      return 0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height;
    }

    Stream<Pos> extend1(List<Pos> pair) {
      Pos p1 = pair.getFirst(), p2 = pair.getLast();
      int dx = p1.x - p2.x;
      int dy = p1.y - p2.y;
      return Stream.of(p1.shift(dx, dy), p2.shift(-dx, -dy)).filter(this::valid);
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
      int dx = p1.x - p2.x;
      int dy = p1.y - p2.y;
      while (valid(p1)) {
        anti.add(p1);
        p1 = p1.shift(dx, dy);
      }
      while (valid(p2)) {
        anti.add(p2);
        p2 = p2.shift(-dx, -dy);
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
