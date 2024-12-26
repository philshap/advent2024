package advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day17 extends Day {
  protected Day17() {
    super(17);
  }

  record Program(List<Integer> ops, long initialA, long initialB, long initialC) {

    static Program fromData(String data) {
      String[] split = data.split("\n\n");
      var regs = Support.integers(split[0]);
      var ops = Support.integers(split[1]);
      return new Program(ops, regs.get(0), regs.get(1), regs.get(2));
    }

    List<Integer> run() {
      return run(initialA);
    }

    List<Integer> run(long initialA) {
      int pc = 0;
      long a = initialA;
      long b = initialB;
      long c = initialC;
      List<Integer> output = new ArrayList<>();
      while (pc < ops.size()) {
        int op = ops.get(pc);
        long operand = ops.get(pc + 1);
        if (op != 1 && op != 3) {
          operand = switch ((int) operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> -1;
          };
        }
        pc += 2;
        switch (op) {
          case 0 -> a >>= operand;
          case 1 -> b ^= operand;
          case 2 -> b = operand % 8;
          case 3 -> {
            if (a != 0) {
              pc = (int) operand;
            }
          }
          case 4 -> b ^= c;
          case 5 -> output.add((int) (operand % 8));
          case 6 -> b = a >> operand;
          case 7 -> c = a >> operand;
        }
      }
      return output;
    }

    void findInitialARecur(int index, long n, List<Long> results) {
      var match = ops.subList(index, ops.size());
      LongStream.range(n, n + 8)
          .forEach(a -> {
            if (run(a).equals(match)) {
              if (index == 0) {
                results.add(a);
              } else {
                findInitialARecur(index - 1, a * 8, results);
              }
            }
          });
    }

    long findInitialA() {
      List<Long> results = new ArrayList<>();
      findInitialARecur(ops.size() - 1, 0L, results);
      return results.stream().mapToLong(x -> x).min().orElseThrow();
    }
  }

  @Override
  String part1() {
    return Program.fromData(data).run().stream()
                  .map(String::valueOf)
                  .collect(Collectors.joining(","));
  }

  @Override
  String part2() {
    return String.valueOf(Program.fromData(data).findInitialA());
  }

  public static void main(String[] args) {
    var day = new Day17() {
      @Override
      String getData() {
        return """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0""";
      }
    };
    System.out.println(day.part1().equals("5,7,3,0"));
    System.out.println(day.part2().equals("117440"));
  }
}
