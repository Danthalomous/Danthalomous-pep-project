package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class MessageDAO {
    private Connection connection; // Local instance of the connection to the database for this entire class

    /***
     * Default Constructor
     */
    public MessageDAO()
    {
        connection = ConnectionUtil.getConnection(); // Initializing the connection variable with the util class
    }

    /***
     * Creates a message and inserts it into the database
     * @param message
     * @return null if unsuccessful insertion and return the provided message if successful insertion occurred
     */
    public Message CreateMessage(Message message)
    {
        try
        {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);"; // SQL Statment

            // Prepared statement for the sql query to be executed through on the database
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Setting the parameters of the prepared statment
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate(); // Executing the query on the database

            ResultSet pkeyResultSet = ps.getGeneratedKeys();

            if(pkeyResultSet.next())
            {
                int generated_message_id = (int) pkeyResultSet.getInt(1);
                Message returnMessage = new Message(
                generated_message_id,
                message.getPosted_by(),
                message.getMessage_text(),
                message.getTime_posted_epoch()
            );

            return returnMessage;
            }
        }
        catch(SQLException e)
        {
            // POTENTIAL TODO: Log the stack trace
            System.out.println(e.toString());
        }

        return null; // Unsuccessful creation of message
    }

    /***
     * Gets all messages stored in the database and returns them as a list
     * @return
     */
    public ArrayList<Message> GetAllMessages()
    {
        ArrayList<Message> messages = new ArrayList<>(); // List to hold all of the messages stored in the database

        try
        {
            String sql = "SELECT * FROM message;"; // SQL Statement

            PreparedStatement ps = connection.prepareStatement(sql); // Creating a statement to execute on the database

            ResultSet rs = ps.executeQuery(); // Executing query and storing the results

            // Iterating through the results and adding objects to the list of type Message
            while(rs.next())
            {
                // Adding a new object to the array list
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        }
        catch (SQLException e)
        {
            // POTENTIAL TODO: Log this
            System.out.println(e.getStackTrace());
        }

        return messages; // Returns the list of all messages (will be empty if nothing was found)
    }

    /***
     * Gets a message by its id
     * @return
     * @param id of the message to retrieve
     */
    public Message GetMessageByID(int id)
    {
        try
        {
            String sql = "SELECT * FROM message WHERE message_id = ?;"; // SQL Statement

            PreparedStatement ps = connection.prepareStatement(sql); // Creating a statement to execute on the database

            // Setting the parameters of the query
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery(); // Executing query and storing the results

            while(rs.next())
            {
                // Creating a message object from the result set to return
                Message message = new Message(
                    id,
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                return message; // Returning the first message found (it should be the only one with that id)
            }
        }
        catch (SQLException e)
        {
            // POTENTIAL TODO: Log this
            System.out.println(e.getStackTrace());
        }

        return null; // Returns null if nothing was found
    }

    /***
     * Deletes a message by its id
     * @return
     * @param id of the message to delete
     */
    public boolean DeleteMessage(int id)
    {
        try
        {
            String sql = "DELETE FROM message WHERE message_id = ?;"; // SQL Statement

            PreparedStatement ps = connection.prepareStatement(sql); // Creating a statement to execute on the database

            // Setting the parameters of the query
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate(); // Executing the query

            if(rowsAffected > 0)
                return true; // True if the value was deleted
            else
                return false; // False if there was nothing to delete
        }
        catch (SQLException e)
        {
            // POTENTIAL TODO: Log this
            System.out.println(e.getStackTrace());
        }

        return false;
    }

    /***
     * Updates a message of a given id to a provided new message
     * @param id
     * @param newMessage
     * @return
     */
    public Message UpdateMessage(int id, String newMessage)
    {
        try
        {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;"; // SQL Statment

            PreparedStatement ps = connection.prepareStatement(sql); // Creating a statment to execute on the database

            // Setting the parameters of the query
            ps.setString(1, newMessage);
            ps.setInt(2, id);

            ps.executeUpdate(); // Executing the query

            return GetMessageByID(id); // Returning the updated object by getting it by ID
        }
        catch(SQLException e)
        {
            // POTENTIAL TODO: Log this
            System.out.println(e.getStackTrace());
        }
        
        return null; // No message to update
    }

    /***
     * Gets all messages stored in the database and returns them as a list
     * @return
     */
    public ArrayList<Message> GetAllMessagesByAccountID(int id)
    {
        ArrayList<Message> messages = new ArrayList<>(); // List to hold all of the messages stored in the database

        try
        {
            String sql = "SELECT * FROM message WHERE message_id = ?;"; // SQL Statement

            PreparedStatement ps = connection.prepareStatement(sql); // Creating a statement to execute on the database

            // Setting the parameters of the query
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery(); // Executing query and storing the results

            // Iterating through the results and adding objects to the list of type Message
            while(rs.next())
            {
                // Adding a new object to the array list
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        }
        catch (SQLException e)
        {
            // POTENTIAL TODO: Log this
            System.out.println(e.toString());
        }

        return messages; // Returns the list of all messages (will be empty if nothing was found)
    }
}
