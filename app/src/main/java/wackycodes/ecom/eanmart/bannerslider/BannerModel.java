package wackycodes.ecom.eanmart.bannerslider;

public class BannerModel {

    private int bannerClickType;
    private String bannerClickID;
    private String bannerImageLink;
    private String bannerExtraText;

    public BannerModel(int bannerClickType, String bannerClickID, String bannerImageLink, String bannerExtraText) {
        this.bannerClickType = bannerClickType;
        this.bannerClickID = bannerClickID;
        this.bannerImageLink = bannerImageLink;
        this.bannerExtraText = bannerExtraText;
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

    public String getBannerImageLink() {
        return bannerImageLink;
    }

    public void setBannerImageLink(String bannerImageLink) {
        this.bannerImageLink = bannerImageLink;
    }

    public String getBannerExtraText() {
        return bannerExtraText;
    }

    public void setBannerExtraText(String bannerExtraText) {
        this.bannerExtraText = bannerExtraText;
    }
}
