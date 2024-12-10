package advent2024;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Day6 extends Day {
  protected Day6() {
    super(6);
  }

  record Guard(Pos pos, Pos dir) {
    Guard move() {
      return new Guard(pos.plus(dir), dir);
    }
    Guard turn() {
      return new Guard(pos, Pos.TURN.get(dir));
    }
  }

  record Lab(Map<Pos, Character> map, Guard guard) {
    static Lab fromInput(List<String> input) {
      var map = Pos.collectByPos(input, Function.identity());
      Guard guard = null;
      for (var entry : map.entrySet()) {
        if (entry.getValue() == '^') {
          map.put(entry.getKey(), '.');
          guard = new Guard(entry.getKey(), Pos.U);
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
    long count = Pos.posStream(input).map(Pair::l).parallel().filter(lab::isLoop).count();
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
