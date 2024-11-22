package advent2024;

record Pair<L, R>(L l, R r) {
    static <L, R> Pair<L, R> of(L l, R r) {
        return new Pair<>(l, r);
    }
}
