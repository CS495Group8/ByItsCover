# ByItsCover 

# Sprint 3 Notes:
* We finished integration and are happy with our progress as we completed all of our goals. We integrated, improved the interface to make it cleaner and more attractive to the user, and added pricing information. Our main challenges from Sprint 2 were differing schedules and having to deal with deprecated code. Unfortunately, we still had difficulty with deprecated code when trying to work on the OCR side of things as Java is not really designed for that. The differing schedules was worked through as we were able to prioritize our meeting times to really dedicate toward working toward our goals.
* Feel free to check out our pull requests to see the process we use for merging
## Testing  
### Unit testing
- [Testing ISBN Class](https://github.com/CS495Group8/ByItsCover/blob/CurrentBook-refactor/app/src/test/java/com/example/byitscover/helpers/IsbnTest.java)
- [Testing Book Class](https://github.com/CS495Group8/ByItsCover/blob/CurrentBook-refactor/app/src/test/java/com/example/byitscover/helpers/BookTest.java)
- [Testing Barnes and Noble Scraper Class](https://github.com/CS495Group8/ByItsCover/blob/scrapersTestsAndComments/app/src/test/java/com/example/byitscover/scrapers/BarnesAndNobleScraperTest.java)
- [Testing Goodreads Scraper Class](https://github.com/CS495Group8/ByItsCover/blob/scrapersTestsAndComments/app/src/test/java/com/example/byitscover/scrapers/GoodreadsScraperTest.java)
  - When testing found a bug where if only title or author are searched, unpredictable results can occur. Have a current fix that works somewhat decently, but we plan on improving
- [Testing Google Books Scraper Class](https://github.com/CS495Group8/ByItsCover/blob/scrapersTestsAndComments/app/src/test/java/com/example/byitscover/scrapers/GoogleScraperTest.java)
- [Testing Storygraph Scraper Class](https://github.com/CS495Group8/ByItsCover/blob/scrapersTestsAndComments/app/src/test/java/com/example/byitscover/scrapers/StorygraphScraperTest.java)
### Other tests
* Camera Start - Click the "Search by Cover" Button. This brings up the camera. Clicking the button at the bottom of the camera page results in an image capture request. 
* Camera Access Request - If you click the "Search by Cover" button and it doesn't already have access to write to external storage and open the camera, it will pop up a request to access those. Once allowed, the camera will open like normal.
* OCR - To test the OCR itself, a set of sample covers with known titles is added and run through the GetTitle function, and compared to the result. If the returned value is at least 90 percent similar, the test is considered passed.
* Title/Author recognition - To test the functionality of the parser that accepts the OCR results, a fake set of names and nouns are passed in, and the function returns the proper segmentation of the input into nouns and proper nouns, which is used to search for the book on various sites. If the program successfully segments the phrases and names, the test is considered passed.
