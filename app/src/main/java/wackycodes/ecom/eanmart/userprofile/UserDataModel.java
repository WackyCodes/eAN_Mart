package wackycodes.ecom.eanmart.userprofile;

public class UserDataModel {

    private boolean isLoadData;

    // Main Variable
    private String userFullName;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userMobile;
    private String userProfilePhoto;

    // ID and Verification..
    private String userAuthID;
    private boolean isEmailVerify;
    private boolean isMobileVerify;

    // User City Name and AreaCode...
    private String userCityName;
    private String userCityCode;
    private String userAreaPinCode;

    // Address...
    private UserAddressDataModel userAddressDataModel;

    // Constructor....
    public UserDataModel() {
    }

    public UserDataModel(boolean isLoadData, String userFullName, String userFirstName, String userLastName, String userEmail, String userMobile, String userProfilePhoto, String userAuthID, boolean isEmailVerify, boolean isMobileVerify, String userCityName, String userCityCode, String userAreaPinCode, UserAddressDataModel userAddressDataModel) {
        this.isLoadData = isLoadData;
        this.userFullName = userFullName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.userProfilePhoto = userProfilePhoto;
        this.userAuthID = userAuthID;
        this.isEmailVerify = isEmailVerify;
        this.isMobileVerify = isMobileVerify;
        this.userCityName = userCityName;
        this.userCityCode = userCityCode;
        this.userAreaPinCode = userAreaPinCode;
        this.userAddressDataModel = userAddressDataModel;
    }

    public boolean isLoadData() {
        return isLoadData;
    }

    public void setLoadData(boolean loadData) {
        isLoadData = loadData;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserAuthID() {
        return userAuthID;
    }

    public void setUserAuthID(String userAuthID) {
        this.userAuthID = userAuthID;
    }

    public boolean isEmailVerify() {
        return isEmailVerify;
    }

    public void setEmailVerify(boolean emailVerify) {
        isEmailVerify = emailVerify;
    }

    public boolean isMobileVerify() {
        return isMobileVerify;
    }

    public void setMobileVerify(boolean mobileVerify) {
        isMobileVerify = mobileVerify;
    }

    public String getUserCityName() {
        return userCityName;
    }

    public void setUserCityName(String userCityName) {
        this.userCityName = userCityName;
    }

    public String getUserCityCode() {
        return userCityCode;
    }

    public void setUserCityCode(String userCityCode) {
        this.userCityCode = userCityCode;
    }

    public String getUserAreaPinCode() {
        return userAreaPinCode;
    }

    public void setUserAreaPinCode(String userAreaPinCode) {
        this.userAreaPinCode = userAreaPinCode;
    }

    public UserAddressDataModel getUserAddressDataModel() {
        return userAddressDataModel;
    }

    public void setUserAddressDataModel(UserAddressDataModel userAddressDataModel) {
        this.userAddressDataModel = userAddressDataModel;
    }
}
