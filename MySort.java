import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySort {

	private static int threadCount;
	private static int noOfFiles;
	private static String blockName;

	static String Inptfilepath;
	static String Outputfilepath;
	static long linesPerFile;

	private static List<String> DividedfileList = new ArrayList<String>();
	private static long totalLinesInMainFile;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Inptfilepath = args[0];
		blockName = args[1];
		noOfFiles = Integer.parseInt(args[2]);
		threadCount = Integer.parseInt(args[3]);
		Outputfilepath = args[4];

		
		System.out.println("Running Threads: " + threadCount);
		System.out.println("Total Files: " + noOfFiles);
		// System.out.println("File Name: "+blockName);
		System.out.println("Input File Name: " + Inptfilepath);

		double startTime = System.currentTimeMillis();

		final List<Integer> fileIndexes = new ArrayList<Integer>();
		for (int i = 0; i < noOfFiles; i++) {
			DividedfileList.add("/tmp/file-" + String.format("%" + blockName + "d", i) + ".txt");
			fileIndexes.add(i);
		}

		File file = new File(Inptfilepath);
		totalLinesInMainFile = file.length();
		linesPerFile = totalLinesInMainFile / noOfFiles / 100;

		Thread[] mythread = new Thread[threadCount];

		for (int count = 0; count < threadCount; count++) {
			mythread[count] = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for (int filePerThread = 0; filePerThread < noOfFiles / threadCount; filePerThread++) {

							int fileIndex = ((Integer.parseInt(Thread.currentThread().getName()) * noOfFiles)
									/ (threadCount)) + filePerThread;
							doSort(fileIndexes.get(fileIndex));
						}
					} catch (Exception e) {
					}
				}
			});

			mythread[count].setName(Integer.toString(count));
			mythread[count].start();
		}

		for (int count = 0; count < threadCount; count++) {
			mythread[count].join();
		}

		System.out.println(
				"Time taken in seconds to sort all files: " + ((System.currentTimeMillis() - startTime) / 1000));

		System.out.println("Merging sorted files");

		mergeSortedFiles();

		System.out.println("Total time taken in seconds to sort and merge single file: "
				+ ((System.currentTimeMillis() - startTime) / 1000));
	}

	// Function calls reading of individual file, sorts data and calls writing
	// sorted file function
	static void doSort(int location) throws Exception {

		List<String> sortedList = null;

		sortedList = readDataFile(DividedfileList.get(location));
		Collections.sort(sortedList);
		writeFile(sortedList, location);
		sortedList.clear();

	}

	static void mergeSortedFiles() throws IOException {
		// TODO Auto-generated method stub

		BufferedReader[] bufReaderArray = new BufferedReader[noOfFiles];
		List<String> intermediateList = new ArrayList<String>();
		List<String> lineDataList = new ArrayList<String>();

		for (int file = 0; file < noOfFiles; file++) {
			bufReaderArray[file] = new BufferedReader(new FileReader(DividedfileList.get(file)));

			String fileLine = bufReaderArray[file].readLine();

			if (fileLine != null) {
				intermediateList.add(fileLine.substring(0, 10));
				lineDataList.add(fileLine);
			}
		}

		BufferedWriter bufw = new BufferedWriter(new FileWriter(Outputfilepath));

		// Merge files into one file

		for (long lineNumber = 0; lineNumber < totalLinesInMainFile; lineNumber++) {
			String sortString = intermediateList.get(0);
			int sortFile = 0;

			for (int iter = 0; iter < noOfFiles; iter++) {
				if (sortString.compareTo(intermediateList.get(iter)) > 0) {
					sortString = intermediateList.get(iter);
					sortFile = iter;
				}
			}

			bufw.write(lineDataList.get(sortFile) + "\r\n");
			intermediateList.set(sortFile, "-1");
			lineDataList.set(sortFile, "-1");

			String nextString = bufReaderArray[sortFile].readLine();

			if (nextString != null) {
				intermediateList.set(sortFile, nextString.substring(0, 10));
				lineDataList.set(sortFile, nextString);
			} else {
				lineNumber = totalLinesInMainFile;

				List<String> ListToWrite = new ArrayList<String>();

				for (int file = 0; file < intermediateList.size(); file++) {
					if (lineDataList.get(file) != "-1")
						ListToWrite.add(lineDataList.get(file));

					while ((sortString = bufReaderArray[file].readLine()) != null) {
						ListToWrite.add(sortString);
					}
				}

				Collections.sort(ListToWrite);
				int index = 0;
				while (index < ListToWrite.size()) {
					bufw.write(ListToWrite.get(index) + "\r\n");
					index++;
				}

			}
		}
		bufw.close();
		for (int file = 0; file < noOfFiles; file++)
			bufReaderArray[file].close();
	}

	// Read the File Path to read the File to sort
	static List<String> readDataFile(String filePath) throws Exception {
		// System.out.println(filePath);
		List<String> sortList = new ArrayList<String>();

		FileReader file = new FileReader(new File(filePath));

		BufferedReader bufRead = new BufferedReader(file);

		for (long line = 0; line < linesPerFile; line++) {

			sortList.add((bufRead.readLine().toString()));
		}

		bufRead.close();
		return sortList;
	}

	// Individual Sorted File Writing Function
	static void writeFile(List<String> sortList, int fileIndex) throws IOException, InterruptedException {
		FileWriter filewrite = new FileWriter(new File(DividedfileList.get(fileIndex)));
		BufferedWriter bufw = new BufferedWriter(filewrite);

		int fileLoc = 0;

		while (fileLoc != sortList.size()) {
			bufw.write(sortList.get(fileLoc) + "\r\n");
			fileLoc++;
		}
		bufw.close();
	}

}
