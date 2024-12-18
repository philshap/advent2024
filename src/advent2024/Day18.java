package advent2024;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

public class Day18 extends Day {
  protected Day18() {
    super(18);
  }

  record Path(Pos pos, int length) {
    Stream<Path> extend(int size, Set<Pos> seen) {
      return Pos.DIRS.stream()
                     .map(pos::plus)
                     .filter(pos -> !seen.contains(pos) && pos.within(0, size, 0, size))
                     .map(pos -> {
                       seen.add(pos);
                       return new Path(pos, length + 1);
                     });
    }
  }

  Path findPath(List<Pos> bytes, int nanosecond, int size) {
    Pos start = new Pos(0, 0);
    Pos end = new Pos(size - 1, size - 1);
    var queue = new PriorityQueue<>(Comparator.comparingInt(Path::length));
    queue.add(new Path(start, 0));
    var seen = new HashSet<>(bytes.subList(0, nanosecond));
    while (!queue.isEmpty() && !queue.peek().pos.equals(end)) {
      queue.poll().extend(size, seen).forEach(queue::add);
    }
    return queue.peek();
  }

  @Override
  String part1() {
    var xys = Support.integers(data);
    var bytes = Support.partition(xys, 2).map(Pos::new).toList();
    int size = xys.stream().max(Integer::compareTo).orElseThrow() + 1;
    return String.valueOf(findPath(bytes, 1024, size).length);
  }

  @Override
  String part2() {
    var xys = Support.integers(data);
    var bytes = Support.partition(xys, 2).map(Pos::new).toList();
    int size = xys.stream().max(Integer::compareTo).orElseThrow() + 1;
    int low = 0;
    int high = bytes.size();
    while (low <= high) {
      int middle = low + (high - low) / 2;
      if (findPath(bytes, middle, size) != null) {
        low = middle + 1;
      } else {
        high = middle - 1;
      }
    }
    Pos pos = bytes.get(low - 1);
    return String.format("%s,%s", pos.x(), pos.y());
  }

  public static void main(String[] args) {
    var day = new Day18();
    System.out.println(day.part1().equals("408"));
    System.out.println(day.part2().equals("45,16"));
  }
}
