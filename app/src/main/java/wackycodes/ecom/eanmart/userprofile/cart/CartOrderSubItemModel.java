package wackycodes.ecom.eanmart.userprofile.cart;

public class CartOrderSubItemModel {

    private int cartIndex;
    private int cartType;
    private String cartID;

    private String productShopID;
    private String productID;
    private String productName;
    private String productImage;
    private String productSellingPrice;
    private String productMrpPrice;
    private String productQty;

    public CartOrderSubItemModel(String productShopID, String productName, String productImage, String productSellingPrice, String productMrpPrice, String productQty) {
        this.productShopID = productShopID;
        this.productName = productName;
        this.productImage = productImage;
        this.productSellingPrice = productSellingPrice;
        this.productMrpPrice = productMrpPrice;
        this.productQty = productQty;
    }



    public CartOrderSubItemModel(int cartType, String cartID, String productShopID, String productID,
                                 String productName, String productImage, String productSellingPrice,
                                 String productMrpPrice, String productQty) {
        this.cartType = cartType;
        this.cartID = cartID;
        this.productShopID = productShopID;
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.productSellingPrice = productSellingPrice;
        this.productMrpPrice = productMrpPrice;
        this.productQty = productQty;
    }

    public int getCartType() {
        return cartType;
    }

    public void setCartType(int cartType) {
        this.cartType = cartType;
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

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
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

    ////---------------------------------------------------------------------------------------
    ////---------------------------------------------------------------------------------------





}
