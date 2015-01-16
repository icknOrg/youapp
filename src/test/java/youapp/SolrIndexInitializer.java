package youapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

public class SolrIndexInitializer {

	/**
	 * Provides access to the Solr search index.
	 */
	private CommonsHttpSolrServer solrServer;
	
	@Autowired
	public void setSolrServer(CommonsHttpSolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	public void resetIndex() throws Exception {
		// Delete everything in the Solr index.
		solrServer.deleteByQuery("*:*");
		solrServer.commit();
		
		// Import initial data.
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		Calendar cal = Calendar.getInstance();
	
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField("id", "question1");
		doc1.addField("id_Question", "1");
		doc1.addField("authorId", "1");
		doc1.addField("authorName", "Calmy-Rey Micheline");
		doc1.addField("authorFirstName", "Micheline");
		doc1.addField("authorLastName", "Calmy-Rey");
		doc1.addField("authorNickName", "Micheline");
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 3);
		cal.set(Calendar.DATE, 15);
		doc1.addField("createdOn", cal.getTime());
		doc1.addField("question", "Cats or dogs - which do you like more?");
		doc1.addField("answerA", "Cats");
		doc1.addField("answerB", "Dogs");
		doc1.addField("deleted", false);
		docs.add(doc1);
		
		SolrInputDocument doc2 = new SolrInputDocument();
		doc2.addField("id", "question2");
		doc2.addField("id_Question", "2");
		doc2.addField("authorId", "1");
		doc2.addField("authorName", "Calmy-Rey Micheline");
		doc2.addField("authorFirstName", "Micheline");
		doc2.addField("authorLastName", "Calmy-Rey");
		doc2.addField("authorNickName", "Micheline");
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 3);
		cal.set(Calendar.DATE, 28);
		doc2.addField("createdOn", cal.getTime());
		doc2.addField("question", "Would you consider a long-distance relationship?");
		doc2.addField("answerA", "Yes, why not?");
		doc2.addField("answerB", "Depends, if I would be totally in love maybe.");
		doc2.addField("answerC", "No, I want to see my partner every day.");
		doc2.addField("deleted", false);
		docs.add(doc2);
		
		SolrInputDocument doc3 = new SolrInputDocument();
		doc3.addField("id", "question3");
		doc3.addField("id_Question", "3");
		doc3.addField("authorId", "5");
		doc3.addField("authorName", "Burkhalter Didier");
		doc3.addField("authorFirstName", "Didier");
		doc3.addField("authorLastName", "Burkhalter");
		doc3.addField("authorNickName", "B.Diddy");
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.DATE, 9);
		doc3.addField("createdOn", cal.getTime());
		doc3.addField("question", "Do you have a smartphone?");
		doc3.addField("answerA", "Yes, I'm addicted.");
		doc3.addField("answerB", "Yes, I have to.");
		doc3.addField("answerC", "No, but I'll buy one soon");
		doc3.addField("answerD", "No, I hate them");
		doc3.addField("deleted", false);
		docs.add(doc3);
		
		SolrInputDocument doc4 = new SolrInputDocument();
		doc4.addField("id", "question4");
		doc4.addField("id_Question", "4");
		doc4.addField("authorId", "5");
		doc4.addField("authorName", "Burkhalter Didier");
		doc4.addField("authorFirstName", "Didier");
		doc4.addField("authorLastName", "Burkhalter");
		doc4.addField("authorNickName", "B.Diddy");
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.DATE, 25);
		doc4.addField("createdOn", cal.getTime());
		doc4.addField("question", "Are you an animal person?");
		doc4.addField("answerA", "Yes, I love animals and I have some domestic animals myself.");
		doc4.addField("answerB", "Yes, I like them but I don't have domestic animals myself.");
		doc4.addField("answerC", "No, but I wouldn't mind having domestic animals.");
		doc4.addField("answerD", "No, I hate them.");
		doc4.addField("deleted", false);
		docs.add(doc4);
		
		SolrInputDocument doc5 = new SolrInputDocument();
		doc5.addField("id", "question5");
		doc5.addField("id_Question", "5");
		doc5.addField("authorId", "6");
		doc5.addField("authorName", "Sommaruga Simonetta");
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 4);
		cal.set(Calendar.DATE, 25);
		doc5.addField("createdOn", cal.getTime());
		doc5.addField("authorFirstName", "Simonetta");
		doc5.addField("authorLastName", "Sommaruga");
		doc5.addField("authorNickName", "SimSom");
		doc5.addField("question", "You find a 100 dollar bill on the floor. What do you do with it?");
		doc5.addField("answerA", "I take it to the police.");
		doc5.addField("answerB", "I take it for myself.");
		doc5.addField("answerC", "I spend it to some charity organization.");
		doc5.addField("answerD", "I leave it where it is.");
		doc5.addField("answerE", "I burn it.");
		doc5.addField("deleted", false);
		docs.add(doc5);
		
		SolrInputDocument doc6 = new SolrInputDocument();
		doc6.addField("id", "question6");
		doc6.addField("id_Question", "6");
		doc6.addField("authorId", "1");
		doc6.addField("authorName", "Calmy-Rey Micheline");
		doc6.addField("authorFirstName", "Micheline");
		doc6.addField("authorLastName", "Calmy-Rey");
		doc6.addField("authorNickName", "Micheline");
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 9);
		cal.set(Calendar.DATE, 10);
		doc6.addField("createdOn", cal.getTime());
		doc6.addField("question", "Cats or dogs - which do you prefer?");
		doc6.addField("answerA", "Cats");
		doc6.addField("answerB", "Dogs");
		doc6.addField("answerD", "I like both!");
		doc6.addField("answerE", "I hate them...");
		doc6.addField("deleted", false);
		docs.add(doc6);
		
		solrServer.add(docs);
		solrServer.commit();
	}
}
