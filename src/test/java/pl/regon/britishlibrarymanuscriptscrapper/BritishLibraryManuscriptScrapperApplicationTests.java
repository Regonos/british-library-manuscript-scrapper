package pl.regon.britishlibrarymanuscriptscrapper;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class BritishLibraryManuscriptScrapperApplicationTests {

    private static final String SAVE_DIRECTORY = "/home/regonos/Desktop/kuba_nuty";

    @Test
    public void execute() {

        String bookName = "ms_mus._147";

        int size = 13;

        LocalTime startTime = LocalTime.now();
        BritishLibraryImageDownloader.downloadBook(bookName, size, SAVE_DIRECTORY);
        LocalTime endTime = LocalTime.now();


        Duration duration = Duration.between(startTime, endTime);
        System.out.println(String.format("Operation duration: %s seconds!", duration.getSeconds()));

    }
}

