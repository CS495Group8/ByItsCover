## Goals

### What does it do?

1a. User Takes a Picture of the Cover
1b. User Types in Title and Author

2a. Application Analyzes Cover to Find Relevant Information
2b. Application Uses Inputted Title and Author to Find Relevant Information

3. Application Displays Reviews on Book from Multiple Sources, including Goodreads, Storygraph, Barnes & Noble, and Google Books

### Why is this important?

It allows for easy retrieval of book information without complicated search inquiries. Taking a picture of the book ensures you find the correct book in one simple step.

It consolidates reviews inot an easy-to-find location across platforms. Different platforms attract different types of reviewers, so reading reviews from multiple sites helps to generate a bigger picture of the book.

### Who would use this? ... READERS

For those people who go into the bookstore without a clear book in mind. This app would assist in the browsing process by giving readers outside sources to help them judge what books they want to read.

For choosing what to read next. A common struggle for avid book readers is conquering their ever-growing To-Be-Read piles. This would give them the information they need to decide what to read with a simple photo.

## Approach
1. Set up base app

2. Enable camera access, and capture ability

3. Setup ability to extract data 

4. Build code to access review information

5. Consolidate and present review information

### Tools
* Java
* Android Studio

### APIs
* Jsoup

### Framework
* Standard Android

### Experience Description

1. User enters main page and can select to search by cover or by title/author

2a. If they search by cover, as intended, they will take a picture of their cover and the app will extract the title/author information

2b. If they choose to search by title/author, they will go to a page to enter that information

3. Once the program has the necessary information, it will give it to the webscrapers to get the reviews from their respective websites

4. Once we have all of the reviews, we will consolidate it on a review page for the user to see, containing the title, author, cover, average review, and the actual reviews found on the other sites

## Team Members

Marc Tuthill

Emily Schroeder

Ripley Ryan

Jack O'Donohue

## Demo Video

{% include youtubePlayer.html id="waj57hCvObs" %}

## Link to Repo

https://github.com/CS495Group8/ByItsCover

