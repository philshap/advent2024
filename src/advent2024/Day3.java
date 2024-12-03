package advent2024;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Day3 extends Day {

  protected Day3() {
    super(3);
  }

  static final Pattern MUL = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

  @Override
  String part1() {
    var total = MUL.matcher(getData()).results()
                   .mapToInt(result -> Integer.parseInt(result.group(1)) *
                       Integer.parseInt(result.group(2)))
                   .sum();
    return String.valueOf(total);
  }

  static final Pattern MUL2 = Pattern.compile("(mul\\((\\d{1,3}),(\\d{1,3})\\))|(don't\\(\\))|(do\\(\\))");

  @Override
  String part2() {
    AtomicBoolean enabled = new AtomicBoolean(true);
    var total = MUL2.matcher(data).results()
                    .mapToInt(result -> {
                      if (result.group(1) != null) {
                        if (enabled.get()) {
                          return Integer.parseInt(result.group(2)) * Integer.parseInt(result.group(3));
                        }
                      } else {
                        enabled.set(result.group(5) != null);
                      }
                      return 0;
                    })
                    .sum();
    return String.valueOf(total);
  }
}
