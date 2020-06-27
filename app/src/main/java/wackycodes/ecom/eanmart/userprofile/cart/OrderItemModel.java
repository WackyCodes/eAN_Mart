package wackycodes.ecom.eanmart.userprofile.cart;

import java.util.List;

public class OrderItemModel {
    // Cart Item...
    private String orderID;
    private String shopID;
    private String deliveryID;
    private String totalAmounts; // total product's amounts...
    private String deliveryCharge; // delivery amounts...
    private String billingAmounts; // billing amounts...
    private String savingAmounts;

    private String orderStatus;

    // Order List Sub Item...
    private List <CartOrderSubItemModel> cartOrderSubItemModelList;

    // Delivery Details...
    private List<DeliveryItemModel> deliveryItemModelList;

    public OrderItemModel(String orderID, String shopID, String deliveryID, String totalAmounts, String deliveryCharge, String billingAmounts, String savingAmounts, String orderStatus, List <CartOrderSubItemModel> cartOrderSubItemModelList) {
        this.orderID = orderID;
        this.shopID = shopID;
        this.deliveryID = deliveryID;
        this.totalAmounts = totalAmounts;
        this.deliveryCharge = deliveryCharge;
        this.billingAmounts = billingAmounts;
        this.savingAmounts = savingAmounts;
        this.orderStatus = orderStatus;
        this.cartOrderSubItemModelList = cartOrderSubItemModelList;
    }

    public OrderItemModel(String orderID, String shopID, String deliveryID, String totalAmounts, String deliveryCharge, String billingAmounts, String savingAmounts, String orderStatus, List <CartOrderSubItemModel> cartOrderSubItemModelList, List <DeliveryItemModel> deliveryItemModelList) {
        this.orderID = orderID;
        this.shopID = shopID;
        this.deliveryID = deliveryID;
        this.totalAmounts = totalAmounts;
        this.deliveryCharge = deliveryCharge;
        this.billingAmounts = billingAmounts;
        this.savingAmounts = savingAmounts;
        this.orderStatus = orderStatus;
        this.cartOrderSubItemModelList = cartOrderSubItemModelList;
        this.deliveryItemModelList = deliveryItemModelList;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getTotalAmounts() {
        return totalAmounts;
    }

    public void setTotalAmounts(String totalAmounts) {
        this.totalAmounts = totalAmounts;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getBillingAmounts() {
        return billingAmounts;
    }

    public void setBillingAmounts(String billingAmounts) {
        this.billingAmounts = billingAmounts;
    }

    public String getSavingAmounts() {
        return savingAmounts;
    }

    public void setSavingAmounts(String savingAmounts) {
        this.savingAmounts = savingAmounts;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List <CartOrderSubItemModel> getCartOrderSubItemModelList() {
        return cartOrderSubItemModelList;
    }

    public void setCartOrderSubItemModelList(List <CartOrderSubItemModel> cartOrderSubItemModelList) {
        this.cartOrderSubItemModelList = cartOrderSubItemModelList;
    }

    public List <DeliveryItemModel> getDeliveryItemModelList() {
        return deliveryItemModelList;
    }

    public void setDeliveryItemModelList(List <DeliveryItemModel> deliveryItemModelList) {
        this.deliveryItemModelList = deliveryItemModelList;
    }
}
