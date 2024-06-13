package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO; // Private instance of the DAO to be used throughout the class

    /***
     * Default Constructor
     */
    public AccountService()
    {
        accountDAO = new AccountDAO(); // Intializing the DAO
    }

    /***
     * This is for when an accountDAO is provided for Mock data
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    /***
     * Calls the DAO to register a new user
     * @param account
     * @return null if the user was invalid or the insertion failed and the object of the user if successful
     */
    public Account Register(Account account)
    {
        // Making sure the user provided valid account info
        if(account.getUsername() == "" || account.getPassword().length() < 4)
            return null;

        // Making sure that their username is unique
        if(accountDAO.GetAccountByUsername(account.getUsername()) != null)
            return null;

        return accountDAO.Register(account); // Returning the result of the registration
    }

    /***
     * Attempts to log the user in and returns the results
     * @param account
     * @return null if login was unsuccessful and the object of the account if succuessful
     */
    public Account Login(Account account)
    {
        return accountDAO.Login(account);
    }

    /***
     * Checks to see if there is an account with that existing ID
     * @param id
     * @return
     */
    public boolean GetAccountByID(int id)
    {
        // Checking to see if the account exists
        if(accountDAO.GetAccountByID(id) != null)
            return true;
        
        return false;
    }
}
