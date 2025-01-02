package advent2024;

import java.util.stream.Stream;

public class Day22 extends Day {
  protected Day22() {
    super(22);
  }

  // To prune the secret number, calculate the value of the secret number modulo 16777216
  static long prune(long value) {
    return value % 16777216;
  }

  static long next(long number) {
    number = prune(number ^ (number * 64));
    number = prune(number ^ (number / 32));
    number = prune(number ^ (number * 2048));
    return number;
  }

  @Override
  String part1() {
    var total = Support.longStream(data)
                       .map(initial ->
                                Stream.iterate(initial, Day22::next)
                                      .skip(2000)
                                      .findFirst().orElseThrow())
                       .sum();
    return String.valueOf(total);
  }

  @Override
  String part2() {
    return "";
  }

  public static void main(String[] args) {
    var day = new Day22() {
      @Override
      String getData() {
        return """
            1
            10
            100
            2024""";
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
