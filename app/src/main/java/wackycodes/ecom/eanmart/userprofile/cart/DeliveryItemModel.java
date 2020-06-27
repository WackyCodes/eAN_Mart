package wackycodes.ecom.eanmart.userprofile.cart;

public class DeliveryItemModel {

    //  : Delivery Details...

    private String deliveryBoyName;
    private String deliveryDateDay;
    private String deliveryTime;
    // rating...
    private String deliveryBoyRating;
    private String deliveryRatingText;


    public DeliveryItemModel(String deliveryBoyName, String deliveryDateDay, String deliveryTime) {
        this.deliveryBoyName = deliveryBoyName;
        this.deliveryDateDay = deliveryDateDay;
        this.deliveryTime = deliveryTime;
    }

    public DeliveryItemModel(String deliveryBoyName, String deliveryDateDay, String deliveryTime, String deliveryBoyRating, String deliveryRatingText) {
        this.deliveryBoyName = deliveryBoyName;
        this.deliveryDateDay = deliveryDateDay;
        this.deliveryTime = deliveryTime;
        this.deliveryBoyRating = deliveryBoyRating;
        this.deliveryRatingText = deliveryRatingText;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public void setDeliveryBoyName(String deliveryBoyName) {
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getDeliveryDateDay() {
        return deliveryDateDay;
    }

    public void setDeliveryDateDay(String deliveryDateDay) {
        this.deliveryDateDay = deliveryDateDay;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryBoyRating() {
        return deliveryBoyRating;
    }

    public void setDeliveryBoyRating(String deliveryBoyRating) {
        this.deliveryBoyRating = deliveryBoyRating;
    }

    public String getDeliveryRatingText() {
        return deliveryRatingText;
    }

    public void setDeliveryRatingText(String deliveryRatingText) {
        this.deliveryRatingText = deliveryRatingText;
    }
}
