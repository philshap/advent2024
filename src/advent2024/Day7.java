package advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Day7 extends Day {
  public Day7() {
    super(7);
  }

  record Equation(long total, List<Long> numbers) {
    static final Pattern EQ = Pattern.compile("(\\d+): (.+)");
    static Equation fromLine(String line) {
      return EQ.matcher(line).results()
               .map(result -> new
                   Equation(Long.parseLong(result.group(1)),
                            Support.longs(result.group(2))))
               .findFirst().orElseThrow();
    }

    boolean canSolve(boolean allowConcat) {
      List<Long> totals = List.of(numbers.getFirst());
      int index = 1;
      while (!totals.isEmpty() && index < numbers.size()) {
        List<Long> next = new ArrayList<>();
        for (long l : totals) {
          if (l > total) {
            continue;
          }
          long val = numbers.get(index);
          next.add(l + val);
          next.add(l * val);
          if (allowConcat) {
            next.add(Long.parseLong("" + l + val));
          }
        }
        index++;
        totals = next;
      }
      return totals.contains(total);
    }
  }
  @Override
  String part1() {
    long grandTotal = input.stream().map(Equation::fromLine)
                           .filter(equation -> equation.canSolve(false))
                           .mapToLong(Equation::total)
                           .sum();
    return String.valueOf(grandTotal);
  }

  @Override
  String part2() {
    long grandTotal = input.stream().map(Equation::fromLine)
                           .filter(eq -> eq.canSolve(true))
                           .mapToLong(Equation::total)
                           .sum();
    return String.valueOf(grandTotal);
  }

  public static void main(String[] args) {
    var day = new Day7() {
      @Override
      String getData() {
        return """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20""";
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
