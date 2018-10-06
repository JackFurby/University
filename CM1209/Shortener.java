/*
* Name: Jack Furby
* Student number: 1619450
*/

import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Shortener {

    private static ShortenerFrame ShortenerFrame = new ShortenerFrame();

    // This class is only a starting point. You should complete all members
    // below, but you may also need to add other fields and methods to
    // finish the implementation as per the question on the assignment sheet.

    private Map<String, String> abbrev = new TreeMap<String,String>( String.CASE_INSENSITIVE_ORDER );
    private static int maxWord; //set to largest abbreviation

    /*
     * Default constructor that will load a default abbreviations text file.
     */
    public Shortener() {
        maxWord = 0;
        try {
            Scanner in = new Scanner( new File( "abbreviations.txt" ) );
            while ( in.hasNextLine() ) {
                String[] line = new StringBuffer( in.nextLine() ).toString().split( "," );
                abbrev.put( line[0].replace( String.valueOf( ( char ) 160 ), " " ).trim(), line[1].replace( String.valueOf( ( char ) 160 ), " " ).trim() );
                String[] words = line[0].split( "((?<=[\\s])|(?=[\\s]))" ); //used to count words in abbreviation
                if ( words.length > maxWord ) {
                    maxWord = words.length;
                }
            }
        } catch ( Exception e ) {
            System.out.println( e ); //used for command line
            ShortenerFrame.fileUploadError = e.toString(); //used for GUI
        }
    }

    /*
     * Constructor that will load the abbreviations file represented by the
     * File parameter.
     */
    public Shortener( File inAbbreviationsFile ) {
        maxWord = 0;
        try {
            Scanner in = new Scanner( inAbbreviationsFile );
            while ( in.hasNextLine() ) {
                String[] line = new StringBuffer( in.nextLine() ).toString().split( "," );
                abbrev.put( line[0].replace( String.valueOf( ( char ) 160 ), " " ).trim(), line[1].replace( String.valueOf( ( char ) 160 ), " " ).trim() );
                String[] words = line[0].split( "((?<=[\\s])|(?=[\\s]))" ); //used to count words in abbreviation
                if ( words.length > maxWord ) {
                    maxWord = words.length;
                }
            }
        } catch ( Exception e ) {
            System.out.println( e );
            ShortenerFrame.fileUploadError = e.toString(); //used for GUI
        }
    }

    /*
     * Constructor that will load the abbreviations file that the String
     * parameter is a file path for.
     */
    public Shortener( String inAbbreviationsFilePath ) {
        maxWord = 0;
        try {
            Scanner in = new Scanner( new File( inAbbreviationsFilePath ) );
            while ( in.hasNextLine() ) {
                String[] line = new StringBuffer( in.nextLine() ).toString().split( "," );
                abbrev.put( line[0].replace( String.valueOf( ( char ) 160 ), " " ).trim(), line[1].replace( String.valueOf( ( char ) 160 ), " " ).trim() );
                String[] words = line[0].split( "((?<=[\\s])|(?=[\\s]))" ); //used to count words in abbreviation
                if ( words.length > maxWord ) {
                    maxWord = words.length;
                }
            }
        } catch ( Exception e ) {
            System.out.println( e );
            ShortenerFrame.fileUploadError = e.toString(); //used for GUI
        }
    }

    /*
     * This method attempts to shorten a word by finding its abbreviation. If
     * no abbreviation exists for this word, then this method will return the
     * original (i.e., unshortened) word.
     *
     * You may assume that words are always lower case.
     *
     * `inWord` should be a single word (no spaces). It may optionally be
     * followed by one of the five following punctuation characters:
     *   ,
     *   ?
     *   .
     *   !
     *   ;
     * If one of these characters is at the end of the word this method will
     * shorten the word (ignoring the punctuation) and return the shortened
     * word with the punctuation character at the end.
     * For example,
     *     shortenerObject.shortenWord( "hello?" )
     * should return
     *     "lo?"
     *
     * You may assume that words are always lower case.
     */
    public String shortenWord( String inWord ) { //this method has been reedesigned and added to shortedMessage and therefore shortedWord is no longer used.

        String reWord;

        if (abbrev.containsKey( inWord.replaceAll( "[,;?.!]{1}$", "" ) ) ) {
            reWord = abbrev.get( inWord.replaceAll( "[,;?.!]{1}$", "" ) );
        } else {
            reWord = inWord.replaceAll( "[,;?.!]{1}$", "" );
        }
        reWord = reWord + findLastChar( inWord );
        return reWord ;
    }
    /*
     * Attempts to shorten a message by replacing words with their
     * abbreviations.
     *
     * You may assume that messages are always lower case.
     *
     * Punctuation characters (,?.!;) should be retained after shortening. See
     * `shortenWord( String inWord )` for more information.
     */
    public String shortenMessage( String inMessage ) {

        //variables for shortenMessage
        String[] splitMessage = inMessage.split("((?<=[\\s\\r\\n])|(?=[\\s\\r\\n]))");
        List<String> abbrevMessage = new ArrayList<String>();

        //tests input message for abbreviations
        for ( int i = 0; i < splitMessage.length; i++ ) { //runs through this loop for every word in inMessage
            List<String> currentAbbrev = new ArrayList<String>(); //list is created for final output
            if ( abbrev.containsKey( splitMessage[i].replaceAll( "[,;?.!]{1}$", "" ) ) ) { //finds abbreviation for single words
                abbrevMessage.add( abbrev.get( splitMessage[i].replaceAll( "[,;?.!]{1}$", "" ) ) + findLastChar( splitMessage[i] ) );
            } else if ( i + 1 != splitMessage.length ) { //finds abbreviation for multiple words
                abbrevBuilder:
                for ( int j = 0; j < splitMessage.length - ( i ); j++ ) { //adds one word from the input to try to find a abbreviation
                    if ( j <= maxWord - 1 ) { //checks to see if word lengh is in range with abbreviations
                        currentAbbrev.add( splitMessage[i + j] );
                        if ( abbrev.containsKey( String.join( "", currentAbbrev ).replaceAll( "[,;?.!]{1}$", "" ) ) ) {
                            abbrevMessage.add( abbrev.get( String.join( "", currentAbbrev ).replaceAll( "[,;?.!]{1}$", "" ) ) + findLastChar( String.join( " ", currentAbbrev ).trim() ) );
                            i += j;
                            break abbrevBuilder; //abbreviation found
                        } else if ( abbrev.containsKey( String.join( "", currentAbbrev) ) ) { //tests for abbreviation including punctuation
                            abbrevMessage.add( abbrev.get( String.join( "", currentAbbrev ) ).trim() );
                            i += j;
                            break abbrevBuilder; //abbreviation found
                        }
                    } else { //abbreviation not found
                        abbrevMessage.add( splitMessage[i] );
                        break abbrevBuilder;
                    }
                    if ( j + 1 == splitMessage.length - ( i ) ) {
                        abbrevMessage.add( splitMessage[i] ); //abbreviation not found
                    }
                }
            } else if ( i + 1 == splitMessage.length ) {
                abbrevMessage.add( splitMessage[i] ); //abbreviation not found
            }
        }
        return String.join( "", abbrevMessage ); //returns the list of the abbreviation message as a string
    }

    /*finds the last character in a word and if it matches a symbol in a list it will be returned.
    This is used to ensure symbols are kept for the final output*/
    public String findLastChar( String inWord ) {

        //variables for findLastChar
        String lastChar;
        List<String> punct = new ArrayList<>( Arrays.asList( ",",";","?",".","!" ) );

        //finds last char and returns it if it has a match
        if ( punct.contains( inWord.substring( inWord.length() - 1 ) ) ) {
            lastChar = inWord.substring( inWord.length() - 1 );
        } else {
            lastChar = "";
        }
        return lastChar;
    }
}
