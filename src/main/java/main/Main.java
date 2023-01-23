package main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static final String URL = "https://api.nasa.gov/planetary/apod?api_key=5pUP6OnuweiKJ1mqteV3Xor4nbKaRfwS1cNjNaqu";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        final CloseableHttpClient httpClient = HttpClients.createDefault();

        final HttpUriRequest httpGet = new HttpGet(URL);

        Post post;

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

            post = mapper.readValue(response.getEntity().getContent(),
                    new TypeReference<>() {
                    });

        }
        if (post.getMediaType().equals("image")) {
            final String newUrl = post.getHdurl();
            String[] getName = newUrl.split("/");
            String nameFile = getName[getName.length - 1];

            File myFile = new File(nameFile);
            try {
                if (myFile.createNewFile()) {
                    System.out.println("Файл " + nameFile + " создан");
                }
            } catch (IOException e) {
                System.out.println("Не удалось создать файл");
            }

            BufferedImage image = ImageIO.read(new URL(newUrl));
            ImageIO.write(image, "jpg", myFile);

            System.out.println("Загрузка изображения завершена");
        }

    }
}
