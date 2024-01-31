#!/usr/bin/env bash

gradle jar
cp build/libs/MeetupMemory.jar MeetupMemory.jar
docker build -t meetup .;