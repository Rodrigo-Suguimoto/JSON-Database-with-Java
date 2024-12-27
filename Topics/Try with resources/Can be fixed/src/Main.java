import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
    public static String main(String[] args) {

        String language;
        BufferedReader buffReader;
        try {
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
            language = buffReader.readLine();
            System.out.println("I'm learning " + language);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            buffReader.close();
        }
    }
}