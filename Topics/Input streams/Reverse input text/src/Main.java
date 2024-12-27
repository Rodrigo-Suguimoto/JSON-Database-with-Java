import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        int charAsNumber = reader.read();
        List<Character> listOfChars = new ArrayList<>();

        while (charAsNumber != -1) {
            char character = (char) charAsNumber;
            listOfChars.add(character);
            charAsNumber = reader.read();
        }

        for (int i = listOfChars.size() - 1; i >= 0; i--) {
            System.out.print(listOfChars.get(i));
        }

        reader.close();
    }
}