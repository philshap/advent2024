package advent2024;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 extends Day {
  final int height;
  final int width;

  Day14() {
    this(103, 101);
  }

  Day14(int height, int width) {
    super(14);
    this.height = height;
    this.width = width;
  }

  Pos wrap(Pos pos) {
    int x = pos.x();
    int y = pos.y();
    if (x < 0) {
      x += width;
    } else if (x >= width) {
      x -= width;
    }
    if (y < 0) {
      y += height;
    } else if (y >= height) {
      y -= height;
    }
    return new Pos(x, y);
  }

  int posQuadrant(Pos pos) {
    if (pos.within(0, width / 2, 0, height / 2)) {
      return 1;
    }
    if (pos.within(width / 2 + 1, width, 0, height / 2)) {
      return 2;
    }
    if (pos.within(0, width / 2, height / 2 + 1, height)) {
      return 3;
    }
    if (pos.within(width / 2 + 1, width, height / 2 + 1, height)) {
      return 4;
    }
    return 0;
  }

  class Bot {
    private final Pos pos;
    private final Pos velocity;

    Bot(Pos pos, Pos velocity) {
      this.pos = pos;
      this.velocity = velocity;
    }

    Bot(String line) {
      var ints = Support.integers(line);
      pos = new Pos(ints.get(0), ints.get(1));
      velocity = new Pos(ints.get(2), ints.get(3));
    }

    Bot move() {
      return new Bot(wrap(pos.plus(velocity)), velocity);
    }

    int quadrant() {
      return posQuadrant(pos);
    }
  }

  List<Bot> moveBots(List<Bot> bots) {
    return bots.stream().map(Bot::move).toList();
  }

  @Override
  String part1() {
    List<Bot> bots = input.stream().map(Bot::new).toList();
    var score = Stream.iterate(bots, this::moveBots)
                      .skip(100)
                      // Generate a stream over the 100th iteration of bots.
                      .findFirst().orElseThrow().stream()
                      // Organize by quadrant, dropping bots not in any quadrant.
                      .map(Bot::quadrant)
                      .filter(quadrant -> quadrant != 0)
                      .collect(Collectors.groupingBy(Function.identity()))
                      // Score is number of bots in all quadrants multiplied by each other.
                      .values().stream()
                      .mapToInt(List::size)
                      .reduce(1, (i, j) -> i * j);
    return String.valueOf(score);
  }

  int maxConnected(List<Bot> bots) {
    var allBots = bots.stream()
                      .map(bot -> bot.pos)
                      .collect(Collectors.toCollection(LinkedHashSet::new));
    int maxConnected = 0;
    while (!allBots.isEmpty()) {
      List<Pos> region = new ArrayList<>();
      List<Pos> queue = List.of(allBots.removeFirst());
      while (!queue.isEmpty()) {
        List<Pos> next = new ArrayList<>();
        for (var pos : queue) {
          Pos.DIRS.stream().map(pos::plus).filter(allBots::remove).forEach(p -> {
            next.add(p);
            region.add(p);
          });
        }
        queue = next;
      }
      maxConnected = Math.max(region.size(), maxConnected);
    }
    return maxConnected;
  }

  @SuppressWarnings("unused")
  void print(List<Bot> bots) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        Pos p = new Pos(x, y);
        long count = bots.stream().filter(bot -> bot.pos.equals(p)).count();
        if (count != 0) {
          System.out.print(count);
        } else {
          System.out.print('.');
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  @Override
  String part2() {
    List<Bot> bots = input.stream().map(Bot::new).toList();
    int iter = 0;
    while (maxConnected(bots) <= 100) {
      bots = moveBots(bots);
      iter++;
    }
//    print(bots);
    return String.valueOf(iter);
  }

  public static void main(String[] args) {
    var day = new Day14(7, 11) {
      @Override
      String getData() {
        return """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3""";
      }
    };
    System.out.println(day.part1().equals("12"));
     System.out.println(new Day14().part2());
  }
}
