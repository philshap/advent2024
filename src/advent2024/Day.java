package advent2024;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public abstract class Day {
  abstract String part1();

  abstract String part2();

  final int number;
  final List<String> input;

  public int number() {
    return number;
  }

  protected Day(int number) {
    this.number = number;
    this.input = readInput(number);
  }

  private static final String INPUT_URL = "https://adventofcode.com/2024/day/%d/input";

  static List<String> readInput(int day) {
    try {
      URL url = new URI(INPUT_URL.formatted(day)).toURL();
      return Arrays.asList(CachingHttpReader.getData(url).trim().split("\n"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
