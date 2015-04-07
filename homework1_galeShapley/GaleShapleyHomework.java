import java.util.ArrayList;

/**
 * Main class
 * @author Anshu
 */
public class GaleShapleyHomework {

    /**
     * Accesses input file, gets it parsed into an input object, 
     * feeds the input object to the matching algorithm, 
     * and obtains the resulting pairs
     * @param args Empty
     */
    public static void main(String[] args) {
        PersonPetData personPetData = new PersonPetData("program1data.txt");
        MatchingAlgo matchingAlgo = new MatchingAlgo(personPetData);
        // pairs[i] = x, where i is person and x is pet
        int[] personPetPairs = matchingAlgo.match();
        ArrayList<Pet> pets = personPetData.getPets();        
        ArrayList<Person> persons = personPetData.getPersons();
        
        for (int i = 0; i < personPetPairs.length; i++) {
            System.out.println(persons.get(i).getName() + " / " + pets.get(personPetPairs[i]).getName());
        }
    }
}
