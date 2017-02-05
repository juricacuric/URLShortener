package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.URLRepository;
import services.Base62;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

@Singleton
public class UrlShortener extends Controller {

    private static final String JSON_URL_FIELD = "url";

    private final URLRepository urlRepository;

    @Inject
    public UrlShortener(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    public CompletionStage<Result> shortUrl() {
        final String contextRoot = routes.UrlShortener.shortUrl().absoluteURL(request()).replace("shortUrl", "");
        final JsonNode jsonNode = request().body().asJson();
        final JsonNode urlField = jsonNode.get(JSON_URL_FIELD);
        final String url = urlField.asText();
        return urlRepository.insertUrl(url)
                .thenApply(Base62::fromBase10)
                .thenApply(basePage -> {
                    responses.ShortUrlResponse shortUrlResponse = new responses.ShortUrlResponse(contextRoot + basePage);
                    return ok(Json.toJson(shortUrlResponse));
                });
    }

    public CompletionStage<Result> redirectToUrl(String urlIdentificator) {
        final long id = Base62.toBase10(urlIdentificator);
        return urlRepository.getUrlForId(id)
                .thenApply(redirectUrl -> {
                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        if (!redirectUrl.startsWith("http://")) {
                            return temporaryRedirect("http://" + redirectUrl);
                        }
                        return temporaryRedirect(redirectUrl);
                    } else {
                        return notFound();
                    }
                });
    }

}
