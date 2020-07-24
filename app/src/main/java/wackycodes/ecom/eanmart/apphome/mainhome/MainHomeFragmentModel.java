package wackycodes.ecom.eanmart.apphome.mainhome;

import java.util.List;

import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;

public class MainHomeFragmentModel {


    private int layoutType;

    public MainHomeFragmentModel(int layoutType) {
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
    private List<BannerSliderModel> bannerSliderModelList;

    public MainHomeFragmentModel(int layoutType, List <BannerSliderModel> bannerSliderModelList, int other) {
        this.layoutType = layoutType;
        this.bannerSliderModelList = bannerSliderModelList;
    }

    public List <BannerSliderModel> getBannerSliderModelList() {
        return bannerSliderModelList;
    }

    public void setBannerSliderModelList(List <BannerSliderModel> bannerSliderModelList) {
        this.bannerSliderModelList = bannerSliderModelList;
    }
    // Slider Layout....

    // CategoryView Layout in Grid...

    private List<CategoryTypeModel> categoryTypeModelList;

    public MainHomeFragmentModel(int layoutType, List <CategoryTypeModel> categoryTypeModelList) {
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

    private String bannerImage;
    private String bannerClickID;
    private String bannerExtraText;
    private int bannerClickType;

    public MainHomeFragmentModel(int layoutType, String bannerClickID, String bannerImage, String bannerExtraText, int bannerClickType) {
        this.layoutType = layoutType;
        this.bannerClickID = bannerClickID;
        this.bannerImage = bannerImage;
        this.bannerExtraText = bannerExtraText;
        this.bannerClickType = bannerClickType;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerClickID() {
        return bannerClickID;
    }

    public void setBannerClickID(String bannerClickID) {
        this.bannerClickID = bannerClickID;
    }

    public String getBannerExtraText() {
        return bannerExtraText;
    }

    public void setBannerExtraText(String bannerExtraText) {
        this.bannerExtraText = bannerExtraText;
    }

    public int getBannerClickType() {
        return bannerClickType;
    }

    public void setBannerClickType(int bannerClickType) {
        this.bannerClickType = bannerClickType;
    }


}
