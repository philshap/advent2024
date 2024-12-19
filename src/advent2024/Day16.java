package advent2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16 extends Day {
  protected Day16() {
    super(16);
  }

  record Path(Pos pos, Pos dir, int score, Path prev) {
    Path(Pos pos) {
      this(pos, Pos.R, 0, null);
    }
    Stream<Path> extend() {
      var t1 = Pos.TURN.get(dir);
      var t2 = Pos.TURN.get(dir).scale(new Pos(-1, -1));
      return Stream.of(new Path(pos.plus(dir), dir, score + 1, this),
                       new Path(pos.plus(t1), t1, score + 1001, this),
                       new Path(pos.plus(t2), t2, score + 1001, this));
    }

    // Use a linked list to avoid creating a new List for each Path.

    int length() {
      int length = 0;
      for (var head = this; head != null; head = head.prev) {
        length++;
      }
      return length;
    }

    boolean seen(Pos pos) {
      for (var head = this; head != null; head = head.prev) {
        if (head.pos.equals(pos)) {
          return true;
        }
      }
      return false;
    }

    List<Pos> seen() {
      List<Pos> seen = new ArrayList<>();
      for (var head = this; head != null; head = head.prev) {
        seen.add(head.pos);
      }
      return seen;
    }
  }

  record Maze(Map<Pos, Character> map, Pos start, Pos end) {
    static Maze fromInput(List<String> input) {
      var map = Pos.streamOf(input).collect(Collectors.toMap(Pair::l, Pair::r));
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
      map.put(start, '.');
      map.put(end, '.');
      return new Maze(map, start, end);
    }

    int lowPathScore() {
      var queue = new PriorityQueue<>(Comparator.comparingInt(Path::score));
      Set<Pos> seen = new HashSet<>();
      queue.add(new Path(start));
      while (!queue.isEmpty()) {
        var path = queue.poll();
        Pos pos = path.pos;
        if (pos.equals(end)) {
          return path.score;
        }
        if (seen.add(pos)) {
          path.extend()
              .filter(p -> map.get(p.pos) == '.' && !seen.contains(p.pos))
              .forEach(queue::add);
        }
      }
      return 0;
    }

    long lowPathLength() {
      int lowScore = 0;
      Map<Pos, Integer> steps = new HashMap<>();
      Set<Pos> shortest = new HashSet<>();
      var queue = new PriorityQueue<>(Comparator.comparingInt(Path::score));
      queue.add(new Path(start));
      while (!queue.isEmpty()) {
        var path = queue.poll();
        Pos pos = path.pos;
        if (pos.equals(end)) {
          if (lowScore == 0) {
            lowScore = path.score;
          }
          if (path.score == lowScore) {
            shortest.addAll(path.seen());
          } else {
            break;
          }
        }
        if (steps.computeIfAbsent(path.pos, p -> path.length()) == path.length()) {
          path.extend()
              .filter(p -> map.get(p.pos) == '.' && !path.seen(p.pos))
              .forEach(queue::add);
        }
      }
      return shortest.size();
    }
  }

  @Override
  String part1() {
    var maze = Maze.fromInput(input);
    return String.valueOf(maze.lowPathScore());
  }

  @Override
  String part2() {
    var maze = Maze.fromInput(input);
    return String.valueOf(maze.lowPathLength());
  }

  public static void main(String[] args) {
    var day = new Day16() {
      @Override
      String getData() {
        return """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################""";
      }
    };
    System.out.println(day.part1().equals("11048"));
    System.out.println(day.part2().equals("64"));
  }
}
