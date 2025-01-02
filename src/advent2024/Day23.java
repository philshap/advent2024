package advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day23 extends Day {
  protected Day23() {
    super(23);
  }

  record Node(String node) {

    boolean isT() {
      return node.charAt(0) == 't';
    }
  }

  record Network(Map<Node, List<Node>> map) {
    static Pattern CONNECTION = Pattern.compile("(..)-(..)");

    static Network fromData(String data) {
      var network = new Network(new HashMap<>());
      CONNECTION.matcher(data).results().forEach(result -> {
        var n1 = new Node(result.group(1));
        var n2 = new Node(result.group(2));
        network.map.computeIfAbsent(n1, (x) -> new ArrayList<>()).add(n2);
        network.map.computeIfAbsent(n2, (x) -> new ArrayList<>()).add(n1);
      });
      return network;
    }

    List<Set<Node>> collectSets(Node node) {
      return map.get(node).stream()
                .flatMap(n1 -> map.get(n1).stream()
                                  .filter(n2 -> map.get(n2).contains(node))
                                  .map(n2 -> Set.of(node, n1, n2)))
                .distinct()
                .toList();
    }
  }

  @Override
  String part1() {
    var network = Network.fromData(data);
    Set<Set<Node>> sets =
        network.map.keySet().stream()
                   .filter(Node::isT)
                   .flatMap(node -> network.collectSets(node).stream())
                   .collect(Collectors.toSet());
    return String.valueOf(sets.size());
  }

  @Override
  String part2() {
    return "";
  }

  public static void main(String[] args) {
    var day = new Day23() {
      @Override
      String getData() {
        return """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn""";
      }
    };
    System.out.println(day.part1());
  }
}
