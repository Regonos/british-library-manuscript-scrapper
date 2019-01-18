package pl.regon.britishlibrarymanuscriptscrapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class ImageUtils {


    public static BufferedImage assembleImageMatrix(ArrayList<ArrayList<BufferedImage>> imagesMatrix) {

        int width = getWidthOfMatrix(imagesMatrix);
        int height = getHeightOfMatrix(imagesMatrix);

        BufferedImage mainImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics mainImageGraphics = mainImage.getGraphics();

        int widthPosition = 0;
        for (ArrayList<BufferedImage> imageColumn : imagesMatrix) {
            int heightPosition = 0;

            for (BufferedImage image : imageColumn) {
                mainImageGraphics.drawImage(image, widthPosition, heightPosition, null);
                heightPosition += image.getHeight();
            }
            widthPosition += imageColumn.get(0).getWidth();
        }

        return mainImage;
    }

    public static BufferedImage readImageFromUrl(String urlString) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(urlString));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static boolean imageUrlExists(String urlString) {
        return Objects.nonNull(readImageFromUrl(urlString));
    }

    public static boolean saveJPGImage(BufferedImage image, String savePath) {
        try {
            File saveFile = new File(savePath);
            saveFile.getParentFile().mkdirs();
            return ImageIO.write(image, "JPG", saveFile);
        } catch (IOException e) {
            return false;
        }
    }

    private static int getWidthOfMatrix(ArrayList<ArrayList<BufferedImage>> imagesMatrix) {

        int width = 0;

        for (ArrayList<BufferedImage> imageSubMatrix : imagesMatrix) {
            width += imageSubMatrix.get(0).getWidth();
        }

        return width;
    }

    private static int getHeightOfMatrix(ArrayList<ArrayList<BufferedImage>> imagesMatrix) {

        int height = 0;

        for (BufferedImage image : imagesMatrix.get(0)) {
            height += image.getHeight();
        }

        return height;
    }


}
