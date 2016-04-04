package org.f0w.k2i.core.util;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.f0w.k2i.core.util.MovieUtils.*;

public class FileUtils {
    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    public static File checkFile(File file) {
        requireNonNull(file, "File is not set!");
        checkArgument(file.exists(), "File not exists!");
        checkArgument(file.isFile(), "Not a file!");

        return file;
    }

    public static String getHashCode(File file) {
        try {
            return Files.hash(file, Hashing.sha256()).toString();
        } catch (IOException e) {
            throw new KinopoiskToIMDBException(e);
        }
    }

    public static List<Movie> parseMovies(File file) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            Document document = Jsoup.parse(file, Charset.forName("windows-1251").toString());
            Elements content = document.select("table tr");
            content.remove(0);

            for (Element entity : content) {
                Elements elements = entity.getElementsByTag("td");

                movies.add(new Movie(
                        parseTitle(elements.get(1).text(), elements.get(0).text()),
                        parseYear(elements.get(2).text()),
                        parseRating(elements.get(9).text())
                ));
            }
        } catch (IOException e) {
            throw new KinopoiskToIMDBException(e);
        }

        return movies;
    }
}
