package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.net.HttpRequest;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.Response;

import org.jsoup.Jsoup;
import java.nio.charset.StandardCharsets;

public class MovieAuthStringFetcher {
    private Configuration config;

    public MovieAuthStringFetcher(Configuration config) {
        this.config = config;
    }

    public String fetch(Movie movie) {
        Response response = sendRequest(movie);

        return handleResponse(response);
    }

    protected Response sendRequest(Movie movie) {
        return new HttpRequest.Builder()
                .setUrl("http://www.imdb.com/title/" + movie.getImdbId())
                .setUserAgent(config.get("user_agent"))
                .addCookie("id", config.get("auth"))
                .build()
                .getResponse()
        ;
    }

    protected String handleResponse(Response data) {
        return Jsoup.parse(data.toString(), StandardCharsets.UTF_8.name())
                .select("[data-auth]")
                .first()
                .attr("data-auth")
        ;
    }
}
