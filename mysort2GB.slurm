#!/bin/bash

#SBATCH --nodes=1
#SBATCH --output=mysort2GB.log

echo 'Generating Files of 100MB'
START_TIME=$SECONDS
split -n 20 -a 2 -d --additional-suffix=.txt /input/data-2GB.in /tmp/file- 
echo 'Time in seconds to generate files is'
ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "$ELAPSED_TIME"
javac *.java
echo 'Sort Started'
java -Xms7g -Xmx7g MySort /input/data-2GB.in 02 20 4 /tmp/dataSorted.out
echo 'Sort Completed'
echo "Running valsort to verify the output file"
valsort /tmp/dataSorted.out
echo "Total time in seconds for splitting 2GB file into 20 files of 100MB and sorting with 4 threads and verifying the output file is"
ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "$ELAPSED_TIME"
