package main;

public enum Constants {
    ROWS(4),
    COLUMNS(5),
    ERROR1(1),
    ERROR2(2),
    ERROR3(3),
    ERROR4(4),
    ERROR5(5),
    ROW0(0),
    ROW1(1),
    ROW2(2),
    ROW3(3),
    MAXIMMANA(10),
    HEALTHHERO(30);


    private final int value;  // Câmp pentru valoarea constantelor

    // Constructor pentru enum
    Constants(final int value) {
        this.value = value;
    }

    // Metodă pentru a obține valoarea constantelor
    public int getValue() {
        return value;
    }
}
