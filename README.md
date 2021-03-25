# ByItsCover 

# Sprint 2 Notes:
* The first version of our book cover recognition software was created during this sprint and is working as intended, but some changes will need to be made to the repository as a whole before it can be integrated. It is currently not in the repository, Ripley was testing several different programs and methods for it, so he did not add it to avoid polluting the codebase with things that were going to be discarded. Integration work is expected to begin during week one of sprint 3 (week of 3/29) and the OCR software will be added to the repo then after minor refactors to ensure it can be added as smoothly as possible.
## Testing  
### Unit testing
* [Testing ISBN Class](https://github.com/CS495Group8/ByItsCover/blob/CurrentBook-refactor/app/src/test/java/com/example/byitscover/helpers/IsbnTest.java)
* [Testing Book Class](https://github.com/CS495Group8/ByItsCover/blob/CurrentBook-refactor/app/src/test/java/com/example/byitscover/helpers/BookTest.java)  
### Other tests
* Camera Start - Click the "Search by Cover" Button. This brings up the camera. Clicking the button at the bottom of the camera page results in an image capture request. 
* Camera Access Request - If you click the "Search by Cover" button and it doesn't already have access to write to external storage and open the camera, it will pop up a request to access those. Once allowed, the camera will open like normal.
* OCR - To test the OCR itself, a set of sample covers with known titles is added and run through the GetTitle function, and compared to the result. If the returned value is at least 90 percent similar, the test is considered passed.
* Title/Author recognition - To test the functionality of the parser that accepts the OCR results, a fake set of names and nouns are passed in, and the function returns the proper segmentation of the input into nouns and proper nouns, which is used to search for the book on various sites. If the program successfully segments the phrases and names, the test is considered passed.

## Branches
* [CurrentBook-refactor](https://github.com/CS495Group8/ByItsCover/tree/CurrentBook-refactor) - This was Jack's main branch that he's been working on. Some of the initial project organization/structure was deprecated and not sutiable for other parts of the project. Jack worked through a lot of those changes on this branch and added more improvements. We hope to have this merged by the Demo for Sprint 2.
* [Emily-CameraX](https://github.com/CS495Group8/ByItsCover/tree/Emily-CameraX) - This is Emily's branch she is currently working on with getting the camera operable. Current timeline puts integration of this next week (week of 3/29).
* [allScrapers](https://github.com/CS495Group8/ByItsCover/tree/allScrapers) - This was Marc's main branch during this sprint as he worked to implement and integrate the remaining scrapers we wanted to have. We also hope to have this merged by the Demo for Sprint 2 but it needs to have some integration happening once the CurrentBook-refactor branch is merged before it can be. 
* [gh-pages](https://github.com/CS495Group8/ByItsCover/tree/gh-pages) - This branch just holds our website info and related items.
* [goodreads-fix](https://github.com/CS495Group8/ByItsCover/tree/goodreads-fix) - This branch was one of Marc's where he helped Jack debug something that was going on with the Goodreads scraper on his refactoring branch 
* [marc/BaNScraper](https://github.com/CS495Group8/ByItsCover/tree/marc/BaNScraper) - This branch was initially seperated from the other scrapers as it was a bit bug filled when implementing. Not really used and will eventually be deleted not merged.
* [marc/amzScraper](https://github.com/CS495Group8/ByItsCover/tree/marc/amzScraper) - Same as the BaN scraper except that Amazon was left out purposefully as they make it pretty much impossible to scrape and the only way to get access to their API is through a paid account so the decision was made to switch sites for the fourth scraper. Will eventually be deleted.
* [tempRefactorBranch](https://github.com/CS495Group8/ByItsCover/tree/tempRefactorBranch) - This was another of Marc's branches where he branched off of the refactoring branch and changed the code to get off the Google Search API in order to test some things as it was giving some errors. Will be deleted eventually once the issues are nailed down.
