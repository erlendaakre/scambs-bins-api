# scambs-bins-api
Parser and REST API proxy server for South Cambridgeshire Council Bins (iCalendar)

A intermediary rest api for https://www.scambs.gov.uk/bins/
Will parse the ical public link for your address and provide a more usable API.

Primarily made for smart home integration (reminding me to take out the right coloured bin)

uses Scala, Cats and Http4s

**NOTE: currently WIP, Parser done but REST api not started***

\* IE: it will turn a iCal file into `Seq[(Date, Seq[Bins])]`

## TODO
- [x] make composed event parser that repeats until lastLine
- [x] merge resulting Collection list on Date to get list of Blue + Green bins for certain dates
- [ ] cache iCal file to avoid making too many responses
- [ ] config file for timeout + url + location id
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
