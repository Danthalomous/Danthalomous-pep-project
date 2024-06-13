package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    private Connection connection; // local connection variable to be used throughout the class

    /***
     * Default Constructor
     * Initializes the connection to the database
     */
    public AccountDAO()
    {
        connection = ConnectionUtil.getConnection(); // Instantiating the local connection to database via the Utility class
    }

    /***
     * Registers a new user within the database
     * @param newAccount
     * @return null if unsuccessful and returns the user if inserted successfully
     */
    public Account Register(Account newAccount)
    {
        try
        {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);"; // SQL for inseration

            // Creating the statement for SQL
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Setting the parameters of the query
            ps.setString(1, newAccount.getPassword());
            ps.setString(2, newAccount.getPassword());

            ps.executeUpdate(); // Inserting the record
            ResultSet pkResultSet = ps.getGeneratedKeys(); // Getting the generated keys
            
            if(pkResultSet.next())
            {
                int generated_account_id = (int) pkResultSet.getInt(1); // Getting the generated pk
                // Creating an object to return with the original values + the primary key
                
                Account returnAccount = new Account(
                    generated_account_id,
                    newAccount.getUsername(),
                    newAccount.getPassword()
                );

                return returnAccount; // Success!
            }

            return newAccount; // Returning the inserted user (w/o primary key)
        }
        catch (SQLException e)
        {
            // POTENTIAL TODO: Add Logging
            System.out.println(e.getStackTrace());
            return null; // Returning null if the attempt was unsuccessful
        }
    }

    /***
     * Account that authenticates that this user is able to login
     * @param account
     * @return null if invalid credentials and returns the full object if the credentials were valid
     */
    public Account Login(Account account)
    {
        try
        {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;"; // Prepared SQL

            // Creating a statment to submit to the database
            PreparedStatement ps = connection.prepareStatement(sql);

            // Setting the parameters of the statment
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery(); // Storing the results of the query in a ResultSet
            
            while(rs.next())
            {
                // Creating a new account object to return the results of the result set
                Account accessedAccount = new Account (
                  rs.getInt("account_id"),
                  rs.getString("username"),
                  rs.getString("password")  
                );
                
                return accessedAccount; // Returning the first result because there will only be one match if any
            }
        }
        catch(SQLException e)
        {
            // POTENTIAL TODO: Log this error
            System.out.println(e.getStackTrace());
        }

        return null; // No account was found
    }

    /***
     * Checks to see if there is an existing account with the given username
     * @param id
     * @return null if nothing was found and return the object of the record of the account if found
     */
    public Account GetAccountByUsername(String username)
    {
        try
        {
            String sql = "SELECT * FROM account WHERE username = ?;"; // SQL Statement

            // Creating a prepared statement for the database
            PreparedStatement ps = connection.prepareStatement(sql);

            // Setting the parameters of the query
            ps.setString(1, username);

            // Storing the results of the query in a ResultSet
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                // Creating a new object from the result set
                Account foundAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );

                return foundAccount; // Returning the first account found of the first iteration since there should only be one record
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getStackTrace());
        }

        return null; // No account with that username
    }

    /***
     * Checks to see if there is an existing account with the given username
     * @param id
     * @return null if nothing was found and return the object of the record of the account if found
     */
    public Account GetAccountByID(int id)
    {
        try
        {
            String sql = "SELECT * FROM account WHERE account_id = ?;"; // SQL Statement

            // Creating a prepared statement for the database
            PreparedStatement ps = connection.prepareStatement(sql);

            // Setting the parameters of the query
            ps.setInt(1, id);

            // Storing the results of the query in a ResultSet
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                // Creating a new object from the result set
                Account foundAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );

                return foundAccount; // Returning the first account found of the first iteration since there should only be one record
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getStackTrace());
        }

        return null; // No account with that username
    }
}
