package advent2024;

import java.net.URI;
import java.util.List;

public abstract class Day {
  abstract String part1();

  abstract String part2();

  final int number;
  final List<String> input;
  final String data;

  public int number() {
    return number;
  }

  protected Day(int number) {
    this.number = number;
    this.data = getData(number).trim();
    this.input = Support.splitInput(data);
  }

  private static final String INPUT_URL = "https://adventofcode.com/2024/day/%d/input";

  String getData(int day) {
    try {
      return CachingHttpReader.getData(new URI(INPUT_URL.formatted(day)).toURL());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
