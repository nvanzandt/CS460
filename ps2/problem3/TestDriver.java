/*
 * TestDriver
 * 
 * A program that can be used for testing your code for Problem 3.
 */

import java.sql.*;      // needed for the JDBC-related classes 
import java.io.*;       // needed for file-related classes

public class TestDriver {
    public static void main(String[] args) 
        throws ClassNotFoundException, SQLException, FileNotFoundException
    {
        // Add your test code below.
        XMLforPeople xml = new XMLforPeople("movie.sqlite");
        System.out.println(xml.elementFor(xml.idFor("Elisabeth Moss")));
        System.out.println(xml.elementFor("1234567"));
        System.out.println(xml.elementFor(xml.idFor("Denzel Washington")));
        
    }
}