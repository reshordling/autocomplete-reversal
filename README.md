# autocomplete-reversal
Scoring autocomplete keywords by volume

# how to start
Use Java 8. Type

`./gradlew clean bootRun`

# Logic
* Expected that found results have more or less equal popularity (hint), and their popularity is less than the popularity of the keyword. My assumption was that moving downwards (taking autocompete search results and using them in search) I get each time less popular results. After repeating a few times I will reach least popular elements. The idea was to measure the number of steps required to go from the initial keyword till I reach some keyword without autocomplete options, and the longer path is, the more popular (the more search vatiations) the initial keyword was.
* I iterated autocomplete search results in depth until reached the element without autocompletion. Also removed noise - e.g categories, duplicate names. Turned out that this approach is not the best. Probably I had to move upwards - partially removing the initial keyword elements and checking if it still found in the reduced form.
* Turned out that different autocomplete elements have different number of subelements - e.g "iphone charger cable" and "iphone charger 11" have different number of sub-combinations, so the ordering was actually important for this approach.
* Not very precise because ordering matters (it would not matter if I had moved upwards, not downwards as I did).

# Solution
* gradle + java 8
* Spring Boot + WebFlux to allow async processing and honor SLA of 10 sec
* Initially implemented with caching but then understood that results can change on the fly, so removed it
* Code is documented and covered by tests - was necessary to test web scapper (random user agent on each request)
