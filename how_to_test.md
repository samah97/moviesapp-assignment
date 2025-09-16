## How to Test

You can perform manual tests by calling the endpoints

> Postman collection added for simple import under _postman_collection_ directory

Examples added to each request

### Quick checks:

> 1. GET /api/movies/winner?title=Dances%20with%20Wolves

Should return hasWon=true for Best Picture (if present in CSV) and some data from OMDb:
Example:

```
{
    "imdbId": "tt0099348",
    "title": "Dances with Wolves",
    "year": "1990",
    "hasWon": true,
    "winnerYears": [
        "1990 (63rd)"
    ]
}
```

---
> 2.POST /api/movies/rating:

```
{
    "imdbId":"tt0099348",
    "score":10.0
}
```

**Show show status 202 Accepted**

---

> 3. GET /api/movies/top-rated

Should Return:

```
[
    {
        "imdbId": "tt0099348",
        "title": "Dances with Wolves",
        "year": "1990",
        "rating": 10.0,
        "boxOfficeValue": "$184,208,848"
    }
]    
```

### Additional Testing

1. Call the [Rate movie API](http://localhost:8080/api/movies/top-rated) with different movies
2. Give a rating for each movie
3. Call the [Top Rated Movies API](http://localhost:8080/api/movies/rate) to check if the result is applied properly

### Unit & Integration

The application already has written Unit & Integration Tests which can executed seperately in your preferred IDE or via:

> mvn clean test