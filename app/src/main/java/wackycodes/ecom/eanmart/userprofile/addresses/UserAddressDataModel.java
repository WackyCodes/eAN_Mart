package wackycodes.ecom.eanmart.userprofile.addresses;

public class UserAddressDataModel {

    private String addressID;

    // new Address...
    private String addUserName;
    private String addUserMobile;

    private String addHouseNoWard;
    private String addColonyVillage;
    private String addCityName;
    private String addState;
    private String addAreaPinCode;
    private String addLandmark;

    private Boolean isSelectedAddress = false;

    public Boolean getSelectedAddress() {
        return isSelectedAddress;
    }

    public void setSelectedAddress(Boolean selectedAddress) {
        isSelectedAddress = selectedAddress;
    }


    public UserAddressDataModel() {
    }

    public UserAddressDataModel(String addHouseNoWard, String addColonyVillage, String addCityName, String addState, String addAreaPinCode, String addLandmark) {
        this.addHouseNoWard = addHouseNoWard;
        this.addColonyVillage = addColonyVillage;
        this.addCityName = addCityName;
        this.addState = addState;
        this.addAreaPinCode = addAreaPinCode;
        this.addLandmark = addLandmark;
    }

    public UserAddressDataModel(String addressID, String addUserName, String addUserMobile, String addHouseNoWard, String addColonyVillage, String addCityName, String addState, String addAreaPinCode, String addLandmark,Boolean isSelectedAddress) {
        this.addressID = addressID;
        this.addUserName = addUserName;
        this.addUserMobile = addUserMobile;
        this.addHouseNoWard = addHouseNoWard;
        this.addColonyVillage = addColonyVillage;
        this.addCityName = addCityName;
        this.addState = addState;
        this.addAreaPinCode = addAreaPinCode;
        this.addLandmark = addLandmark;
        this.isSelectedAddress = isSelectedAddress;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }

    public String getAddUserMobile() {
        return addUserMobile;
    }

    public void setAddUserMobile(String addUserMobile) {
        this.addUserMobile = addUserMobile;
    }

    public String getAddHouseNoWard() {
        return addHouseNoWard;
    }

    public void setAddHouseNoWard(String addHouseNoWard) {
        this.addHouseNoWard = addHouseNoWard;
    }

    public String getAddColonyVillage() {
        return addColonyVillage;
    }

    public void setAddColonyVillage(String addColonyVillage) {
        this.addColonyVillage = addColonyVillage;
    }

    public String getAddCityName() {
        return addCityName;
    }

    public void setAddCityName(String addCityName) {
        this.addCityName = addCityName;
    }

    public String getAddState() {
        return addState;
    }

    public void setAddState(String addState) {
        this.addState = addState;
    }

    public String getAddAreaPinCode() {
        return addAreaPinCode;
    }

    public void setAddAreaPinCode(String addAreaPinCode) {
        this.addAreaPinCode = addAreaPinCode;
    }

    public String getAddLandmark() {
        return addLandmark;
    }

    public void setAddLandmark(String addLandmark) {
        this.addLandmark = addLandmark;
    }
}
