package client.client.Statistik;


import java.util.LinkedList;
import java.util.List;


public class StatistikZwischenspeicher {
    private static StatistikZwischenspeicher speicher;

    private List<Integer> numbers;

    private StatistikZwischenspeicher() {
    }

    public static StatistikZwischenspeicher getSpeicher(){
        if(speicher == null){
            speicher = new StatistikZwischenspeicher();
        }

        return speicher;
    }


    public List<Integer> getList() {
        return numbers;
    }

    public void setList(List<Integer> numbers) {
        this.numbers = numbers;
    }

}
