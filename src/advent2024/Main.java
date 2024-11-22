package advent2024;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class Main {

    private static final String INPUT_URL = "https://adventofcode.com/2024/day/%d/input";

    List<String> readLines(int day) {
        try {
            URL url = new URI(INPUT_URL.formatted(day)).toURL();
            return Arrays.asList(CachingHttpReader.getData(url).trim().split("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    record PartRun(String result, String duration) {
        static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("s.SSSSS");

        static PartRun run(Supplier<String> part) {
            Instant start = Instant.now();
            String result = part.get();
            Duration between = Duration.between(start, Instant.now());
            // https://stackoverflow.com/a/65586659
            return new PartRun(result, LocalTime.ofNanoOfDay(between.toNanos()).format(FORMAT));
        }
    }

    private void runDay(Day day) {
        List<String> input = readLines(day.number());
        PartRun part1 = PartRun.run(() -> day.part1(input));
        System.out.printf("day %s part 1: (%s) %s%n", day.number(), part1.duration, part1.result);
        PartRun part2 = PartRun.run(() -> day.part2(input));
        System.out.printf("day %s part 2: (%s) %s%n", day.number(), part2.duration, part2.result);
    }

    private void runDays() {
        List<Day> days = List.of(
                new Day1()
        );
        days.stream()
            .sorted(Comparator.comparing(Day::number))
            .forEach(this::runDay);
    }

    public static void main(String[] args) {
        new Main().runDays();
    }
}
