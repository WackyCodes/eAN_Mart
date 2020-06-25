package wackycodes.ecom.eanmart.userprofile.cart;

public class CartItemModel {

    private int cartIndex;


    private String productShopID;
    private String productID;
    private String productName;
    private String productImage;
    private String productSellingPrice;
    private String productMrpPrice;
    private String productQty;

    public CartItemModel( String productShopID, String productID, String productName, String productImage, String productSellingPrice, String productMrpPrice, String productQty) {
        this.productShopID = productShopID;
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.productSellingPrice = productSellingPrice;
        this.productMrpPrice = productMrpPrice;
        this.productQty = productQty;
    }

    public int getCartIndex() {
        return cartIndex;
    }

    public void setCartIndex(int cartIndex) {
        this.cartIndex = cartIndex;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductShopID() {
        return productShopID;
    }

    public void setProductShopID(String productShopID) {
        this.productShopID = productShopID;
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

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }
}
