package es.usal;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicBehaviourPrincipal extends CyclicBehaviour{	
	private static final long serialVersionUID = 1L;
	private Scanner scanner;
	final String[] posible_topics = {"Animals","Art","Cinema","Economy","Education",
			"Food","Jobs","Literature","Medicine","Music","Politics","Religion","Sport",
			"Technology","Videogames","Weather"};
    
	private ArrayList<String> my_topics;
    private String new_topic;
    private int my_topics_size;
    private Random random_topic;
    private Checker my_checker;
	
	//Constructor
	public CyclicBehaviourPrincipal(Agent agent) {
		super(agent);
	}
	
	@Override
	public void action(){
		Random rn = new Random();
		my_topics_size = rn.nextInt(5)+1;
		
		scanner = new Scanner(System.in);
		String username;
		username = scanner.nextLine();
		
		
		/*MAIN AGENT FUNCTION*/
		/*****************************************************************************************************/
		/*****************************************************************************************************/
		/*****************************************************************************************************/
        my_topics = new ArrayList<String>();
        random_topic = new Random();
        my_checker = new Checker();
	
        for(int i=0; i<my_topics_size; i++){
        	do{
        		new_topic = posible_topics[random_topic.nextInt(16)];
        	}while(my_checker.isRepeated(new_topic, my_topics,my_topics.size()));
        	
        	my_topics.add(new_topic);
        }
	
        System.out.println("My Main Topics are...");
        System.out.println("---------------");
        for(int i=0; i<my_topics.size(); i++) System.out.println(my_topics.get(i));
        System.out.print("\n");
		/******************************************************************************************************/
        /*****************************************************************************************************/
        /*****************************************************************************************************/
		
		
		//Notifying the 'tweeter' agent and the classifier
		Utils.enviarMensaje(this.myAgent, "BuscarUsuario", username);
		Utils.enviarMensaje(this.myAgent, "ClasificarRelacion", my_topics);
		
		
		//Waiting for the classifier...
		ACLMessage msgFromClassifier = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology("ontologia")));
		
		try{
			System.out.println("Message from Classifier: "+msgFromClassifier.getContentObject() + "\n\n");
		}
		
		catch (UnreadableException e){
			// TODO Auto-generated catch blocke.printStackTrace()
			e.printStackTrace();
		}
	}	
}