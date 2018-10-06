/*
 * Name: Jack Furby
 * Student number: 1619450
 */

 import java.util.Scanner;
 import java.util.ArrayList;
 import java.util.List;

/*
 * A command-line application that shortens a message.
 */
public class ShortenerUtility {

    //variables for shortener application
    private static Scanner sc = new Scanner( System.in );
    private static Shortener shortener;

    //main method
    public static void main( String[] args ) {

        String abbrevOut;
        String consoleIn;

        //sets text to be shortened
        try {
            consoleIn = args[0]; //gets string from command input
        } catch ( ArrayIndexOutOfBoundsException boundsError ) { //no user input entered
            System.out.println( "No input text found" );
            consoleIn = "";
        }

        try { //tests to see if a differnt abbreviations file is being inputted
            String fileIn = args[1];
            shortener = new Shortener( fileIn );
            abbrevOut = shortener.shortenMessage( consoleIn.trim() ); //gets output test
            System.out.println( abbrevOut );
        } catch ( ArrayIndexOutOfBoundsException e ) { //shorten message with default abbreviations file
            shortener = new Shortener();
            abbrevOut = shortener.shortenMessage( consoleIn.trim() ); //gets output test
            System.out.println( abbrevOut );
        }

        //exit application
        System.exit( 0 );
    }
}
