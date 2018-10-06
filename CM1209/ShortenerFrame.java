/*
 * Name: Jack Furby
 * Student number: 1619450
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import java.util.*;
 import java.awt.datatransfer.*;

public class ShortenerFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L; //sets a serialVersionUID (removes warning)

    // Instance variables -- GUI components
    private JPanel editViewPanel, mainPanel, btnPanel;
    private JLabel instructionLabel;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton btnSubmit, btnPaste, btnCopy;
    private JScrollPane inputScroll, outputScroll;

    //creates shotener object for application
    private static Shortener shortener;

    //global variables for errors
    public static String fileUploadError = "";

    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Constructor
    public ShortenerFrame() {
        super();

        // Set up the frame
        setTitle( "Text Shortener" );


        //
        // Set up the editViewPanel and layout manager
        editViewPanel = new JPanel();
        GridLayout editViewgrid = new GridLayout( 0, 2 );
        editViewPanel.setLayout( editViewgrid );

        //set up components
        inputArea = new JTextArea();
        outputArea = new JTextArea();
        inputScroll = new JScrollPane( inputArea );
        outputScroll = new JScrollPane( outputArea );

        //set properties for components
        inputArea.setBackground( Color.black );
        inputArea.setForeground( Color.white );
        inputArea.setCaretColor( Color.white );
        inputArea.setMargin( new Insets( 10,10,10,10 ) );
        inputArea.setWrapStyleWord( true );
        inputArea.setLineWrap( true );
        outputArea.setEditable( false );
        outputArea.setMargin( new Insets( 10,10,10,10 ) );
        outputArea.setWrapStyleWord( true );
        outputArea.setLineWrap( true );
        inputScroll.setBorder( BorderFactory.createEmptyBorder() );
        outputScroll.setBorder( BorderFactory.createEmptyBorder() );

        //add components to editViewPanel
        editViewPanel.add( inputScroll );
        editViewPanel.add( outputScroll );


        //
        //add elements to btnPanel and setting properties
        btnPanel = new JPanel();
        btnPanel.setLayout( new GridBagLayout() );
        GridBagConstraints gbcBtnLayout = new GridBagConstraints();
        gbcBtnLayout.fill = GridBagConstraints.HORIZONTAL;

        //set up components
        instructionLabel = new JLabel( "Enter text in the field to the left below and click " , JLabel.CENTER );
        btnSubmit = new JButton( "Shorten" );
        btnCopy = new JButton( "Copy" );
        btnPaste = new JButton( "Paste" );

        //set location of components
        gbcBtnLayout.gridx = 1;
        gbcBtnLayout.gridy = 0;
        btnPanel.add( instructionLabel, gbcBtnLayout );
        gbcBtnLayout.gridx = 2;
        gbcBtnLayout.gridy = 0;
        btnPanel.add( btnSubmit, gbcBtnLayout );
        gbcBtnLayout.gridx = 0;
        gbcBtnLayout.gridy = 1;
        btnPanel.add( btnPaste, gbcBtnLayout );
        gbcBtnLayout.gridx = 3;
        gbcBtnLayout.gridy = 1;
        btnPanel.add( btnCopy, gbcBtnLayout );


        //
        // Set up the mainPanel and layout manager
        mainPanel = new JPanel();
        BorderLayout mainLayout = new BorderLayout();
        mainPanel.setLayout( mainLayout );

        //add components to panel
        mainPanel.add( editViewPanel,BorderLayout.CENTER );
        mainPanel.add( btnPanel ,BorderLayout.NORTH );


        //
        //add eventlisteners to items in window
        btnSubmit.addActionListener( this );
        btnCopy.addActionListener( this );
        btnPaste.addActionListener( this );

        //
        //add and pack mainPanel
        add( mainPanel );
        pack();
    }

    //
    //events for the window
    public void actionPerformed( ActionEvent e ) {

        JPanel warningPanel = new JPanel();

        //gets text in inputArea and outputs abbreviated text
        if( e.getSource() == btnSubmit ) {
            outputArea.setText( shortener.shortenMessage( inputArea.getText().trim() ) );
            outputArea.setCaretPosition( 0 );
        }
        //copy text in outputArea
        if( e.getSource() == btnCopy ) {
            StringSelection copyText = new StringSelection( outputArea.getText() ); //setstext in outputArea to a Transferable object
            clipboard.setContents( copyText, null ); //sets text to clipboard, text is a Transferable object, null is the owner
            //https://docs.oracle.com/javase/7/docs/api/java/awt/datatransfer/Clipboard.html
        }
        //paste text in inputArea to text in clipboard
        if( e.getSource() == btnPaste ) {
            Transferable pasteObj = clipboard.getContents( this ); //gets item in clipboard
            try {
                inputArea.insert( ( String ) pasteObj.getTransferData( DataFlavor.stringFlavor ), inputArea.getCaretPosition() ); //if pasteObj can be converted to text it is added to inputArea
            } catch ( Exception expt ){
                JOptionPane.showMessageDialog( warningPanel,expt,"Paste error",JOptionPane.ERROR_MESSAGE ); // else error is displayed
            }
        }
    }

    //
    //Loads in abbreviation sheet
    public void loadAbbrev() {

        //panel to display warnings
        JPanel warningPanel = new JPanel();

        shortener = new Shortener();

        if( fileUploadError != "" ) {
            JOptionPane.showMessageDialog(warningPanel,fileUploadError,"Upload file error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
