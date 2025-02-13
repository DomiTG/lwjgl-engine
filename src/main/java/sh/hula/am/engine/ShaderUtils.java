package sh.hula.am.engine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ShaderUtils {
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = ShaderUtils.class.getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            result = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new Exception("Error loading shader file: " + fileName, e);
        }
        return result;
    }
}