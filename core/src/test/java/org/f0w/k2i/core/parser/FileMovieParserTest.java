package org.f0w.k2i.core.parser;

import com.google.common.io.Resources;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class FileMovieParserTest extends BaseMovieParserTest {
    @Before
    public void setUp() throws Exception {
        parser = new FileMovieParser();
    }

    @Test
    public void parseFromEmptyFile() throws Exception {
        val resource = getClass().getClassLoader().getResource("parser/test_data_file_empty.xls");
        val data = Resources.toString(resource, Charset.forName("windows-1251"));

        assertEquals(Collections.emptyList(), parser.parse(data));
    }

    @Test
    public void parseFromInvalidFile() throws Exception {
        val resource = getClass().getClassLoader().getResource("parser/test_data_file_invalid.xls");
        val data = Resources.toString(resource, Charset.forName("windows-1251"));

        assertEquals(Collections.emptyList(), parser.parse(data));
    }

    @Test
    public void parseFromValidFileExportedFromLists() throws Exception {
        val resource = getClass().getClassLoader().getResource("parser/test_data_file_exported_from_lists.xls");
        val data = Resources.toString(resource, Charset.forName("windows-1251"));
        val expected = Arrays.asList(
                new Movie("Kung Fury", 2015, Movie.Type.SHORT, 9, null),
                new Movie("Jackass Number Two", 2006, Movie.Type.DOCUMENTARY, 8, null),
                new Movie("Белое солнце пустыни", 1969, 7),
                new Movie("Операция Ы и другие приключения Шурика", 1965, 9),
                new Movie("Кавказская пленница, или Новые приключения Шурика", 1966, 8),
                new Movie("Иван Васильевич меняет профессию", 1973, 8),
                new Movie("В бой идут одни старики", 1973, 8),
                new Movie("Бриллиантовая рука", 1968, 8),
                new Movie("...А зори здесь тихие", 1972),
                new Movie("Приключения Шерлока Холмса и доктора Ватсона: Собака Баскервилей", 1981),
                new Movie("Шерлок Холмс и доктор Ватсон: Знакомство", 1979),
                new Movie("Собачье сердце", 1988, 10),
                new Movie("Офицеры", 1971),
                new Movie("Москва слезам не верит", 1979),
                new Movie("Летят журавли", 1957),
                new Movie("Судьба человека", 1959),
                new Movie("Белый Бим Черное ухо", 1976),
                new Movie("Они сражались за Родину", 1975),
                new Movie("Breaking Bad", 2008, Movie.Type.SERIES, 10, null),
                new Movie("South Park", 1997, Movie.Type.SERIES, 10, null),
                new Movie("Intouchables", 2011, 10),
                new Movie("Hannibal", 2013, Movie.Type.SERIES, 10, null),
                new Movie("Kokaku kidotai: Stand Alone Complex", 2002, Movie.Type.SERIES, 10, null),
                new Movie("Sin City", 2005, 10),
                new Movie("Law Abiding Citizen", 2009, 10)
        );

        assertEquals(expected, parser.parse(data));
    }

    @Test
    public void parseFromValidFileExportedFromProfile() throws Exception {
        val resource = getClass().getClassLoader().getResource("parser/test_data_file_exported_from_profile.xls");
        val data = Resources.toString(resource, Charset.forName("windows-1251"));
        val expected = Arrays.asList(
                new Movie("Standoff", 2015, 6),
                new Movie("The Trust", 2016, 6),
                new Movie("Space Cop", 2016, 5),
                new Movie("Как поднять миллион. Исповедь Z@drota", 2014, 7),
                new Movie("Cellular", 2004, 7),
                new Movie("I Smile Back", 2015, 5),
                new Movie("Childhood's End", 2015, 5),
                new Movie("D'Ardennen", 2015, 6),
                new Movie("Joy", 2015, 6),
                new Movie("The Boy", 2016, 6),
                new Movie("The Merry Gentleman", 2008, 5),
                new Movie("Every Secret Thing", 2014, 4),
                new Movie("The 5th Wave", 2016, 5),
                new Movie("Triple 9", 2016, 7),
                new Movie("Чемпионы: Быстрее. Выше. Сильнее", 2016, 6),
                new Movie("An Inspector Calls", 2015, 7),
                new Movie("The Boy", 2015, 3),
                new Movie("Тряпичный союз", 2015, 4),
                new Movie("Crystal Fairy & the Magical Cactus and 2012", 2013, 4),
                new Movie("La fille de Monaco", 2008, 5),
                new Movie("The Danish Girl", 2015, 7),
                new Movie("Deadpool", 2016, 7),
                new Movie("День выборов 2", 2015, 6),
                new Movie("Kung Fu Panda 3", 2016, 6),
                new Movie("Вакантна жизнь шеф-повара", 2015, 5)
        );

        assertEquals(expected, parser.parse(data));
    }
}