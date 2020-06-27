package wackycodes.ecom.eanmart.userprofile.cart;

public class CartListSubItemModel {


    private String productShopID;
//    private String productID;
    private String productName;
    private String productImage;
    private String productSellingPrice;
    private String productMrpPrice;
    private String productWeight;
    private String productQty;

    public CartListSubItemModel() {
    }

    public CartListSubItemModel(String productShopID, String productName, String productImage, String productSellingPrice, String productMrpPrice, String productWeight, String productQty) {
        this.productShopID = productShopID;
        this.productName = productName;
        this.productImage = productImage;
        this.productSellingPrice = productSellingPrice;
        this.productMrpPrice = productMrpPrice;
        this.productWeight = productWeight;
        this.productQty = productQty;
    }

    public String getProductShopID() {
        return productShopID;
    }

    public void setProductShopID(String productShopID) {
        this.productShopID = productShopID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSellingPrice() {
        return productSellingPrice;
    }

    public void setProductSellingPrice(String productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }

    public String getProductMrpPrice() {
        return productMrpPrice;
    }

    public void setProductMrpPrice(String productMrpPrice) {
        this.productMrpPrice = productMrpPrice;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }
}
