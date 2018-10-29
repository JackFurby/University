In this coursework I analysed 2 sorting algorithms (counting sort and insertion sort) by creating a test bed before analysing my results. 

To compile the source code navigate to the folder holding the file from a terminal and execute the following command:
javac *.java

To run the code execute the following command:
java source

In its current state this will run all tests and output two CSV files containing run-times which can then be plotted. To run the algorithm separately and see its
progress print statements and additional tests have been included but are commented out. The print statements can be found within the algorithm implementations (found on lines 123 and 143).
The code to call these is within the main function.

This includes:

This will set an array to sort - int b[] = {1,2,3,4,5,6,7,8};

This will call counting sort with array b containing 8 elements and with the max element value of 9 - countingSort(b, 8, 9);

This will call insertion sort with array b containing 8 elements - insertionSort(b, 8);
