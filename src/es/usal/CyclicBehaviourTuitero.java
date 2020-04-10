package es.usal;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicBehaviourTuitero extends CyclicBehaviour{	
	private static final long serialVersionUID = 1L;
	String my_file_string = "Tweets.txt";
    File my_file;
    ArrayList<String> all_tweets;
    ArrayList<String> my_tweets;
    String new_tweet;
    final int ELEMENTS = 5;
    Random random_tweet;
    Checker my_checker;
    String username;
	
	//Constructor
	public CyclicBehaviourTuitero(Agent agent) {
		super(agent);
	}
	
	@Override
	public void action(){
		
		//Waiting for the main agent...
		ACLMessage msgFromMain = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology("ontologia")));
		
		try{
			username = (String)msgFromMain.getContentObject();
			System.out.println("\n\nMessage from Main: The username is @"+ username);
		}
		
		catch (UnreadableException e){
			// TODO Auto-generated catch blocke.printStackTrace()
			e.printStackTrace();
		}
		
		
		
		/*TWEETER AGENT FUNCTION*/
		/*****************************************************************************************************/
		/*****************************************************************************************************/
		/*****************************************************************************************************/
         my_file = new File(my_file_string);
         my_tweets = new ArrayList<String>();
         random_tweet = new Random();
         my_checker = new Checker();
		
         try{
             all_tweets = (ArrayList<String>) Files.readAllLines(my_file.toPath());
         }
         catch(IOException e){
             System.err.println("ERROR WHILE READING...");
             return;
         }
		
         for(int i=0; i<ELEMENTS; i++){
             do{
                 new_tweet = all_tweets.get(random_tweet.nextInt(all_tweets.size()));
             }while(my_checker.isRepeated(new_tweet, my_tweets, my_tweets.size()));
             my_tweets.add(new_tweet);
         }
		
         System.out.println("Tweets extracted from the user @"+username+":\n\n");
         for(int i=0; i<my_tweets.size(); i++) System.out.println(my_tweets.get(i));
 		/*****************************************************************************************************/
 		/*****************************************************************************************************/
 		/*****************************************************************************************************/
		
		
         
		//Notifying the analyzer...
		Utils.enviarMensaje(this.myAgent, "AnalizarTweets", my_tweets); 
		
		//Giving the classificator the username
		Utils.enviarMensaje(this.myAgent, "ClasificarRelacion", username);
	}	
}
