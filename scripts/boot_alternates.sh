#!/usr/bin/env bash

SCRIPT=$(realpath "$0")
SCRIPTPATH=$(dirname "$SCRIPT")

echo "Spinning up some Docker containers"

docker run --rm -d -e THREAD_MODE=multi --name "Meetup_Malloc" --cpus 4 -m 2g meetup
docker run --rm -d -e THREAD_MODE=multi -e MALLOC_MAX_ARENA=2 --name "Meetup_Malloc_MALLOC_MAX_ARENA-2" --cpus 4 -m 2g meetup
docker run --rm -d -e THREAD_MODE=multi -e LD_PRELOAD="/usr/lib/x86_64-linux-gnu/libjemalloc.so.2" --name "Meetup_Jemalloc" --cpus 4 -m 2g meetup
docker run --rm -d -e THREAD_MODE=multi -e LD_PRELOAD="/usr/lib/x86_64-linux-gnu/libmimalloc.so.2.0" --name "Meetup_Mimalloc" --cpus 4 -m 2g meetup

python "${SCRIPTPATH}/plot_container_memory.py"