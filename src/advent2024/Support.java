package advent2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Support {
  Pattern NUMBER = Pattern.compile("(-?\\d+)");

  static Collector<Character, StringBuilder, String> collectToString() {
    return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
  }

  static <T> Stream<List<T>> partition(List<T> source, int length) {
    return partition(source, length, 0);
  }

  static <T> Stream<List<T>> partition(List<T> source, int length, int overlap) {
    return IntStream.range(0, source.size() - length + overlap).mapToObj(
        n -> source.subList(n * (length - overlap), n + length));
  }

  static <T> Stream<List<T>> combinations(List<T> data, int size) {
    final long N = 1L << data.size();
    return StreamSupport.stream(new Spliterators.AbstractSpliterator<>(N, Spliterator.SIZED) {
      long i = 1;

      @Override
      public boolean tryAdvance(Consumer<? super List<T>> action) {
        if (i < N) {
          int comboLength = Long.bitCount(i);
          if (comboLength == size) {
            List<T> out = new ArrayList<>(comboLength);
            for (int bit = 0; bit < data.size(); bit++) {
              if ((i & (1L << bit)) != 0) {
                out.add(data.get(bit));
              }
            }
            action.accept(out);
          }
          ++i;
          return true;
        } else {
          return false;
        }
      }
    }, false);
  }

  static List<Integer> integers(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).map(Integer::parseInt).toList();
  }

  static List<Long> longs(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).map(Long::parseLong).toList();
  }

  static List<String> splitInput(String input) {
    return Arrays.asList(input.split("\n"));
  }
}
