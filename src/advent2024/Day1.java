package advent2024;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Day1 extends Day {

  Day1() {
    super(1);
  }

  record Lists(int[] left, int[] right) {
    static Lists fromInput(List<String> input) {
      int[] left = new int[input.size()];
      int[] right = new int[input.size()];
      IntStream.range(0, input.size())
               .forEach(i -> {
                          String[] nums = input.get(i).split("\\h+");
                          left[i] = Integer.parseInt(nums[0]);
                          right[i] = Integer.parseInt(nums[1]);
                        }
               );
      return new Lists(left, right);
    }
  }

  @Override
  public String part1() {
    Lists lists = Lists.fromInput(input);
    Arrays.sort(lists.left);
    Arrays.sort(lists.right);
    var distance = IntStream.range(0, lists.left.length)
                            .mapToLong(i -> Math.abs(lists.left[i] - lists.right[i]))
                            .sum();
    return String.valueOf(distance);
  }

  @Override
  public String part2() {
    Lists lists = Lists.fromInput(input);
    var frequencies = Arrays.stream(lists.right).boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    var similarity = Arrays.stream(lists.left).mapToLong(l -> frequencies.getOrDefault(l, 0L) * l).sum();
    return String.valueOf(similarity);
  }

  public static void main(String[] args) {
    var day = new Day1() {
      String getData(int day) {
        return """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3""";
      }
    };
    System.out.println(day.part1().equals("11"));
    System.out.println(day.part2().equals("31"));
  }
}
