package advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day20 extends Day {
  static int minSaved = 100;

  Day20() {
    super(20);
  }

  record Track(Map<Pos, Character> map, Pos start, Pos end) {
    static Track fromInput(List<String> input) {
      var map = Pos.collectByPos(input);
      Pos start = null, end = null;
      for (var entry : map.entrySet()) {
        Pos pos = entry.getKey();
        char ch = entry.getValue();
        if (ch == 'S') {
          start = pos;
        } else if (ch == 'E') {
          end = pos;
        }
      }
      return new Track(map, start, end);
    }

    List<Pos> noCheatPath() {
      List<Pos> path = new ArrayList<>();
      Pos pos = start;
      while (true) {
        path.add(pos);
        if (pos.equals(end)) {
          break;
        }
        pos = Pos.DIRS.stream()
                      .map(pos::plus)
                      .filter(p -> !path.contains(p) && map.get(p) != '#')
                      // Track is not a maze; only one exit is possible.
                      .findFirst().orElseThrow();
      }
      return path;
    }

    int distance(Pos p1, Pos p2) {
      Pos difference = p1.minus(p2);
      return Math.abs(difference.x()) + Math.abs(difference.y());
    }

    // Consider two positions on the path; for each position that's at least minSaved steps
    // ahead, see if its manhattan distance is within the allowed cheat size, and if the cost
    // savings is large enough after taking the distance into account.
    int cheatCount(int cheatSize) {
      var path = noCheatPath();
      int count = 0;
      for (int i = 0; i < path.size() - minSaved; i++) {
        for (int j = i + minSaved; j < path.size(); j++) {
          int cost = distance(path.get(i), path.get(j));
          if (cost <= cheatSize && ((j - i) - cost) >= minSaved) {
            count++;
          }
        }
      }
      return count;
    }
  }

@Override
  String part1() {
    return String.valueOf(Track.fromInput(input).cheatCount(2));
  }

  @Override
  String part2() {
    return String.valueOf(Track.fromInput(input).cheatCount(20));
  }

  public static void main(String[] args) {
    var day = new Day20() {
      @Override
      String getData() {
        return """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############""";
      }
    };
    minSaved = 1;
    System.out.println(day.part1().equals("44"));
    minSaved = 50;
    System.out.println(day.part2().equals("285"));
  }
}
