package yolo.basket.db;

public class Pair<T1, T2> {
    T1 val1;
    T2 val2;

    public Pair(T1 val1, T2 val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public T1 getKey() {
        return val1;
    }

    public T2 getValue() {
        return val2;
    }
}
