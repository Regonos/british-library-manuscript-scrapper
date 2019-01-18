package pl.regon.britishlibrarymanuscriptscrapper;

import javafx.util.Pair;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BritishLibraryImageDownloader {

    private final static String URL_FORMAT = "http://www.bl.uk/manuscripts/Proxy.ashx?view=%s_f%03d%s_files/%s/%s_%s.jpg";
    private static final String IMAGE_NAME_FORMAT = "Image_%03d_%s";


    public static void downloadBook(String bookName, int size, String saveDirectory) {

        ExecutorService executor = Executors.newWorkStealingPool();

        List<LinkWithName> generatedLinks = generateLinks(bookName, size);

        CompletableFuture.allOf(generatedLinks.stream()
                .map(link -> CompletableFuture.runAsync(() ->
                        downloadByUrl(link, saveDirectory), executor))
                .toArray(CompletableFuture[]::new))
                .join();

    }

    private static List<LinkWithName> generateLinks(String bookName, int size) {

        List<LinkWithName> generatedLinks = new ArrayList<>();

        List<String> possibleSuffixes = Arrays.asList("v", "r");

        int failureCount = 0;
        for (int i = 1; ; i++) {
            boolean anySucceed = false;
            for (String suffix : possibleSuffixes) {
                String urlString = String.format(URL_FORMAT, bookName.toLowerCase(), i, suffix, size, "%d", "%d");
                if (ImageUtils.imageUrlExists(String.format(urlString, 0, 0))) {
                    generatedLinks.add(new LinkWithName(urlString, String.format(IMAGE_NAME_FORMAT, i, suffix)));
                    System.out.println(String.format("Added url to download: %s", urlString));
                    failureCount = 0;
                    anySucceed = true;
                }
            }

            if (anySucceed) {
                continue;
            }

            failureCount++;
            if (failureCount >= 3) {
                break;
            }
        }

        return generatedLinks;
    }


    public static void downloadByUrl(LinkWithName linkWithName, String saveDirectory) {
        ArrayList<ArrayList<BufferedImage>> imagesMatrix = new ArrayList<>();

        outer:
        for (int i = 0; ; i++) {

            ArrayList<BufferedImage> imagesSubMatrix = new ArrayList<>();

            for (int j = 0; ; j++) {

                String urlString = String.format(linkWithName.getLink(), i, j);

                BufferedImage image = ImageUtils.readImageFromUrl(urlString);

                if (Objects.isNull(image)) {
                    if (j == 0) {
                        break outer;
                    }

                    break;
                }

                System.out.println(String.format("Added chunk to matrix: %s", urlString));

                imagesSubMatrix.add(image);
            }

            imagesMatrix.add(imagesSubMatrix);
        }

        ImageUtils.saveJPGImage(ImageUtils.assembleImageMatrix(imagesMatrix), String.format("%s/%s.jpg", saveDirectory, linkWithName.getName()));
    }
}
