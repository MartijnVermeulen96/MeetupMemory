#!/usr/bin/env bash

if [ "${THREAD_MODE}" == "" ]; then
  echo "Please specify THREAD_MODE as an environment variable, 'single' or 'multi'"
  exit 1
fi

memory_limit=$(cat /sys/fs/cgroup/memory.max 2> /dev/null)

echo "Starting with;"
echo "- Thread mode: ${THREAD_MODE}"
echo "- Memory limit: ${memory_limit:="?"} bytes"

if [ "$MALLOC_ARENA_MAX" != "" ]; then
  echo "MALLOC_ARENA_MAX: $MALLOC_ARENA_MAX"
fi

if [ "$LD_PRELOAD" != "" ]; then
  echo "LD_PRELOAD: $LD_PRELOAD"
fi

# Running the JVM with NativeMemoryTracking set to summary
exec java -XX:NativeMemoryTracking=summary -jar "${JAR_LOCATION:="MeetupMemory.jar"}" "${THREAD_MODE}"