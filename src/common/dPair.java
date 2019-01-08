package common;

public class dPair implements Comparable<dPair> {
    public int key;
    public double value;

    public dPair(int i, double dist) {
        key = i;
        value = dist;
    }

    @Override
    public int compareTo(dPair o) {
        return -1 * Double.compare(this.value, o.value);
    }
}
