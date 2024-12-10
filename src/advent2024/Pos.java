package advent2024;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

record Pos(int x, int y) {
  static final Pos U = new Pos(0, -1);
  static final Pos R = new Pos(1, 0);
  static final Pos D = new Pos(0, 1);
  static final Pos L = new Pos(-1, 0);

  static final Map<Pos, Pos> TURN = Map.of(U, R, R, D, D, L, L, U);

  static Stream<Pair<Pos, Character>> posStream(List<String> input) {
    return IntStream.range(0, input.getFirst().length())
                    .boxed()
                    .flatMap(x -> IntStream.range(0, input.size())
                                           .mapToObj(y -> Pair.of(new Pos(x, y), input.get(y).charAt(x))));
  }

  static <T> Map<Pos, T> collectByPos(List<String> input, Function<Character, T> convert) {
    return posStream(input).collect(Collectors.toMap(Pair::l, pair -> convert.apply(pair.r())));
  }

  Pos plus(Pos p2) {
    return new Pos(x + p2.x, y + p2.y);
  }
  Pos minus(Pos p2) {
    return new Pos(x - p2.x, y - p2.y);
  }
}
