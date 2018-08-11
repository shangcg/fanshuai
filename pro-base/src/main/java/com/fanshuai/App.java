package com.fanshuai;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        StatusView statusView = new StatusView();
        statusView.setText("侬好");
        System.out.println(statusView.getText());

        statusView.setTruncated(true);
        System.out.println(statusView.getText());
        System.out.println(statusView.getRawText());
    }
}
