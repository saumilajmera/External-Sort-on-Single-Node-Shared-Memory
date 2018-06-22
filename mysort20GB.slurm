#!/bin/bash

#SBATCH --nodes=1
#SBATCH --output=mysort20GB.log

echo 'Generating Files of 100MB'
START_TIME=$SECONDS
split -l 1000000 -a 3 -d --additional-suffix=.txt /input/data-20GB.in /tmp/file- 
echo 'Time in seconds to generate files is'
ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "$ELAPSED_TIME"
javac *.java
echo 'Sort Started'
java -Xms7g -Xmx7g MySort /input/data-20GB.in 03 200 10 /tmp/dataSorted.out
echo 'Sort Completed'
echo "Running Valsort to verify output file"
valsort /tmp/dataSorted.out
echo "Total time in seconds for splitting 20GB file into 200 files of 100MB and sorting with 10 threads and verifying the output file is"
ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "$ELAPSED_TIME"
