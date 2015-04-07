import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that implements Gale Shapley algorithm to find matching pairs
 * between persons and pets
 * @author Anshu
 */
public class MatchingAlgo {

    private PersonPetData personPetData;
    
    /**
     * Constructor
     * @param personPetData
     */
    public MatchingAlgo(PersonPetData personPetData) {
        this.personPetData = personPetData;
    }
    
    /**
     * Compares preference for currentPerson w.r.t requestedPerson
     * and returns true if requestedPerson is more preferred 
     * than currentPerson
     * @param currentPerson
     * @param requestedPerson
     * @param petNumber
     * @return true if the requestedPerson is more preferred 
     * than currentPerson
     */
    public boolean checkPreference(int currentPerson, int requestedPerson, int petNumber) {
        boolean isPreferred = false;
        int[] tempPref = (int[])personPetData.getPets().get(petNumber).getPetPersonPreference();

        // Higher preference is indicated by a lower value for preference field
        if(tempPref[requestedPerson] < tempPref[currentPerson]) {
            isPreferred = true;
        }
        
        return isPreferred;
    }
    
    /**
     * Runs the Gale Shapley matching algorithm on the PersonPetData 
     * object contained in this instance
     * @return person to pet matching pairs
     */
    public int[] match() {
        int[] petMatch = new int[personPetData.getNumPairs()];
        // -1 value in the Array signify not matched yet
        Arrays.fill(petMatch, -1);
        int[] personMatch = new int[personPetData.getNumPairs()];
        Arrays.fill(personMatch, -1);
        
        ArrayList<Integer> freePersonIds = new ArrayList<Integer>();
        for (int i = 0; i < personPetData.getNumPairs(); i++) {
            freePersonIds.add(i);
        }
        ArrayList<Person> persons = personPetData.getPersons();
        
        while(freePersonIds.size() != 0) {            
            int personIndex = persons.get(freePersonIds.remove(0)).getId();
            int[] personPreferencePet = persons.get(personIndex).getPersonPreferencePet();
            
            for(int i = 0; i < personPreferencePet.length; i++) {
                // Make pet-person pair
                // Also make person-pet pair since that is the expected return
                if (petMatch[personPreferencePet[i]] == -1) {
                    petMatch[personPreferencePet[i]] = personIndex;
                    personMatch[personIndex] = personPreferencePet[i];
                    break;
                }
                else {
                    if(checkPreference(petMatch[personPreferencePet[i]], personIndex, personPreferencePet[i])) {
                        freePersonIds.add(petMatch[personPreferencePet[i]]);
                        petMatch[personPreferencePet[i]] = personIndex;
                        personMatch[personIndex] = personPreferencePet[i];
                        break;
                    }
                }
            }
        }
        
        return personMatch;
    }
}
