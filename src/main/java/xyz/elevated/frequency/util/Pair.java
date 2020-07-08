package xyz.elevated.frequency.util;

public final class Pair<X, Y> {
    private X first;
    private Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    public X getX() {
        return first;
    }

    public Y getY() {
        return second;
    }

    public void setX(final X to) {
        this.first = to;
    }

    public void setY(final Y to) {
        this.second = to;
    }
}
