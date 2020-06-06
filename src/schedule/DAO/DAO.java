package schedule.DAO;

import java.util.Optional;
import javafx.collections.ObservableList;
import schedule.User;

public interface DAO<T> {
    
    T get(int Id);
    ObservableList<T> getAll();
    void add(T t, User u);
    void update(T t);
    void delete(T t);
}
