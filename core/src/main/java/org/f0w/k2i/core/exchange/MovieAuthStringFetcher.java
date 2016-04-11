package org.f0w.k2i.core.exchange;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.google.inject.Inject;
import java.io.IOException;

public final class MovieAuthStringFetcher implements Exchangeable<Movie, String> {
    private final Config config;

    private Connection.Response response;

    @Inject
    public MovieAuthStringFetcher(Config config) {
        this.config = config;
    }

    @Override
    public void sendRequest(Movie movie) throws IOException {
        final String moviePageLink = "http://www.imdb.com/title/";

        Connection request = Jsoup.connect(moviePageLink + movie.getImdbId())
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"));

        response = request.execute();
    }

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    @Override
    public String getProcessedResponse() {
        try {
            return Jsoup.parse(response.body())
                    .select("[data-auth]")
                    .first()
                    .attr("data-auth");
        } catch (NullPointerException ignore) {
            return null;
        }
    }
}
