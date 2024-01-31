FROM eclipse-temurin:17-jre

RUN apt-get update && apt-get install -y \
    libjemalloc2 \
    libmimalloc2.0 \
 && rm -rf /var/lib/apt/lists/*

# Copying files
COPY MeetupMemory.jar MeetupMemory.jar 
COPY entrypoint.sh entrypoint.sh

ENTRYPOINT ["/bin/bash", "entrypoint.sh"]