package wackycodes.ecom.eanmart.bannerslider;

public class BannerSliderModel {
    private int bannerClickType;
    private String bannerClickID;
    private String bannerImage;
    private String bannerOtherText;

    public BannerSliderModel(int bannerClickType, String bannerClickID, String bannerImage, String bannerOtherText) {
        this.bannerClickType = bannerClickType;
        this.bannerClickID = bannerClickID;
        this.bannerImage = bannerImage;
        this.bannerOtherText = bannerOtherText;
    }

    public int getBannerClickType() {
        return bannerClickType;
    }

    public void setBannerClickType(int bannerClickType) {
        this.bannerClickType = bannerClickType;
    }

    public String getBannerClickID() {
        return bannerClickID;
    }

    public void setBannerClickID(String bannerClickID) {
        this.bannerClickID = bannerClickID;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerOtherText() {
        return bannerOtherText;
    }

    public void setBannerOtherText(String bannerOtherText) {
        this.bannerOtherText = bannerOtherText;
    }
}
