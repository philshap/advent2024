package advent2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Day15 extends Day {
  protected Day15() {
    super(15);
  }

  record Box(Pos p1, Pos p2) {
    public boolean atPos(Pos pos) {
      return p1.equals(pos) || p2.equals(pos);
    }

    public Box move(Pos move) {
      return new Box(p1.plus(move), p2.plus(move));
    }
  }

  record Warehouse(Set<Pos> walls, List<Pos> moves, Pos bot, List<Box> boxes) {
    static Warehouse fromData(String data, boolean wide) {
      String[] split = data.split("\n\n");
      List<Box> boxes = new ArrayList<>();
      Set<Pos> walls = new HashSet<>();
      var bot = new AtomicReference<Pos>();
      Pos.streamOf(Support.splitInput(split[0]))
          .forEach(pair -> {
            Pos pos = wide ? pair.l().scale(new Pos(2, 1)) : pair.l();
            Pos nextPos = wide ? pos.plus(Pos.R) : pos;
            switch (pair.r()) {
              case '#' -> {
                walls.add(pos);
                walls.add(nextPos);
              }
              case 'O' -> boxes.add(new Box(pos, nextPos));
              case '@' -> bot.set(pos);
            }
          });
      var moves = split[1].chars().mapToObj(ch -> Pos.CHAR_DIR.get((char) ch)).filter(Objects::nonNull).toList();
      return new Warehouse(walls, moves, bot.get(), boxes);
    }

    @SuppressWarnings("unused")
    void draw() {
      int maxX = walls.stream().mapToInt(Pos::x).max().orElseThrow();
      int maxY = walls.stream().mapToInt(Pos::y).max().orElseThrow();
      for (int y = 0; y <= maxY; y++) {
        for (int x = 0; x <= maxX; x++) {
          Pos pos = new Pos(x, y);
          char ch = '.';
          if (pos.equals(bot)) {
            ch = !moves.isEmpty() ? Pos.DIR_CHAR.get(moves.getFirst()) : '@';
          } else if (walls.contains(pos)) {
            ch = '#';
          } else {
            Box box = boxAt(pos);
            if (box != null) {
              if (box.p1.equals(pos)) {
                ch = '[';
              } else {
                ch = ']';
              }
            }
          }
          System.out.print(ch);
        }
        System.out.println();
      }
      System.out.println();
    }

    Box boxAt(Pos pos) {
      return boxes.stream().filter(box -> box.atPos(pos)).findFirst().orElse(null);
    }

    boolean canMove(Pos pos, Pos move, Set<Box> boxesToMove) {
      var pushed = pos.plus(move);
      if (walls.contains(pushed)) {
        return false;
      }
      var box = boxAt(pushed);
      if (box != null && boxesToMove.add(box)) {
        return canMove(box.p1, move, boxesToMove) && canMove(box.p2, move, boxesToMove);
      }
      return true;
    }

    Warehouse move() {
      Set<Box> boxesToMove = new HashSet<>();
      Pos move = moves.getFirst();
      var newBot = bot;
      var newBoxes = boxes;
      if (canMove(bot, move, boxesToMove)) {
        newBot = bot.plus(move);
        newBoxes = boxes.stream().map(box -> boxesToMove.contains(box) ? box.move(move) : box).toList();
      }
      return new Warehouse(walls, moves.subList(1, moves.size()), newBot, newBoxes);
    }

    int gpsSum() {
      return boxes.stream()
                  .map(Box::p1)
                  .mapToInt(pos -> pos.x() + pos.y() * 100)
                  .sum();
    }
  }

  int gpsSum(Warehouse w) {
    return Stream.iterate(w, Warehouse::move)
                 .dropWhile(warehouse -> !warehouse.moves.isEmpty())
                 .mapToInt(Warehouse::gpsSum)
                 .findFirst().orElseThrow();
  }

  @Override
  String part1() {
    return String.valueOf(gpsSum(Warehouse.fromData(data, false)));
  }

  @Override
  String part2() {
    return String.valueOf(gpsSum(Warehouse.fromData(data, true)));
  }

  public static void main(String[] args) {
    var day = new Day15() {
      @Override
      String getData() {
        return """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^""";
      }
    };
    System.out.println(day.part1().equals("10092"));
    System.out.println(day.part2().equals("9021"));
  }
}
