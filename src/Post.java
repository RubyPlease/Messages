import java.util.ArrayList;

/**
 * Created by RobertBarber on 4/18/16.
 */
public class Post {
    String name;
    String content;
    ArrayList<Message> messages = new ArrayList<>();

    public Post(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
