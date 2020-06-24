package wackycodes.ecom.eanmart.apphome;

import java.util.List;

public class HomeFragmentModel {


    private int layoutType;

    public HomeFragmentModel(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    // Slider Layout....
    //---...
    // Slider Layout....

    // CategoryView Layout in Grid...

    private List<CategoryTypeModel> categoryTypeModelList;

    public HomeFragmentModel(int layoutType, List <CategoryTypeModel> categoryTypeModelList) {
        this.layoutType = layoutType;
        this.categoryTypeModelList = categoryTypeModelList;
    }

    public List <CategoryTypeModel> getCategoryTypeModelList() {
        return categoryTypeModelList;
    }

    public void setCategoryTypeModelList(List <CategoryTypeModel> categoryTypeModelList) {
        this.categoryTypeModelList = categoryTypeModelList;
    }

    // Shop Ad : Banner & Strip Banner...

    private String shopImage;
    private String shopID;
    private String shopNameOrColor;
    private int bannerViewFragmentType;

    public HomeFragmentModel(int layoutType, String shopID, String shopImage, String shopNameOrColor, int bannerViewFragmentType) {
        this.layoutType = layoutType;
        this.shopID = shopID;
        this.shopImage = shopImage;
        this.shopNameOrColor = shopNameOrColor;
        this.bannerViewFragmentType = bannerViewFragmentType;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopNameOrColor() {
        return shopNameOrColor;
    }

    public void setShopNameOrColor(String shopNameOrColor) {
        this.shopNameOrColor = shopNameOrColor;
    }

    public int getBannerViewFragmentType() {
        return bannerViewFragmentType;
    }

    public void setBannerViewFragmentType(int bannerViewFragmentType) {
        this.bannerViewFragmentType = bannerViewFragmentType;
    }


}
