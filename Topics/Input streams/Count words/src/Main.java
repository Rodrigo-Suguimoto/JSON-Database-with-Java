import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        String userInput = reader.readLine().trim();
        if (userInput.equals("")) {
            System.out.println(0);
            return;
        }

        String[] splitInput = userInput.split("\\s+");
        System.out.println(splitInput.length);

        reader.close();
    }
}