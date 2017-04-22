package hello;

/**
 * Created by syq on 1/17/2017.
 */
public class Greeting {
    private final long id;
    private final String name;
    public Greeting(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
