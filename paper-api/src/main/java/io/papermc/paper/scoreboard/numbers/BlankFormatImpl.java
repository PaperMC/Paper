package io.papermc.paper.scoreboard.numbers;

record BlankFormatImpl() implements NumberFormat {
    public static final BlankFormatImpl INSTANCE = new BlankFormatImpl();
}
