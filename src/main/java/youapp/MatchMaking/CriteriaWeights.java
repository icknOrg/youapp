package youapp.MatchMaking;

import youapp.model.Person;


public class CriteriaWeights {
	
	
	//Weight for the quiz scores
	private double weightQuiz;
	
	//Weight of facebook info
	private double weightFB;
	private double weightFBPages;
	private double weightFBGroups;
	private double weightFBFriends;
	
	//Weight of distance info
	private double weightDistance;

	private double weightSymptoms;
	private double weightMedication;
	
	public double getWeightQuiz() {
		return weightQuiz;
	}
	
	public void setWeightQuiz(double weightQuiz) {
		this.weightQuiz = weightQuiz;
	}

	public double getWeightFB() {
		return weightFB;
	}

	public void setWeightFB(double weightFB) {
		this.weightFB = weightFB;
	}

	public double getWeightFBPages() {
		return weightFBPages;
	}

	public void setWeightFBPages(double weightFBPages) {
		this.weightFBPages = weightFBPages;
	}

	public double getWeightFBGroups() {
		return weightFBGroups;
	}

	public void setWeightFBGroups(double weightFBGroups) {
		this.weightFBGroups = weightFBGroups;
	}

	public double getWeightFBFriends() {
		return weightFBFriends;
	}

	public void setWeightFBFriends(double weightFBFriends) {
		this.weightFBFriends = weightFBFriends;
	}

	public double getWeightDistance() {
		return weightDistance;
	}

	public void setWeightDistance(double weightDistance) {
		this.weightDistance = weightDistance;
	}
	
	public double getWeightSymptoms() {
		return weightSymptoms;
	}

	public void setWeightSymptoms(double weightSymptoms) {
		this.weightSymptoms = weightSymptoms;
	}

	public double getWeightMedication() {
		return weightMedication;
	}

	public void setWeightMedication(double weightMedication) {
		this.weightMedication = weightMedication;
	}
	
	public CriteriaWeights reCalculateWeightsForPerson(Person person)
	{		
		CriteriaWeights personCriteriaWeigts = new CriteriaWeights();
		
		double totalWeight = 0.0;
		
		//calculate total weight
		if(person.getUseFBMatching())
		{
			totalWeight+=this.weightFB;			
		}
		if(person.getUseQuestionMatching())
		{
			totalWeight+=this.weightQuiz;
		}
		if(person.getUseDistanceMatching())
		{
			totalWeight+=this.weightDistance;
		}
		if(person.getUseSymptomsMatching())
		{
			totalWeight+=this.weightSymptoms;
		}
		if(person.getUseMedicationMatching())
		{
			totalWeight+=this.weightMedication;
		}
		
		//set weights 		
		if(person.getUseFBMatching())
		{
			personCriteriaWeigts.setWeightFB(this.weightFB/totalWeight);		
		}
		if(person.getUseQuestionMatching())
		{
			personCriteriaWeigts.setWeightQuiz(this.weightQuiz/totalWeight);
		}
		if(person.getUseDistanceMatching())
		{
			personCriteriaWeigts.setWeightDistance(this.weightDistance/totalWeight);
		}
		if(person.getUseSymptomsMatching())
		{
			personCriteriaWeigts.setWeightSymptoms(this.weightSymptoms/totalWeight);
		}
		if(person.getUseMedicationMatching())
		{
			personCriteriaWeigts.setWeightMedication(this.weightMedication/totalWeight);
		}	
		
		// set to default, not configurable at the moment
		personCriteriaWeigts.setWeightFBFriends(this.weightFBFriends);
		personCriteriaWeigts.setWeightFBGroups(this.weightFBGroups);
		personCriteriaWeigts.setWeightFBPages(this.weightFBPages);
		
		return personCriteriaWeigts;		
	}

}
