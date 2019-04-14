# Popular Movies: Stage 2

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, this app is built to allow users to discover the most popular movies playing.

-  [x]  Passed thought [Udacity's PROJECT SPECIFICATION: Popular Movies, Stage 2 rubric](https://review.udacity.com/#!/rubrics/67/view)
-  [x]  Build according to [Udacity's Popular Movies App Implementation Guide](https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.7sxo8jefdfll)
-  [x]  Passed thought [Udacity Android Developer Nanodegree - Core App Quality Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html)

## Screenshots
<p align="center"> <img src="/pictures/screenshot1.png" width="250"> <img src="/pictures/screenshot2.png" width="250"> <img src="/pictures/screenshot3.png" width="250"> </p>

> Please get your API key form [this link](https://www.themoviedb.org/settings/api) or create yours if you don't have an account sign up from [this link](https://www.themoviedb.org/account/signup).
> Add API key to retrofit > MoviesAPI > apiKey_value (there is a TODO there)

## What this app is doing
-   Allowing users to view and play trailers ( either in the youtube app or a web browser).
-   Allowing users to read reviews of a selected movie.
-   Allowing users to mark a movie as a favorite in the details view by tapping a button(star).
-   Creating a database to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).
-   Showing an additional pivot to show users favorites collection.

## What I have learnt from the app
-   Building a fully featured application that looks and feels natural on the latest Android operating system.

## Improves to be a better project
-   Handling user fast clicking on the sorting options
-   Making 2 separate SQLite database one for MainActivity and one for DetailsActivty and link them together to get better performance
-   Adding some animations and first launch tips
-   Implementing YouTube player embedded in the app
-   Using Snacbar instead of Toast
