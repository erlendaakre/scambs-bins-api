# scambs-bins-api
Parser, CLI and REST API proxy server for South Cambridgeshire Council Bins (iCalendar) using Scala, Cats and Http4s

Will parse your public iCal file from https://www.scambs.gov.uk/bins/ and provide a more usable API or run as a command line client.

Primarily made for smart home integration (reminding me to take out the right coloured bin)

Command Line Interface will return a human-readable description or produce no output if tomorrow is not the bin collection day.
e.g.  *The Blue and Green bin will be collected tomorrow*


The proxy api returns a chronologically sorted Json array of the following "collection" object:
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
- [ ] location id hardcoded, fix (param for CLI) 
- [ ] cache remote iCal file (`Seq[(Date, Seq[Bins])]`?) to avoid repeated slow web requests for API

## Setup
* Getting your relevant link from [scambs website](https://www.scambs.gov.uk/bins/)
  * e.g. : https://refusecalendarapi.azurewebsites.net/calendar/ical/nnnnnn* 

## Usage
* Automatically run the CLI daily to check if tomorrow is collection day, if CLI outputs text send that to your pushover/google home/whatever
