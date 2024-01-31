# MeetupMemory
Accompanying repo for the Amsterdam Java User Group Meetup

## Running the code directly
The quickest way, would be to run:
- `java -jar MeetupMemory.jar single` for a single threaded example
- `java -jar MeetupMemory.jar multi` for a multi threaded example

This requires Java 17. 
One could also build the jar themselves using `gradle jar` and running the resulting jar file in `build/libs`.` 

## Running in a Docker container
To build the Docker container for the examples, run:
- `docker build -t meetup .`

When running the container, one should specify the following environment variable:
- `THREAD_MODE`: required, either 'single' or 'multi'

It is possible to set different optional `LD_PRELOAD` to use jemalloc or mimalloc.
Or tune any of the chosen memory allocators.

## Accompanying scripts
It is also possible to do the steps above with the scripts in `scripts/`