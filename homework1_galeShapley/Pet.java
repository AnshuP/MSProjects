/**
 * Class to represent pet
 * @author Anshu
 */
public class Pet {

    // Pet id
    private int id;
    
    // Pet name
    private String name;
    
    // petPersonPreference[i] = x, where i is person and x is preference
    private int[] petPersonPreference;
    
    public Pet(int id, String name, int[] preferenceList) {
        this.id = id;
        this.name = name;
        // Construct a inverse preference list such that
        // the indices are persons and values are preferences
        this.petPersonPreference = new int[preferenceList.length];
        
        for (int i=0; i < preferenceList.length; i++) {
            this.petPersonPreference[preferenceList[i]] = i;
        }
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the petPersonPreference
     */
    public int[] getPetPersonPreference() {
        return petPersonPreference;
    }        
}
