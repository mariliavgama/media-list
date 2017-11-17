# Media List

- Displays items from a NY times feed in a Recycler View, including images
- MVP architecture
- Used Retrofit 2 for the GET call, Universal Image Loader for images loading/caching
- At first load, GET items from remote datasource and store in cache and database. If cached items are available, use them. If app is restarted, load items from database. 
- An "Update" button let's the user clean cache and load the latest items. Used that to demo the use of an Observable timer and an Observer (reactivex lib)
- From time to time (this delay can be changed in code), the app will load the latest items. For that, created an alarm that extends BroadcastReceiver.
- The message "Showing items" is displayed when the items are displayed by the Fragment. If new items can't be retrieved either due to an error or if the app is offline when trying to get items from the remote data source, a message saying "Unable to load new items" is displayed
- Universal Image Loader is set to cache images in memory. Cache in SD Card is turned off, but can be enabled by uncommenting one line. The app works offline and the list is displayed, but if SD card caching images is off images may display an empty space when the app is restarted while offline.
- There are some basic Unit tests, used Mockito and JUnit for those
- Future improvement and research: More Unit tests, Instrumental Tests using dependency injection (Dagger 2) for mocking dependencies and data sources for the tests.
