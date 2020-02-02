# scambs-bins-api
Parser and REST API proxy server for South Cambridgeshire Council Bins (iCalendar)

A intermediary rest api for https://www.scambs.gov.uk/bins/
Will parse the iCal public link for your address and provide a more usable API.

Primarily made for smart home integration (reminding me to take out the right coloured bin)

uses Scala, Cats and Http4s

**NOTE: currently WIP, Parser done, Rest api is query only and very basic**

returns a Json array of the following object:
```json
{
    "date" : {
      "year" : 2020,
      "month" : 3,
      "day" : 20
    },
    "bins" : [
      "GreenBin",
      "BlueBin"
    ]
}
```

## TODO
- [x] make composed event parser that repeats until lastLine
- [x] merge resulting Collection list on Date to get list of Blue + Green bins for certain dates
- [ ] rewrite using ZIO
- [ ] refine date types
- [ ] cache remote iCal file (`Seq[(Date, Seq[Bins])]`?) to avoid repeated slow web requests
- [ ] config file for timeout + url + location id. use pureconfig
- [ ] define rest api endpoints
- [ ] create algorithms for doing queries
- [ ] wire up everything in http4s

## Setup
TODO describe getting link/ID from scambs website
   https://refusecalendarapi.azurewebsites.net/calendar/ical/nnnnnn

TODO how to run? sbt or fat jar?
TODO make fat jar and put on java docker image?
TODO set up CI to notify if test fails (format changes)

## Usage
TODO describe API
