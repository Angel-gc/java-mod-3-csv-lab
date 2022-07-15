import java.util.ArrayList;
import java.util.List;

class PeopleSingleton {
    private List<Person> people;
    private PeopleSingleton() {
        this.people = new ArrayList<>();
    }
    private static PeopleSingleton singleton;
    public static PeopleSingleton getInstance() {
        if (singleton == null) {
            singleton = new PeopleSingleton();
        }
        return singleton;
    }
    public void addPerson(Person p) {
        people.add(p);
    }
    public List<Person> getPeople() {
        return people;
    }
    @Override
    public String toString() {
        return people.toString();
    }
}