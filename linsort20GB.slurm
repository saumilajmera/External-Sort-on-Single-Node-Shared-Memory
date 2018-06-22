#!/bin/bash

#SBATCH --nodes=1
#SBATCH --output=linsort20GB.log


START_TIME=$SECONDS
LC_ALL=C sort --parallel=4 -o /tmp/dataSorted.out /input/data-20GB.in 
ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "Total time in seconds for sorting 20 GB file in 4 threads using sort is"
echo "$ELAPSED_TIME"
valsort /tmp/dataSorted.out
