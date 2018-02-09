package cmw.co.id.pmc.data;

/**
 * Created by CMW on 09/02/2018.
 */

public class ProjectSpinnerData {
    private String id, name;

    public ProjectSpinnerData() {
    }

    public ProjectSpinnerData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
