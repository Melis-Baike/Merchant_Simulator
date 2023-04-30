package com.example.merchantsimulator.enums;

public enum Towns {
    BISHKEK("Bishkek", 40),
    OSH("Osh", 50),
    TALAS("Talas",20),
    KARAKOL("Karakol", 60),
    NARYN("Naryn", 30);

    private final String name;
    private final int leagues;

    Towns(String name, int leagues) {
        this.name = name;
        this.leagues = leagues;
    }

    public String getName() {
        return name;
    }

    public int getLeagues() {
        return leagues;
    }
}
