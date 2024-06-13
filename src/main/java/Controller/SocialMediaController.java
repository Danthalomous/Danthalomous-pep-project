package Controller;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    // Declaring the services to be used throughout this class
    AccountService accountService;
    MessageService messageService;

    // Declaring an object mapper so that it can be used throughout the class
    ObjectMapper om;

    /***
     * Default Constructor
     */
    public SocialMediaController()
    {
        accountService = new AccountService();
        messageService = new MessageService();

        om = new ObjectMapper();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // All Endpoints for the application
        app.post("/register", this::ProcessRegisterAccount);
        app.post("/login", this::ProcessLoginAccount);
        
        app.post("/messages", this::ProcessCreateMessage);
        app.get("/messages", this::ProcessGetAllMessages);
        app.get("/messages/{message_id}", this::ProcessGetMessageByID);
        app.delete("/messages/{message_id}", this::ProcessDeleteMessage);
        app.patch("/messages/{message_id}", this::ProcessUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::ProcessGetMessagesByAccountID);

        return app;
    }

    /**
     * Utilizes the service to process a register request
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void ProcessRegisterAccount(Context ctx) throws JsonProcessingException 
    {
        Account account = om.readValue(ctx.body(), Account.class); // mapping the body to an object

        // Attempting to register an account
        Account attemptedAccount = accountService.Register(account);
        if(attemptedAccount != null)
        {
            ctx.json(om.writeValueAsString(attemptedAccount)); // returning the object as a JSON
            ctx.status(200); // Success!
        }
        else
            ctx.status(400); // Failure!
    }

    /***
     * Utilizes the service to process a login request
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessLoginAccount(Context ctx) throws JsonProcessingException
    {
        Account account = om.readValue(ctx.body(), Account.class); // mapping body to object

        // Attempting to log in with the provided account
        Account attemptedLogin = accountService.Login(account);

        if(attemptedLogin != null)
        {
            ctx.json(om.writeValueAsString(attemptedLogin)); // returning the object as a JSON object
            ctx.status(200); // Success!
        }
        else
            ctx.status(401); // Failure!
    }

    /***
     * Utilizes the service to process a create message request
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessCreateMessage(Context ctx) throws JsonProcessingException
    {
        Message message = om.readValue(ctx.body(), Message.class);

        // Check to see the account exists
        if(!accountService.GetAccountByID(message.getMessage_id()))
            ctx.status(400);

        // Attempt to create a new message
        Message attemptedMessage = messageService.CreateMessage(message);

        if(attemptedMessage != null)
        {
            ctx.json(om.writeValueAsString(attemptedMessage)); // writing the object to a JSON object
            ctx.status(200); // Success!
        }
        else
            ctx.status(400); // Failure!
    }

    /***
     * Gets all stored messages upon request
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessGetAllMessages(Context ctx) throws JsonProcessingException
    {
        ArrayList<Message> messages = new ArrayList<>();

        messages = messageService.GetAllMessages(); // Gets all messages from the database

        ctx.json(om.writeValueAsString(messages)); // Return the list as a JSON object (even if it is empty)
        ctx.status(200); // Success!
    }

    /***
     * Gets a message by its id upon request
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessGetMessageByID(Context ctx) throws JsonProcessingException
    {
        int message_id = Integer.parseInt(ctx.pathParam("message_id")); // Getting the parameter with the id

        Message message = messageService.GetMessageByID(message_id); // Getting the message with the id
        
        // Only attaching a body if the message object is not null
        if(message != null)
            ctx.json(om.writeValueAsString(message)); // Storing the object as a JSON object (even if it is empty)
        
        ctx.status(200); // Success!
    }

    /***
     * Deletes a message by its id upon request
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessDeleteMessage(Context ctx) throws JsonProcessingException
    {
        // Getting the id from the context
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        // Storing the message before it gets deleted in order to return it
        Message messageBeforeDeleted = messageService.GetMessageByID(id);

        // Checking to see if the deletion was a success
        if(messageService.DeleteMessage(id))
            ctx.json(om.writeValueAsString(messageBeforeDeleted)); // Attaching the object as a JSON object

        ctx.status(200); // Success!
    }

    /***
     * Updates a message upon request utilizing its id
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessUpdateMessage(Context ctx) throws JsonProcessingException
    {
        int id = Integer.parseInt(ctx.pathParam("message_id")); // storing the id in a local variable
        Message newMessage = om.readValue(ctx.body(), Message.class); // Retrieving the message from the body
        
        System.out.println("Created Variables: " + newMessage.toString());

        // Storing the updated object in a local object and calling the update method
        Message message = messageService.UpdateMessage(id, newMessage.getMessage_text());

        // Checking to see if the update was successful
        if(message != null)
        {
            ctx.json(om.writeValueAsString(message)); // Writing the updated object as a JSON object
            ctx.status(200); // Success!
        }
        else
            ctx.status(400); // Failure!   
    }

    /***
     * Gets all messages that belong to an account upon request
     * @param ctx
     * @throws JsonProcessingException
     */
    private void ProcessGetMessagesByAccountID(Context ctx) throws JsonProcessingException
    {
        ArrayList<Message> messages = new ArrayList<>(); // creating an array to store the Messages in

        int accountID = Integer.parseInt(ctx.pathParam("account_id")); // storing the integer from the path parameter

        messages = messageService.GetAllMessagesByAccountID(accountID); // Getting all the messages

        ctx.json(om.writeValueAsString(messages)); // writing the array of messages as a JSON object
        ctx.status(200); // Sucess!
    }
}