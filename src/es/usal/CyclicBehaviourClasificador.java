package es.usal;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicBehaviourClasificador extends CyclicBehaviour{	
	private static final long serialVersionUID = 1L;
	ArrayList<String> main_agent_topics;
	ArrayList<String> tweeter_user_topics;
    ArrayList<String> common_topics;
    float common_topics_number; 
    float main_agent_topics_number;
    String user_name;
    float percentage_of_compatibility;
	
	//Constructor
	public CyclicBehaviourClasificador(Agent agent) {
		super(agent);
	}
	
	@Override
	public void action(){
		common_topics_number = 0;
		main_agent_topics = new ArrayList<String>();
		tweeter_user_topics = new ArrayList<String>();
		common_topics =  new ArrayList<String>(); 
		
		ACLMessage msgFromMain = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology("ontologia")));
		
		ACLMessage msgFromTweeter = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology("ontologia")));
		
		ACLMessage msgFromParser = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchOntology("ontologia")));
		
		try{
			main_agent_topics = (ArrayList<String>) msgFromMain.getContentObject();
			user_name = (String) msgFromTweeter.getContentObject();
			tweeter_user_topics = (ArrayList<String>) msgFromParser.getContentObject();
		}
		
		catch (UnreadableException e){
			// TODO Auto-generated catch blocke.printStackTrace()
			e.printStackTrace();
		}
		
		
		main_agent_topics_number = (float)main_agent_topics.size();
        for(int i=0; i<main_agent_topics.size(); i++){
            for(int j=0; j<tweeter_user_topics.size(); j++){
                if(main_agent_topics.get(i).contains(tweeter_user_topics.get(j))){
                    common_topics.add(main_agent_topics.get(i));
                    common_topics_number += 1;
                }
            }
        }
        
        percentage_of_compatibility = (common_topics_number/main_agent_topics_number)*100;
        
        System.out.println("Main Agent Topics: "+ main_agent_topics);
        System.out.println("Tweeter User Topics Extracted: "+ tweeter_user_topics);
        System.out.println("Common Topics: "+ common_topics);
        System.out.println("Common Topics Counter: "+ common_topics_number + " of " + main_agent_topics_number);
        System.out.println("Percentage Of Compatibilty: "+ percentage_of_compatibility+"%\n\n");
        
        
        String result;
        if(percentage_of_compatibility == 0) result = "Hi user. @"+user_name+" is interested in nothing similar to you, so why don't you try to find another user to establish contact with?";
        else{
            result = "Hi, user. @"+user_name+" is interested in [";
            for(int i=0; i<common_topics.size(); i++){
                result = result.concat(" "+common_topics.get(i));
            }
            result = result.concat("] like you.\nPercentage of compatibility: "+percentage_of_compatibility+"%.");
            if(percentage_of_compatibility > 0 && percentage_of_compatibility <= 25) result = result.concat(" You don't have that much in common so I would recommend you to look for another user. Good luck.");
            else if(percentage_of_compatibility > 25 && percentage_of_compatibility <= 75) result = result.concat(" You have some things in common so I would recommend both of you to follow each other but without establishing contact.");
            else result = result.concat(" Congratulations. You are made for each other, you should be friends.");
        }
		

        //Notifying the main agent
		Utils.enviarMensaje(this.myAgent, "IniciarMAS", result);  
	}	
}
