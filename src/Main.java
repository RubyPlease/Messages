import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    static HashMap<String, Post> posts = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();

        Spark.post(
                // after the enter their loginName
                // store their login state so that we know who they are next time
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String content = new String();
                    Post post = posts.get(name);
                    if (post == null){
                        post = new Post(name, content);
                        posts.put(name, post);
                    }

                    // save loginName
                    Session session = request.session();
                    session.attribute("postName", name);

                    // send the user to the page
                    response.redirect("/");
                    return "";
                })
        );
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("postName");
                    Post post = posts.get(name);

                    HashMap m = new HashMap();
                    if (post == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else{
                        return new ModelAndView(post, "home.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/createMessage",
                ((request, response) -> {

                    Session session = request.session();
                    String name = session.attribute("postName");
                    Post post = posts.get(name);
                    if (post == null){
                        throw new Exception("Post requires name");
                    }

                    String messageName = request.queryParams("messageName");
                    String messageContent = request.queryParams("messageContent");
                    Message message = new Message(messageName, messageContent);

                    post.messages.add(message);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

    }
}
