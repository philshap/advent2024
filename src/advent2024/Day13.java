package advent2024;

import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;

public class Day13 extends Day {
  protected Day13() {
    super(13);
  }

  record Game(int ax, int ay, int bx, int by, int px, int py) {
    // Button A: X+11, Y+73
    // Button B: X+95, Y+99
    // Prize: X=6258, Y=10706
    static Game fromInput(List<Integer> input) {
      return new Game(input.get(0), input.get(1), input.get(2), input.get(3), input.get(4), input.get(5));
    }

    boolean atPrize(Pair<Integer, Integer> ab) {
      return px == (ab.l() * ax + ab.r() * bx)
          && py == (ab.l() * ay + ab.r() * by);
    }

    long prizeTokens1() {
      return IntStream.range(0, 101).boxed()
                      .flatMap(a -> IntStream.range(0, 101)
                                             .mapToObj(b -> Pair.of(a, b)))
                      .filter(this::atPrize)
                      .mapToInt(pair -> pair.l() * 3 + pair.r())
                      .findFirst()
                      .orElse(0);
    }

    static final long PRIZE_OFFSET = 10_000_000_000_000L;

    long prizeTokens2() {
      long px2 = px + PRIZE_OFFSET;
      long py2 = py + PRIZE_OFFSET;
      // Cramer's rule for 2x2 systems
      long a = (px2 * by - py2 * bx) / (ax * by - ay * bx);
      long b = (py2 * ax - px2 * ay) / (ax * by - ay * bx);
      if (px2 == (a * ax + b * bx) && py2 == (a * ay + b * by)) {
        return a * 3 + b;
      }
      return 0;
    }
  }

  long totalTokens(ToLongFunction<Game> prizeTokens) {
    return Support.partition(Support.integers(data), 6)
                  .map(Game::fromInput)
                  .mapToLong(prizeTokens)
                  .sum();
  }

  @Override
  String part1() {
    return String.valueOf(totalTokens(Game::prizeTokens1));
  }

  @Override
  String part2() {
    return String.valueOf(totalTokens(Game::prizeTokens2));
  }

  public static void main(String[] args) {
    var day = new Day13();
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
