package hello;

/**
 * Created by Justyna on 08.03.2018.
 */
public class RevString {
    private long id;
    private String revContent;

    public RevString(long id, String revContent) {
        this.id = id;
        this.revContent = revContent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRevContent() {
        return revContent;
    }

    public void setRevContent(String revContent) {
        this.revContent = revContent;
    }
}
