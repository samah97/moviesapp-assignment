While building the application, I assume that:

- CSV file structure remains unchanged (headers)
- CSV Ingestion through the application needed to be implemented instead of database migration
- OMDb API is the source of truth for movie details
- IMDb ID is the unique identifier for each movie
- Ratings are anonymous and stored in DB
- Only “Best Picture” category is required now, but design supports future extension
- For possible omdb response changes (such as value of Box Office, especially for new movie), movie data shouldn't be
  persisted
