###### Compile ######

make all


###### Steps to Run mysort2GB and mysort20GB ############


1) Run the Makefile by make all which will compile the MySort.java code with required flags into executable MySort class file 

2) Run script file mysort2GB.slurm and mysort20GB.slurm using sbatch mysort2GB.slurm

3) Calling MySort class file with parameters -Xms7g and -Xmx7g to set minimum and maximum heap size upto 7GB. There are 5 inputs given - 
	a)Input file path 
	b)Number of characters used in naming file 
	c)Number of Files 
	d)Thread Count 
	e)Output file path

5) Output is written into file named mysort2GB.log and mysort20GB.log which contains different timings written as per operation and valsort verified output 

###### Steps to Run linsort2GB and linsort20GB ############

1) Run script file linsort2GB.slurm and linsort20GB.slurm using sbatch linsort2GB.slurm

2) Output is written into file named mysort2GB.log and mysort20GB.log which contains total time of operation and valsort verified output 
