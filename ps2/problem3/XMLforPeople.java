/*
 * XMLforPeople
 * 
 * A class for objects that convert data about people from 
 * the relational database used in PS 1 to XML.
 */

import java.sql.*;      // needed for the JDBC-related classes
import java.io.*;       // needed for file-related classes

public class XMLforPeople {
    private Connection db;   // a connection to the database
    
    /*
     * XMLforPeople constructor - takes the name of a SQLite file containing
     * a Person table like the one from PS 1, and creates an object that 
     * can be used to convert the data in that table to XML.
     * 
     * ** YOU SHOULD NOT CHANGE THIS METHOD **
     */
    public XMLforPeople(String dbFilename) throws SQLException {
        this.db = DriverManager.getConnection("jdbc:sqlite:" + dbFilename);
    }

    /*
     * simpleElem - a private helper method takes the name and value of 
     * a simple XML element and returns a string representation of that 
     * element
     * 
     * ** YOU SHOULD NOT CHANGE THIS METHOD **
     */
    private String simpleElem(String name, String value) {
        String elem = "<" + name + ">";
        elem += value;
        elem += "</" + name + ">";
        return elem;
    }
    
    /*
     * Takes a string representing a SQL query for the movie database
     * and returns a ResultSet object that represents the results
     * of the query (if any).
     * 
     * ** YOU SHOULD NOT CHANGE THIS METHOD **
     */
    public ResultSet resultsFor(String query) throws SQLException {
        Statement stmt = this.db.createStatement();
        ResultSet results = stmt.executeQuery(query);
        return results;
    }

    /*
     * idFor - takes the name of a person and returns the id number of 
     * that person in the database as a string. If the movie is not in the 
     * database, it returns an empty string.
     * 
     * ** YOU SHOULD NOT CHANGE THIS METHOD **
     */
    public String idFor(String name) throws SQLException {
        String query = "SELECT id FROM Person WHERE name = '" + name + "';";
        ResultSet results = resultsFor(query);
        
        if (results.next()) {    
            String id = results.getString(1);
            return id;
        } else {
            return "";
        }
    }

    /*
     * fieldsFor - takes a string representing the id number of a person
     * and returns a sequence of XML elements for the non-null field values
     * of that person in the database. If there is no person with the 
     * specified id number, the method returns an empty string.
     */
    public String fieldsFor(String personID) throws SQLException {
        
        // replace this return statement with your implementation
        String query = "SELECT * FROM Person WHERE id = '" + personID + "';";
        ResultSet results = resultsFor(query);

        String output = "";
        if (results.next()) {
            String name = results.getString(2);
            String dob = results.getString(3);
            String pob = results.getString(4);
            
            
            output += "    " + simpleElem("name", name) + "\n";

            if (dob != null){
                output += "    " + simpleElem("dob", dob) + "\n";
            }
            
            if (pob != null) {
                output += "    " + simpleElem("pob", pob) + "\n";
            } 
        }
        return output;
    }

    /*
     * movieElemsFrom - takes a ResultSet for a SQL query that produces
     * tuples of the form (movie year, movie name) and creates a sequence
     * of complex elements of type movie for them. If the result set is
     * empty, the method returns the empty string.
     */
    public String movieElemsFrom(ResultSet results) throws SQLException {
        
        // replace this return statement with your implementation
        String output = "";


        while (results.next()) {
            String movieYear = results.getString(1);
            String movieName = results.getString(2);
            
            output += "      " + "<movie>" + "\n";
            if (movieYear != null) {
                output += "        " + simpleElem("year", movieYear) + "\n";
            }
            if (movieName != null) {
                output += "        " + simpleElem("name", movieName) + "\n";
            }
            output += "      " + "</movie>" + "\n";
        }
        return output;
    }
    
    /*
     * actedIn - takes a string representing the id number of a person.
     * If the person has acted in one or more movies in the database,
     * the method constructs and returns a single complex XML element named 
     * "actedIn" that contains a nested child element named "movie" for 
     * each movie that person acted in. If the person has not acted
     * in any movies or if the person id is not in the database, the method 
     * returns an empty string.
     */
    public String actedIn(String personID) throws SQLException {
        
        // replace this return statement with your implementation
        
        String query = "SELECT M.year, M.name FROM Movie M, Actor A, Person P Where M.id = A.movie_id AND A.actor_id = P.id AND P.id = '" + personID + "' ORDER BY M.year, M.name;";
        ResultSet results = resultsFor(query);
        
        String movieElements = movieElemsFrom(results);

        String output = "";
    
        if (!movieElements.isEmpty()) {
            output += "    " + "<actedIn>" + "\n";
            output += movieElements;
            output += "    " + "</actedIn>" + "\n";
        }
    
        return output;
    }
    
    /*
     * directed - takes a string representing the id number of a person.
     * If the person has directed one or more movies in the database,
     * the method constructs and returns a single complex XML element named 
     * "directed" that contains a nested child element named "movie" for 
     * each movie directed by that person. If the person has not directed
     * any movies or if the person id is not in the database, the method 
     * returns an empty string.
     */
    public String directed(String personID) throws SQLException {
        
        // replace this return statement with your implementation
        String query = "SELECT M.year, M.name FROM Movie M, Director D, Person P Where M.id = D.movie_id AND D.director_id = P.id AND P.id = '" + personID + "' ORDER BY M.year, M.name;";
        ResultSet results = resultsFor(query);
        
        String movieElements = movieElemsFrom(results);

        String output = "";
    
        if (!movieElements.equals("")) {
            output += "    " + "<directed>" + "\n";
            output += movieElements;
            output += "    " + "</directed>" + "\n";
        }
    
        return output;
    }    
    
    /*
     * elementFor - takes a string representing the id number of a person
     * and returns a single complex XML element named "person" that contains
     * nested child elements for all of the fields and movies associated
     * with that person in the database. If there is no person with 
     * the specified id number, the method returns an empty string.
     */
    public String elementFor(String personID) throws SQLException {
        
        // replace this return statement with your implementation
        String output = ""; 

        String personFields = fieldsFor(personID);

        if (!personFields.equals("")) {
            output += "  " + "<person id=\"" + personID + "\">\n";
            output += personFields;
            output += actedIn(personID);
            output += directed(personID);
            output += "  " + "</person>" + "\n";
        }
        
        return output;
    }

    /*
     * createFile - creates a text file with the specified filename 
     * containing an XML representation of the entire Person table.
     * 
     * ** YOU SHOULD NOT CHANGE THIS METHOD **
     */
    public void createFile(String filename) 
      throws FileNotFoundException, SQLException 
    {
        PrintStream outfile = new PrintStream(filename);    
        outfile.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
        outfile.println("<people>");
        
        // Use a query to get all of the ids from the Movie Table.
        ResultSet results = resultsFor("SELECT id FROM Person;");
        
        // Process one movie id at a time, creating its 
        // XML element and writing it to the output file.
        while (results.next()) {
            String personID = results.getString(1);
            outfile.println(elementFor(personID));
        }
        
        outfile.println("</people>");
        
        // Close the connection to the output file.
        outfile.close();
        System.out.println(filename + " has been written.");
    }
    
    /*
     * closeDB - closes the connection to the database that was opened
     * when the XMLforPeople object was constructed
     * 
     * ** YOU SHOULD NOT CHANGE THIS METHOD **
     */
    public void closeDB() throws SQLException {
        this.db.close();
    }
    
    public static void main(String[] args) 
        throws ClassNotFoundException, SQLException, FileNotFoundException
    {
        XMLforPeople xml = new XMLforPeople("movie.sqlite");
        xml.createFile("people.xml");
        xml.closeDB();
        
    }
}
