package org.f0w.k2i.core.command;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.comparators.title.TitleComparatorType;
import org.f0w.k2i.core.comparators.title.TitleComparatorsFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFindersFactory;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.utils.MovieFieldsUtils.*;

public class ParseIMDBMovieIDCommand extends AbstractMovieCommand {
    private final Config config;

    private final MovieFindersFactory movieFindersFactory;

    @Inject
    public ParseIMDBMovieIDCommand(Config config, MovieFindersFactory movieFindersFactory) {
        this.config = config;
        this.movieFindersFactory = movieFindersFactory;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        if (movie.getImdbId() != null) {
            LOG.info("Movie is already prepared {}", movie);
            return;
        }

        LOG.info("Preparing movie {}", movie);

        try {
            MovieFinder.Type movieFinderType = MovieFinder.Type.valueOf(config.getString("query_format"));

            MovieFinder movieFinder = movieFindersFactory.make(movieFinderType);
            movieFinder.sendRequest(movie);

            List<Movie> movies = filterMovies(movie, movieFinder.getProcessedResponse());

            Optional<Movie> matchingMovie = findMatchingMovie(movie, movies);

            if (!matchingMovie.isPresent()) {
                LOG.info("Matching movie not found");
                return;
            }

            matchingMovie.ifPresent(m -> {
                movie.setImdbId(m.getImdbId());

                importProgress.setMovie(movie);

                LOG.info("Movie IMDB id found: {}", m.getImdbId());
            });
        } catch (IOException e) {
            LOG.info("Can't prepare movie: {}", e);
        }
    }

    private Optional<Movie> findMatchingMovie(Movie movie, List<Movie> movies) {
        TitleComparatorType titleComparatorType = TitleComparatorType.valueOf(config.getString("comparator"));
        EqualityComparator<Movie> comparator = TitleComparatorsFactory.make(titleComparatorType);

        return movies.stream()
                .filter(imdbMovie -> comparator.areEqual(movie, imdbMovie))
                .findFirst();
    }

    private List<Movie> filterMovies(Movie movie, List<Movie> movies) {
        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movieFieldsIsSet())
                .filter(isBetweenYearDeviation(movie, config.getInt("year_deviation")))
                .collect(Collectors.toList());
    }

    private Predicate<Movie> isBetweenYearDeviation(Movie movie, int deviation) {
        return imdbMovie -> Range.closed(imdbMovie.getYear() - deviation, imdbMovie.getYear() + deviation)
                .contains(movie.getYear());
    }

    private Predicate<Movie> movieFieldsIsSet() {
        return m -> (!isEmptyTitle(m.getTitle()) && !isEmptyYear(m.getYear()) && !isEmptyIMDBId(m.getImdbId()));
    }
}