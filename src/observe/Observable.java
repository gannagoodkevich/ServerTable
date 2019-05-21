package observe;

/**
 * Created by shund on 18.05.2017.
 */
public interface Observable {
    void addServerPanel(Observer observer);
    void removeServerPanel(Observer observer);
}
