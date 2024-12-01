package server;

import java.util.Arrays;

public class Main {
    private static final int DATABASE_SIZE = 1000;
    private final String[] database = new String[DATABASE_SIZE];

    public Main() {
        Arrays.fill(this.database, "");
    }

    public String set(int index, String text) {
        if (index < 1 || index > DATABASE_SIZE) {
            return "ERROR";
        }

        this.database[index - 1] = text; // Adjust for 0-based indexing
        return "OK";
    }

    public String get(int index) {
        if (index < 1 || index > DATABASE_SIZE) {
            return "ERROR";
        }

        String textRetrieved = this.database[index - 1];
        if (textRetrieved.equals("")) {
            return "ERROR";
        } else {
            return textRetrieved;
        }
    }

    public String delete(int index) {
        if (index < 1 || index > DATABASE_SIZE) {
            return "ERROR";
        }

        this.database[index - 1] = "";
        return "OK";
    }
}
