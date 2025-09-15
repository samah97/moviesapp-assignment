package com.backbase.moviesapp.integration;

import com.backbase.moviesapp.constants.OMDBConstants;
import com.backbase.moviesapp.domain.AcademyAward;
import com.backbase.moviesapp.domain.Rating;
import com.backbase.moviesapp.fakers.AcademyAwardFaker;
import com.backbase.moviesapp.integration.base.BaseIntegrationTest;
import com.backbase.moviesapp.integration.commons.OmdbStubHelper;
import com.backbase.moviesapp.repository.RatingRepository;
import com.backbase.moviesapp.services.AcademyAwardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MoviesIntegrationTest extends BaseIntegrationTest {


    @Autowired
    private AcademyAwardService academyAwardService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    private static final String TEST_API_TOKEN = "test-token";
    private static final String API_TOKEN_HEADER = "X-API-TOKEN";

    @Test
    void shouldReturnWinnerResponse() throws Exception {
        String movieTitle = "Inception";
        String category = "Best Picture";
        boolean hasWon = true;

        OmdbStubHelper.stubByTitle(
                OmdbStubHelper.OmdbStubRequest.builder()
                        .title(movieTitle)
                        .build()
        );

        AcademyAward academyAward = AcademyAwardFaker.createAward(category, movieTitle, true);
        academyAwardService.save(academyAward);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/winner")
                        .header(API_TOKEN_HEADER, TEST_API_TOKEN)
                        .param("category", category)
                        .param("movie", movieTitle))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieTitle))
                .andExpect(jsonPath("$.hasWon").value(hasWon));
    }

    @Test
    void rateMovie_shouldSaveMovie() throws Exception {
        String movieTitle = "Inception";
        String imdbId = "tt1375666";
        String omdbResponse = """
                    {
                      "Title": "%s",
                      "Year": "2010",
                      "imdbID": "%s",
                      "Response": "True",
                      "BoxOffice": "$829,895,144"
                    }
                """.formatted(movieTitle, imdbId);

        stubFor(get(urlPathEqualTo("/"))
                .withQueryParam(OMDBConstants.IMDB_ID_PARAM, equalTo(imdbId))
                .willReturn(okJson(omdbResponse)));
        OmdbStubHelper.stubById(
                OmdbStubHelper.OmdbStubRequest.builder()
                        .title(movieTitle)
                        .imdbId(imdbId)
                        .build()
        );


        ratingRepository.deleteAll();
        //Act
        performRateMovieRequest(imdbId, new BigDecimal("5.0"));

        //Validate
        List<Rating> ratingList = ratingRepository.findAll();
        assertEquals(1, ratingList.size());
        assertEquals(imdbId, ratingList.get(0).getImdbId());
    }


    private void performRateMovieRequest(String imdbId, BigDecimal score) throws Exception {
        String rateMovieRequest = """
                {
                    "imdbId":"%s",
                    "score":%s
                }
                """.formatted(imdbId, score);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/movies/rate")
                                .header(API_TOKEN_HEADER, TEST_API_TOKEN)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .content(rateMovieRequest)
                )
                .andExpect(status().isAccepted());
    }

}
