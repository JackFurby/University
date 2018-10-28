import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class source {

	public static void main(String[] args) {
		// array a[] contains array sizes to be passed to the functions to record run-time

		//int a[] = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000, 13000, 14000, 15000};
		int a[] = {1000, 2236, 3000, 3606, 4123, 4583, 5000, 5385, 5745, 6083, 6403, 6708, 7000, 7280, 7550};

		// array b[] is an array to be sorted. To look at the results of either algorithm you will need to un-comment the print statements found within
		// the corrisponding functions (found on lines 123 and 143)

		//int b[] = {1,2,3,4,5,6,7,8};
		//countingSort(b, 8, 9);
		//insertionSort(b, 8);


		//The CSV outputs will be named depending on which algorithem it contains. As it is currently set up InsertionSort.csv will contain
		//3 lines with the first one showing testRunTimeWorst, line 2 containing testRunTimeAverage and line 3 containing testRunTimeBest.
		//CountingSort.csv contains 2 lines with hte first containg testRunTimeBad and the second with testRunTimeGood


		double testRunTimeWorst[] = getRunTime(a, "insertionSort", "worst", 3500);
		System.out.print("Insertion Sort worst: ");
		System.out.println(Arrays.toString(testRunTimeWorst));

		double testRunTimeAverage[] = getRunTime(a, "insertionSort", "average", 3500);
		System.out.print("Insertion Sort average: ");
		System.out.println(Arrays.toString(testRunTimeAverage));

		double testRunTimeBest[] = getRunTime(a, "insertionSort", "best", 3500);
		System.out.print("Insertion Sort best: ");
		System.out.println(Arrays.toString(testRunTimeBest));

		saveInsertionSort(testRunTimeWorst, testRunTimeAverage, testRunTimeBest); //saves results to CSV

		double testRunTimeGood[] = getRunTime(a, "countingSort", "good", 3500);
		System.out.print("Counting Sort good: ");
		System.out.println(Arrays.toString(testRunTimeGood));

		double testRunTimeBad[] = getRunTime(a, "countingSort", "bad", 3500);
		System.out.print("Counting Sort bad: ");
		System.out.println(Arrays.toString(testRunTimeBad));

		saveCountingSort(testRunTimeBad, testRunTimeGood); //saves results to CSV


	}

	// returns an array containing the run-time analysis for specified array sizes
	public static double[] getRunTime(int arraySize[], String sortType, String arrayType, int loops) {
		int warmUp = 1000; // removes high run-times before JIT improves the performance
		double runtime[] = new double[arraySize.length];
		for (int i = 0; i < arraySize.length; i++) { // For each array length to test
			long currentTime = 0L;
			for (int j = 0; j < loops; j++) { // Loop over the specified number of times (used to find average)
				if (sortType == "insertionSort") {

					int a[] = insertionSortArray(arraySize[i], arrayType); // Gets an array to sort in a specified format (best, worst, average)
					int n = a.length;
					long timeDone = insertionSort(a, n); // Sorts array and gets run-time
					if (j > warmUp) { // Discards run times before JIT kicks in
						currentTime += timeDone;
					}

				} else if (sortType == "countingSort") {

					int a[] = countingSortArray(arraySize[i], arrayType); // Gets an array to sort in a specified format (good, bad)
					int n = a.length;
					int k = n;
					if (arrayType == "bad") { // If running a bad example k = n squared
						k = n * n;
					}
					long timeDone = countingSort(a, n, k); // Sorts array and gets run-time
					if (j > warmUp) { // Discards run times before JIT kicks in
						currentTime += timeDone;
					}
				}
				if (j % 1000 == 0) { // Prints current status
					System.out.println(sortType + " - i: " + i + ", j: " + j);
				}

			}
			runtime[i] = currentTime / (loops - (warmUp + 1)); // Works out average run time
		}
		return runtime;
	}

	// makes array of specified size
	public static int[] insertionSortArray(int size, String type) {
		Random randomGenerator = new Random();
		int currentArray[] = new int[size];
		for (int i = 0; i < size; i++) {
			if (type == "best") { // adds elements to the array for a best run-time
				currentArray[i] = i;
			} else if (type == "worst") { //adds elements to the array for a worst run-time
				currentArray[size - 1 - i] = i;
			} else if (type == "average") { //adds elements to the array for a average run-time
				currentArray[i] = randomGenerator.nextInt(100);
			}
		}
		return currentArray;
	}

	// makes array of specified size
	public static int[] countingSortArray(int size, String type) {
		Random randomGenerator = new Random();
		int currentArray[] = new int[size];
		for (int i = 0; i < size; i++) {
			if (type == "good") { // adds elements to the array for a good run-time
				currentArray[i] = randomGenerator.nextInt(size);
			} else if (type == "bad") { // adds elements to the array for a bad run-time
				currentArray[i] = randomGenerator.nextInt(size * size);
			}
		}
		return currentArray;
	}

	// insertion sort - o(n) best, o(n^2) worst and average
	public static long insertionSort(int a[], int n) {
		long startTime = System.nanoTime(); // get start time
		for(int i = 1; i < n; i++) {
			int item = a[i];
			int j = i - 1;
			//System.out.println("item: " + item);
			while (j >= 0 && a[j] > item) {
				a[j + 1] = a[j];
				j = j - 1;
			}
			a[j + 1] = item;
			//System.out.print("A: ");
			//System.out.println(Arrays.toString(a));
		}
		long endTime = System.nanoTime(); // get end time
		long currentTime = endTime - startTime; // work out total time
		return currentTime;
	}

	// counting sort - o(n+k)
	public static long countingSort(int a[], int n, int k) {
		long startTime = System.nanoTime();
		int c[] = new int[k];
		int b[] = new int[n];
		for(int i = 0; i < k-1; i++) {
			c[i] = 0;
		}
		//System.out.print("C: ");
		//System.out.println(Arrays.toString(c));
		for(int j = 0; j < n; j++) {
			c[a[j]] = c[a[j]] + 1;
		}
		//System.out.print("C: ");
		//System.out.println(Arrays.toString(c));
		for(int i = 1; i < k; i++) {
			c[i] = c[i] + c[i - 1];
		}
		//System.out.print("C: ");
		//System.out.println(Arrays.toString(c));
		//System.out.print("A: ");
		//System.out.println(Arrays.toString(a));
		for(int j = n - 1; j >= 0; j--) {
			b[c[a[j]] - 1] = a[j];
			c[a[j]] = c[a[j]] - 1;
			//System.out.println("j: " + j);
			//System.out.print("B: ");
			//System.out.println(Arrays.toString(b));
			//System.out.print("C: ");
			//System.out.println(Arrays.toString(c));
		}
		long endTime = System.nanoTime();
		long currentTime = endTime - startTime;
		return currentTime;
	}

	// saves runtime for InsertionSort to a csv file
	public static void saveInsertionSort(double[] runtime1, double[] runtime2, double[] runtime3) {
		PrintWriter printWriter = null;                                     // <----- to do
		try {
			printWriter = new PrintWriter(new File("runtimeInsertionSort.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < runtime1.length; i++) {
			stringBuilder.append(runtime1[i]);
			if (i != runtime1.length) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("\n");

		for (int i = 0; i < runtime2.length; i++) {
			stringBuilder.append(runtime2[i]);
			if (i != runtime2.length) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("\n");

		for (int i = 0; i < runtime3.length; i++) {
			stringBuilder.append(runtime3[i]);
			if (i != runtime3.length) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("\n");

		printWriter.write(stringBuilder.toString());
		printWriter.close();
		System.out.println("done!");
	}

	// saves runtime for countingSort to a csv file
	public static void saveCountingSort(double[] runtime1, double[] runtime2) {
		PrintWriter printWriter = null;                                     // <----- to do
		try {
			printWriter = new PrintWriter(new File("runtimeCountingSort.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < runtime1.length; i++) {
			stringBuilder.append(runtime1[i]);
			if (i != runtime1.length) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("\n");

		for (int i = 0; i < runtime2.length; i++) {
			stringBuilder.append(runtime2[i]);
			if (i != runtime2.length) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("\n");

		printWriter.write(stringBuilder.toString());
		printWriter.close();
		System.out.println("done!");
	}

}
