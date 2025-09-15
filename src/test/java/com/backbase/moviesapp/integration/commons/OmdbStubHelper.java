package com.backbase.moviesapp.integration.commons;

import com.backbase.moviesapp.constants.OMDBConstants;
import com.backbase.moviesapp.fakers.CommonFaker;
import lombok.Builder;
import lombok.experimental.UtilityClass;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@UtilityClass
public class OmdbStubHelper {

    @Builder
    public record OmdbStubRequest(
            String title,
            String imdbId,
            String boxOffice,
            String year,
            String genre
    ) {
    }

    public void stubById(OmdbStubRequest request) {
        stubOmdb(request, OMDBConstants.IMDB_ID_PARAM, request.imdbId());
    }

    public void stubByTitle(OmdbStubRequest request) {
        stubOmdb(request, OMDBConstants.TITLE_PARAM, request.title());
    }

    private void stubOmdb(OmdbStubRequest request, String queryParamKey, String queryParamValue) {
        String response = """
                {
                  "Title": "%s",
                  "Year": "2010",
                  "imdbID": "%s",
                  "Response": "True",
                  "BoxOffice": "%s"
                }
                """.formatted(request.title(), request.imdbId(), request.boxOffice());

        stubFor(get(urlPathEqualTo("/"))
                .withQueryParam(queryParamKey, equalTo(queryParamValue))
                .willReturn(okJson(response)));
    }

    public OmdbStubRequest defaultRequest(String title) {
        return OmdbStubRequest.builder()
                .title(title)
                .imdbId(CommonFaker.imdbId())
                .boxOffice(CommonFaker.boxOfficeValue())
                .build();
    }
}
