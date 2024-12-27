import java.io.CharArrayWriter;
import java.io.IOException;

class Converter {
    public static char[] convert(String[] words) throws IOException {

        try (CharArrayWriter charWriter = new CharArrayWriter()) {
            for (String word : words) {
                charWriter.append(word);
            }

            return charWriter.toCharArray();
        }
    }
}