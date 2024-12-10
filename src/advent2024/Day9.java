package advent2024;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 extends Day {
  protected Day9() {
    super(9);
  }

  record Block(int fileId, int size) {
    boolean empty() {
      return fileId == -1;
    }

    static Block fromChar(int index, char ch) {
      if (index % 2 == 0) {
        return new Block(index / 2, ch - '0');
      }
      return new Block(-1, ch - '0');
    }
  }

  List<Block> read(String mapData) {
    return IntStream.range(0, mapData.length())
                    .mapToObj(i -> Block.fromChar(i, mapData.charAt(i)))
                    .toList();
  }

  List<Block> compact(List<Block> diskMap) {
    List<Block> expanded = new ArrayList<>(diskMap);
    List<Block> compacted = new ArrayList<>();
    int copyTo = 0;
    int copyFrom = expanded.size() - 1;
    while (copyTo < copyFrom) {
      Block to = expanded.get(copyTo);
      if (to.empty() && to.size() != 0) {
        Block from = expanded.get(copyFrom);
        if (from.empty()) {
          copyFrom--;
        } else if (from.size <= to.size) {
          compacted.add(from);
          expanded.set(copyTo, new Block(to.fileId, to.size - from.size));
          copyFrom--;
        } else {
          compacted.add(new Block(from.fileId, to.size));
          expanded.set(copyFrom, new Block(from.fileId, from.size - to.size));
          copyTo++;
        }
      } else {
        compacted.add(expanded.get(copyTo++));
      }
    }
    compacted.add(expanded.get(copyFrom));
    return compacted;
  }

  long checksum(List<Block> diskMap) {
    int index = 0;
    long checksum = 0;
    for (Block block : diskMap) {
      if (!block.empty()) {
        for (int i = index; i < index + block.size; i++) {
          checksum += (long) i * block.fileId;
        }
      }
      index += block.size;
    }
    return checksum;
  }

  @Override
  String part1() {
    return String.valueOf(checksum(compact(read(data))));
  }

  List<Block> compact2(List<Block> diskMap) {
    List<Block> compacted = new ArrayList<>(diskMap);
    int copyFrom = compacted.size() - 1;
    while (copyFrom > 0) {
      Block from = compacted.get(copyFrom);
      if (from.empty()) {
        copyFrom--;
        continue;
      }
      for (int copyTo = 0; copyTo < copyFrom; copyTo++) {
        Block to = compacted.get(copyTo);
        if (to.empty() && from.size <= to.size) {
          compacted.set(copyFrom, new Block(-1, from.size));
          compacted.set(copyTo, from);
          if (to.size != from.size) {
            compacted.add(copyTo + 1, new Block(-1, to.size - from.size));
          }
          break;
        }
      }
      copyFrom--;
    }
    return compacted;
  }

  @Override
  String part2() {
    return String.valueOf(checksum(compact2(read(data))));
  }

  public static void main(String[] args) {
    var day = new Day9() {
      @Override
      String getData() {
        return """
            2333133121414131402""";
      }
    };
    System.out.println(day.part1().equals("1928"));
    System.out.println(day.part2().equals("2858"));
  }
}
