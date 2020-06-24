package wackycodes.ecom.eanmart.apphome;

public class CategoryTypeModel {

    // Home main page : Category Data...
    private int type;
    private String catId;
    private String catName;
    private String catImage;

    public CategoryTypeModel(int type, String catId, String catName, String catImage) {
        this.type = type;
        this.catId = catId;
        this.catName = catName;
        this.catImage = catImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
