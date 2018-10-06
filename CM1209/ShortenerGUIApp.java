/*
 * Name: Jack Furby
 * Student number: 1619450
 */

import javax.swing.*;

//builder for windows
public class ShortenerGUIApp {
  public static void main( String[] args ) {
    JFrame frame = new ShortenerFrame();
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setVisible( true );
    frame.setSize( 1280,720 );
    frame.setLocationRelativeTo( null ); //set frame position on screen

    //calls method to load default abbreviation sheet
    ShortenerFrame ShortenerFrame = new ShortenerFrame();
    ShortenerFrame.loadAbbrev();
  }
}
