import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class parses the input file containing the persons and pets preference data
 * and converts it to PersonPetData object that will be used by MatchingAlgo
 * @author Anshu
 */
public class PersonPetData {
    
    private BufferedReader br;
    
    // The number of persons (or pets) in this file
    // It is given that the number of persons will be the
    // same as number of pets. So if there are 5 persons
    // and 5 pets, we would have numPairs as 5
    private int numPairs;
    
    // Helper data structures
    private ArrayList<String> personNameList = new ArrayList<String>();
    private ArrayList<String> petNameList = new ArrayList<String>();
    
    // Lists of Person and Pet objects which will be used
    // by the matching algorithm
    // All data in the input file is eventually
    // used to construct these two lists
    private ArrayList<Person> persons = new ArrayList<Person>();
    private ArrayList<Pet> pets = new ArrayList<Pet>();
    
    /**
     * Constructor
     * @param filename Name of the file assumed to be in the 
     *     default execution directory
     */
    public PersonPetData(String filename) {
        String currentLine;
        ArrayList<String> fileData = new ArrayList<String>();
        
        try {
            br = new BufferedReader(new FileReader(filename));
            
            while ((currentLine = br.readLine()) != null) {
                fileData.add(currentLine);
            }
        
        } catch (IOException e) {
            e.printStackTrace();
            
        }
        
        numPairs = Integer.parseInt(fileData.get(0).toString());
        
        for(int i = 1; i <= numPairs; i++) {            
            personNameList.add(fileData.get(i).toString());
        }
        
        int startPos = numPairs + 1;
        int endPos = startPos - 1 + numPairs;
        int counter = 0;
        
        for(int i = startPos; i <= endPos; i++) {
            String[] tempPref = fileData.get(i).toString().trim().split("\\s+");
            int[] preferenceList = new int[tempPref.length];
            
            for(int j = 0; j < tempPref.length; j++) {    
                preferenceList[j] = Integer.parseInt(tempPref[j]) - 1; // starting index at 0
            }
            
            persons.add(new Person(counter, personNameList.get(counter), preferenceList));
            counter++;
        }
        
        startPos = endPos + 1;
        endPos = startPos - 1 + numPairs;
        
        for(int i = startPos; i <= endPos; i++) {
            petNameList.add(fileData.get(i).toString());
        }
        
        startPos = endPos + 1;
        endPos = startPos - 1 + numPairs;
        counter = 0;
        
        for(int i = startPos; i <= endPos; i++) {
            String[] tempPref = fileData.get(i).toString().trim().split("\\s+");
            int[] preferenceList = new int[tempPref.length];
            
            for(int j = 0; j < tempPref.length; j++) {    
                preferenceList[j] = Integer.parseInt(tempPref[j]) - 1; // starting index at 0
            }
            pets.add(new Pet(counter, petNameList.get(counter), preferenceList));
            counter++;            
        }        
    }
    
    /**
     * @return the numPairs
     */
    public int getNumPairs() {
        return numPairs;
    }

    /**
     * @return the persons
     */
    public ArrayList<Person> getPersons() {
        return persons;
    }

    /**
     * @return the pets
     */
    public ArrayList<Pet> getPets() {
        return pets;
    }
}
