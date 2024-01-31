#!/usr/bin/env bash

SCRIPT=$(realpath "$0")
SCRIPTPATH=$(dirname "$SCRIPT")

echo "Spinning up some Docker containers"

docker run --rm -d -e THREAD_MODE=single --name "Meetup_Single" --cpus 4 -m 2g meetup
docker run --rm -d -e THREAD_MODE=single -e MALLOC_MAX_ARENA=2 --name "Meetup_Single_MALLOC_MAX_ARENA-2" --cpus 4 -m 2g meetup
docker run --rm -d -e THREAD_MODE=multi --name "Meetup_Multi" --cpus 4 -m 2g meetup
docker run --rm -d -e THREAD_MODE=multi -e MALLOC_MAX_ARENA=2 --name "Meetup_Multi_MALLOC_MAX_ARENA-2" --cpus 4 -m 2g meetup

python "${SCRIPTPATH}/plot_container_memory.py"