package es.usal;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgenteAnalizador extends Agent{
	private static final long serialVersionUID = 1L;
	private CyclicBehaviourAnalizador cba = new CyclicBehaviourAnalizador(this);

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("Analizador");
		sd.setType("AnalizarTweets");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}
		catch(FIPAException e){
			System.err.println("Agente" + getLocalName() + ": " + e.getMessage());
		}
		
		cba = new CyclicBehaviourAnalizador(this);
		this.addBehaviour(cba);
	}
}
