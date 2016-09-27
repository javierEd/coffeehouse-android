package ve.org.coffeehouse.napkin.data;

/**
 * Created by javier on 25/09/16.
 */

public class Note {
    private int id;
    private String content;
    private String created_at;

    public Note(){}

    public Note(int id, String content, String created_at){
        this.id = id;
        this.content = content;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
