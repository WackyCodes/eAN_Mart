package wackycodes.ecom.eanmart.apphome.shophome;

import java.util.List;

import wackycodes.ecom.eanmart.apphome.mainhome.CategoryTypeModel;
import wackycodes.ecom.eanmart.bannerslider.BannerSliderModel;
import wackycodes.ecom.eanmart.productdetails.ProductModel;

public class ShopHomeFragmentModel {

    private int layoutType;
    private List <BannerSliderModel> bannerSliderModelList;

    public int getLayoutType() {
        return layoutType;
    }
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    //--- Category Layout...
    private List<CategoryTypeModel> categoryTypeModelList;
    private boolean isVisible;

    public ShopHomeFragmentModel(int layoutType, List <CategoryTypeModel> categoryTypeModelList, boolean isVisible ) {
        this.layoutType = layoutType;
        this.categoryTypeModelList = categoryTypeModelList;
        this.isVisible = isVisible;
    }

    public List <CategoryTypeModel> getCategoryTypeModelList() {
        return categoryTypeModelList;
    }

    public void setCategoryTypeModelList(List <CategoryTypeModel> categoryTypeModelList) {
        this.categoryTypeModelList = categoryTypeModelList;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    //------ Layout for Banner Slider...
    public ShopHomeFragmentModel(int layoutType, List <BannerSliderModel> bannerSliderModelList) {
        this.layoutType = layoutType;
        this.bannerSliderModelList = bannerSliderModelList;
    }
    public List <BannerSliderModel> getBannerSliderModelList() {
        return bannerSliderModelList;
    }
    public void setBannerSliderModelList(List <BannerSliderModel> bannerSliderModelList) {
        this.bannerSliderModelList = bannerSliderModelList;
    }
    //------ Layout for Banner Slider...

    //------ Layout for Strip ad....
    private int stripClickType;
    private String stripClickID;
    private String stripAdImage;
    private String stripExtraText;


    public ShopHomeFragmentModel(int layoutType, int stripClickType, String stripClickID,  String stripAdImage, String stripExtraText) {
        this.layoutType = layoutType;
        this.stripClickType = stripClickType;
        this.stripClickID = stripClickID;
        this.stripAdImage = stripAdImage;
        this.stripExtraText = stripExtraText;
    }
    public String getStripAdImage() {
        return stripAdImage;
    }
    public void setStripAdImage(String stripAdImage) {
        this.stripAdImage = stripAdImage;
    }

    public int getStripClickType() {
        return stripClickType;
    }

    public void setStripClickType(int stripClickType) {
        this.stripClickType = stripClickType;
    }

    public String getStripClickID() {
        return stripClickID;
    }

    public void setStripClickID(String stripClickID) {
        this.stripClickID = stripClickID;
    }

    public String getStripExtraText() {
        return stripExtraText;
    }

    public void setStripExtraText(String stripExtraText) {
        this.stripExtraText = stripExtraText;
    }

    //------ Layout for Strip ad....

    // ------- Horizontal Item View ..----------------
    private List<ProductModel> productModelList;
    private List<String> HrAndGridProductIdList;
    private String horizontalLayoutTitle;

    public ShopHomeFragmentModel(int layoutType, List<String> HrAndGridProductIdList, List <ProductModel> productModelList, String horizontalLayoutTitle) {
        this.layoutType = layoutType;
        this.productModelList = productModelList;
        this.horizontalLayoutTitle = horizontalLayoutTitle;
        this.HrAndGridProductIdList = HrAndGridProductIdList;
    }

    public List <ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List <ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    public String getHorizontalLayoutTitle() {
        return horizontalLayoutTitle;
    }
    public void setHorizontalLayoutTitle(String horizontalLayoutTitle) {
        this.horizontalLayoutTitle = horizontalLayoutTitle;
    }

    public List <String> getHrAndGridProductIdList() {
        return HrAndGridProductIdList;
    }

    public void setHrAndGridProductIdList(List <String> hrAndGridProductIdList) {
        HrAndGridProductIdList = hrAndGridProductIdList;
    }

    // ------- Horizontal Item View ..----------------

}
