package advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day2 extends Day {
  protected Day2() {
    super(2);
  }

  record State(int neg, int pos, int total, boolean differ) {
    State(int delta) {
      this(delta < 0 ? 1 : 0, delta > 0 ? 1 : 0, 1, Math.abs(delta) > 3);
    }

    static State fromLevels(List<Integer> levels) {
      return new State(levels.getFirst() - levels.getLast());
    }

    boolean safe() {
      return (neg == total || pos == total) && !differ;
    }

    static State reduce(State s1, State s2) {
      return new State(s1.neg + s2.neg, s1.pos + s2.pos, s1.total + s2.total, s1.differ || s2.differ);
    }
  }

  boolean safeLevels(List<Integer> levels) {
    return Support.partition(levels, 2, 1)
                  .map(State::fromLevels)
                  .reduce(State::reduce)
                  .map(State::safe)
                  .orElseThrow();
  }

  @Override
  String part1() {
    long safe = input.stream()
                     .map(Support::integers)
                     .filter(this::safeLevels)
                     .count();

    return String.valueOf(safe);
  }

  static <T> Stream<List<T>> dropOne(List<T> source) {
    return IntStream.range(0, source.size())
                    .mapToObj(i -> {
                      var l = new ArrayList<T>();
                      l.addAll(source.subList(0, i));
                      l.addAll(source.subList(i + 1, source.size()));
                      return l;
                    });
  }

  @Override
  String part2() {
    long safe = input.stream()
                     .map(Support::integers)
                     .filter(levels -> dropOne(levels).anyMatch(this::safeLevels))
                     .count();

    return String.valueOf(safe);
  }

  public static void main(String[] args) {
    var day = new Day2() {
      @Override
      String getData() {
        return """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9""";
      }
    };
    System.out.println(day.part1().equals("2"));
    System.out.println(day.part2().equals("4"));
  }
}
