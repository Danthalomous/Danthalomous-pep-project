package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.ArrayList;

public class MessageService {
    MessageDAO messageDAO; // Creating an instance of the DAO

    /***
     * Default Constructor
     */
    public MessageService()
    {
        messageDAO = new MessageDAO(); // Initializing the DAO
    }

    /***
     * This intializes the DAO to a provided one for Mock data and testing
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO)
    {
        this.messageDAO = messageDAO; // Initializing the DAO to the provided arguement
    }

    /***
     * Creates a new message
     * @param message
     * @return null if invalid or unsuccessful insertion and returns the message object if inserted successfully
     */
    public Message CreateMessage(Message message)
    {
        // Checking to see if the message is valid
        if(message.getMessage_text() == "" || message.getMessage_text().length() > 255)
            return null;

        return messageDAO.CreateMessage(message);
    }

    /***
     * Retrieves all stored messages
     * @return a list of all the Message objects
     */
    public ArrayList<Message> GetAllMessages()
    {
        return messageDAO.GetAllMessages();
    }

    /***
     * Retrieves a message by its id
     * @param id
     * @return either null if not found or the Message object if found
     */
    public Message GetMessageByID(int id)
    {
        return messageDAO.GetMessageByID(id);
    }

    /***
     * Deletes a message by its id
     * @param id
     * @return true or false if the message was deleted or not
     */
    public boolean DeleteMessage(int id)
    {
        return messageDAO.DeleteMessage(id);
    }

    /***
     * Updates the message of an existing message; identified by its id
     * @param id
     * @param newMessage
     * @return null if invalid request or message was not found and the whole Message object gets returned if found
     */
    public Message UpdateMessage(int id, String newMessage)
    {
        // Checking to see if the message is valid
        if(newMessage == "" || newMessage.length() > 255)
            return null;

        // Making sure the ID already exists and there is an object
        if(messageDAO.GetMessageByID(id) == null)
            return null;

        return messageDAO.UpdateMessage(id, newMessage);
    }

    /***
     * Gets all stored messages under a specific account
     * @param id
     * @return an ArrayList of type Message for all messages under an account id
     */
    public ArrayList<Message> GetAllMessagesByAccountID(int id)
    {
        return messageDAO.GetAllMessagesByAccountID(id);
    }
}
