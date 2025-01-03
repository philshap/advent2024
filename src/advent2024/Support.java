package advent2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Support {
  Pattern NUMBER = Pattern.compile("(-?\\d+)");

  static <T> Stream<List<T>> partition(List<T> source, int length) {
    return IntStream.range(0, source.size() / length).mapToObj(
        n -> source.subList(n * length, n * length + length));
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
    return longStream(input).boxed().toList();
  }

  static LongStream longStream(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).mapToLong(Long::parseLong);
  }

  static List<String> splitInput(String input) {
    return Arrays.asList(input.split("\n"));
  }

  ///  A computeIfAbsent that supports recursive functions.
  static <T, R> R computeIfAbsent(Map<T, R> cache, T input, Function<T, R> func) {
    var result = cache.get(input);
    if (result != null) {
      return result;
    }
    result = func.apply(input);
    cache.put(input, result);
    return result;
  }

  static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
    return map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
  }
}
