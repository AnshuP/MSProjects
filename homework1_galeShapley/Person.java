/**
 * Class to represent person
 * @author Anshu
 */
public class Person {

    // Person id
    private int id;
    
    // Person name
    private String name;
    
    // personPetPreference[i] = x, where i is preference and x is pet
    private int[] personPreferencePet;
    
    public Person(int id, String name, int[] preferenceList) {
        this.id = id;
        this.name = name;
        this.personPreferencePet = preferenceList;
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
     * @return the personPreferencePet
     */
    public int[] getPersonPreferencePet() {
        return personPreferencePet;
    }

}
