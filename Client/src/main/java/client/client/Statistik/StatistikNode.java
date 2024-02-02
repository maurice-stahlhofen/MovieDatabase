package client.client.Statistik;

public class StatistikNode {

    public String name;
    public int count = 1;

    public StatistikNode(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public void riseCount() { this.count++; }
}
