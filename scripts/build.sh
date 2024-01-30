#!/usr/bin/env bash

gradle jar
docker build -t meetup .;
cp build/libs/MeetupMemory.jar MeetupMemory.jar