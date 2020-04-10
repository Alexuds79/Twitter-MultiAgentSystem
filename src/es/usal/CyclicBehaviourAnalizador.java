package es.usal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicBehaviourAnalizador extends CyclicBehaviour{	
	private static final long serialVersionUID = 1L;
	ArrayList<String> tweets_from_tweeter;
    ArrayList<String> tweeter_topics;
    ArrayList<String> all_words;
    File my_file;
    String current_topic;


	//Constructor
	public CyclicBehaviourAnalizador(Agent agent) {
		super(agent);
	}
	
	
	@Override
	public void action(){
		current_topic = "";
		tweeter_topics = new ArrayList<String>();
		
		ACLMessage msgFromTweeter = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology("ontologia")));
		
		try{
			tweets_from_tweeter = (ArrayList<String>)msgFromTweeter.getContentObject();
		}
		
		catch (UnreadableException e){
			// TODO Auto-generated catch blocke.printStackTrace()
			e.printStackTrace();
		}

		
		for(int i=0; i<tweets_from_tweeter.size(); i++){
            String[] palabras_tweet = tweets_from_tweeter.get(i).split("\\W+");
            for(int j=0; j<palabras_tweet.length; j++){
                for(int k=0; k<16; k++){
                    if(k==0) my_file = new File("Topics\\Animal.txt");
                    else if(k==1) my_file = new File("Topics\\Art.txt");
                    else if(k==2) my_file = new File("Topics\\Cinema.txt");
                    else if(k==3) my_file = new File("Topics\\Economy.txt");
                    else if(k==4) my_file= new File("Topics\\Education.txt");
                    else if(k==5) my_file= new File("Topics\\Food.txt");
                    else if(k==6) my_file= new File("Topics\\Jobs.txt");
                    else if(k==7) my_file= new File("Topics\\Literature.txt");
                    else if(k==8) my_file= new File("Topics\\Medicine.txt");
                    else if(k==9) my_file= new File("Topics\\Music.txt");
                    else if(k==10) my_file= new File("Topics\\Politics.txt");
                    else if(k==11) my_file= new File("Topics\\Religion.txt");
                    else if(k==12) my_file= new File("Topics\\Sport.txt");
                    else if(k==13) my_file= new File("Topics\\Technology.txt");
                    else if(k==14) my_file= new File("Topics\\Videogames.txt");
                    else my_file= new File("Topics\\Weather.txt");
                    
                    try{
                        all_words = (ArrayList<String>) Files.readAllLines(my_file.toPath());
                    }
                    catch(IOException e){
                        System.err.println("ERROR WHILE READING...");
                        return;
                    }
                    for(String current_word : all_words){
                        if(current_word.equals(all_words.get(0))) current_topic = current_word;
                        else if(palabras_tweet[j].contains(current_word)){
                            tweeter_topics.add(current_topic);
                            break;
                        }
                    }
                }
            }
       }
        
		//Delete duplicates
	    HashSet<String> hs = new HashSet<>();
	    hs.addAll(tweeter_topics);
	    tweeter_topics.clear();
	    tweeter_topics.addAll(hs);
	    
	    System.out.println("\nTopics extracted from the user tweets:\n-----------------------------------------\n");
        for(int i=0; i<tweeter_topics.size(); i++) System.out.println(tweeter_topics.get(i));
		
		
		//Notifying the classifier...
		Utils.enviarMensaje(this.myAgent, "ClasificarRelacion", tweeter_topics);
	}	
}