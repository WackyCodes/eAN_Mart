package wackycodes.ecom.eanmart.category;

public class ShopItemModel {

    private String shopID;
    private String shopName;
    private String shopLogo;
    private String shopRating;
    private String shopCategory;
    private int shopVegType;


    public ShopItemModel(String shopID, String shopName, String shopLogo, String shopRating, String shopCategory, int shopVegType) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.shopLogo = shopLogo;
        this.shopRating = shopRating;
        this.shopCategory = shopCategory;
        this.shopVegType = shopVegType;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopRating() {
        return shopRating;
    }

    public void setShopRating(String shopRating) {
        this.shopRating = shopRating;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public int isShopVegType() {
        return shopVegType;
    }

    public void setShopVegType(int shopVegType) {
        this.shopVegType = shopVegType;
    }
}
