package advent2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 extends Day {
  protected Day24() {
    super(24);
  }

  record Gate(String in1, String in2, String out, String op) {
    // x00 AND y00 -> z00
    static final Pattern GATE = Pattern.compile("(...) (.*) (...) -> (...)");

    static Gate fromLine(String line) {
      var result = GATE.matcher(line).results().findFirst().orElseThrow();
      return new Gate(result.group(1), result.group(3), result.group(4), result.group(2));
    }

    public boolean run(boolean v1, boolean v2) {
      return switch (op) {
        case "AND" -> v1 && v2;
        case "OR" -> v1 || v2;
        case "XOR" -> v1 ^ v2;
        default -> false;
      };
    }
  }

  record Device(Map<String, Boolean> wires, List<Gate> gates) {
    // x00: 0
    static final Pattern WIRE = Pattern.compile("(...): ([01])");

    static Device fromData(String data) {
      String[] split = data.split("\n\n");
      var wires = WIRE.matcher(split[0]).results()
                      .collect(Collectors.toMap(r -> r.group(1),
                                                r -> r.group(2).equals("1")));
      return new Device(wires, Support.splitInput(split[1]).stream().map(Gate::fromLine).toList());
    }

    long run() {
      var pending = new ArrayList<>(gates);
      var wires = new HashMap<>(wires());
      while (!pending.isEmpty()) {
        for (var gate : List.copyOf(pending)) {
          var v1 = wires.get(gate.in1);
          var v2 = wires.get(gate.in2);
          if (v1 != null && v2 != null) {
            wires.put(gate.out, gate.run(v1, v2));
            pending.remove(gate);
          }
        }
      }
      return wires.entrySet().stream().mapToLong(entry -> {
        String key = entry.getKey();
        if (key.charAt(0) == 'z' && entry.getValue()) {
          return 1L << Integer.parseInt(key.substring(1));
        }
        return 0;
      }).sum();
    }
  }

  @Override
  String part1() {
    var device = Device.fromData(data);
    return String.valueOf(device.run());
  }

  @Override
  String part2() {
    return "";
  }

  public static void main(String[] args) {
    var day = new Day24() {
      @Override
      String getData() {
        return """
            x00: 1
            x01: 1
            x02: 1
            y00: 0
            y01: 1
            y02: 0

            x00 AND y00 -> z00
            x01 XOR y01 -> z01
            x02 OR y02 -> z02""";
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
