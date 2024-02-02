package com.example.Spring2.TwoFA;

import java.util.concurrent.ThreadLocalRandom;

public class TwoFAGenerator {
    private int number;

    // durch Konstruktor wird eine Nummer generiert
    public TwoFAGenerator() {
        generateNumber();
    }

    public void generateNumber(){
        this.number= ThreadLocalRandom.current().nextInt(1000,9999);

    }

    public int getNumber() {
        return number;
    }
}
