package client.client.Vorschlaege;

public class VorschlagNode {

    public String name;
    public int count = 0;


    public VorschlagNode(String name){
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
        this.count = this.count + count;
    }

    public void riseCount(){ this.count++;}

}
