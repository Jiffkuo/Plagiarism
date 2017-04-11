/**
 * Created by Tzu-Chi Kuo on 2017/4/11.
 */
public class Pair<T> {
    private T value1;
    private T value2;

    // constructor
    public Pair(T v1, T v2) {
        this.value1 = v1;
        this.value2 = v2;
    }

    public void setValue1(T val) {
        this.value1 = val;
    }

    public void setValue2(T val) {
        this.value2 = val;
    }

    public T getValue1() {
        return this.value1;
    }

    public T  getValue2() {
        return this.value2;
    }
}
