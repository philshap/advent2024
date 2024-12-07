package advent2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day6 extends Day {
  protected Day6() {
    super(6);
  }

  enum Dir {
    U(0, -1),
    R(1, 0),
    D(0, 1),
    L(-1, 0);
    final int dx;
    final int dy;

    Dir(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }
    Dir turn() {
      return switch (this) {
        case U -> R;
        case R -> D;
        case D -> L;
        case L -> U;
      };
    }
  }

  record Pos(int x, int y) { }

  record Guard(Pos pos, Dir dir) {
    Guard move() {
      return new Guard(new Pos(pos.x + dir.dx, pos.y + dir.dy), dir);
    }
    Guard turn() {
      return new Guard(pos, dir.turn());
    }
  }

  static Stream<Pos> posStream(List<String> input) {
    return IntStream.range(0, input.getFirst().length())
                    .boxed()
                    .flatMap(x -> IntStream.range(0, input.size())
                                           .mapToObj(y -> new Pos(x, y)));
  }

  record Lab(Map<Pos, Character> map, Guard guard) {
    static Lab fromInput(List<String> input) {
      var map = posStream(input)
          .collect(Collectors.toMap(Function.identity(), pos -> input.get(pos.y).charAt(pos.x)));
      Guard guard = null;
      for (var entry : map.entrySet()) {
        if (entry.getValue() == '^') {
          map.put(entry.getKey(), '.');
          guard = new Guard(entry.getKey(), Dir.U);
          break;
        }
      }
      return new Lab(map, guard);
    }

    int patrolLength() {
      Set<Pos> seen = new HashSet<>();

      Guard patrol = guard;
      while (true) {
        seen.add(patrol.pos);
        Guard next = patrol.move();
        Character peek = map.getOrDefault(next.pos, '*');
        if (peek == '*') {
          break;
        }
        if (peek == '#') {
          patrol = patrol.turn();
        } else {
          patrol = next;
        }
      }
      return seen.size();
    }

    boolean isLoop(Pos block) {
      if (map.get(block) != '.') {
        return false;
      }
      Set<Guard> seen = new HashSet<>();
      Guard patrol = guard;
      while (true) {
        if (!seen.add(patrol)) {
          return true;
        }
        Guard next = patrol.move();
        var peek = next.pos.equals(block) ? '#' : map.getOrDefault(next.pos, '*');
        if (peek == '*') {
          return false;
        }
        if (peek == '#') {
          patrol = patrol.turn();
        } else {
          patrol = next;
        }
      }
    }
  }

  @Override
  String part1() {
    return String.valueOf(Lab.fromInput(input).patrolLength());
  }

  @Override
  String part2() {
    Lab lab = Lab.fromInput(input);
    long count = posStream(input).parallel().filter(lab::isLoop).count();
    return String.valueOf(count);
  }

  public static void main(String[] args) {
    var day = new Day6() {
      @Override
      String getData() {
        return """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...""";
      }
    };
    System.out.println(day.part1().equals("41"));
    System.out.println(day.part2().equals("6"));
  }
}
