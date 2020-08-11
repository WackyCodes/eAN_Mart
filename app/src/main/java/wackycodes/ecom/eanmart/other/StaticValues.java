package wackycodes.ecom.eanmart.other;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.userprofile.UserDataModel;
import wackycodes.ecom.eanmart.userprofile.cart.CartOrderSubItemModel;

public class StaticValues {

    public static String APP_VERSION = "em-1-01"; // ev-1-01

    public static final String DEFAULT_CITY_NAME = "BHOPAL";
    public static String CURRENT_CITY_NAME ;
    public static String CURRENT_CITY_CODE ;

    // User Cart...
    public static final int CART_TYPE_ITEMS = 0;
    public static final int CART_TYPE_TOTAL_PRICE = 1;
    public static int TOTAL_BILL_AMOUNT;
    public static int DELIVERY_AMOUNT;

    // PayMode...
    public static int BUY_FROM_VALUE ;
    public static final int BUY_FROM_CART = 101;
    public static final int BUY_FROM_WISH_LIST = 102;
    public static final int BUY_FROM_HOME = 103;

    public static final int COD_MODE = 104;
    public static final int ONLINE_MODE = 105;

    // address..
    public static final int EDIT_ADDRESS_MODE = 2;
    public static final int MANAGE_ADDRESS = 1;
    public static final int SELECT_ADDRESS = 0;
    public static boolean isVerifiedMobile = false;

    public static final int QUERY_TO_ADD_ADDRESS = 20;
    public static final int QUERY_TO_REMOVE_ADDRESS = 21;
    public static final int QUERY_TO_UPDATE_ADDRESS = 22;

    public static final int NOTIFY_SIMPLE = 0;
    public static final int NOTIFY_ORDER_ACCEPTED = 1;
    public static final int NOTIFY_ORDER_ON_DELIVERY = 2;
    public static final int NOTIFY_ORDER_DELIVERED = 3;
    public static final int NOTIFY_ORDER_CANCEL = 4;
    public static final int NOTIFY_OFFER = 5;

    // User Data Model...
    public static final UserDataModel USER_DATA_MODEL = new UserDataModel();


    public static final int STORAGE_PERMISSION = 1;


    public static final int FRAGMENT_NULL = 0; // No Previous fragment
    public static final int FRAGMENT_MAIN_HOME = 1;
    public static final int FRAGMENT_MAIN_MY_ACCOUNT_ = 2;
    public static final int FRAGMENT_MAIN_MY_ORDER = 3;
    public static final int FRAGMENT_MAIN_MY_CART = 4;
    public static final int FRAGMENT_MAIN_SHOPS_VIEW = 5;

    public static final int FRAGMENT_SHOP_HOME = 6;
//    public static final int FRAGMENT_LOG_OUT = 7;
//    public static final int FRAGMENT_HELP = 8;
//    public static final int FRAGMENT_REPORT_PROBLEM = 9;
//    public static final int FRAGMENT_RATE_US = 10;

    public static final int FRAGMENT_SIGN_IN =14;
    public static final int FRAGMENT_SIGN_UP =15;

    // Activity...
    public static final int MAIN_ACTIVITY = 16;

    // Banner Click Type...
    public static final int BANNER_CLICK_TYPE_WEBSITE = 1;
    public static final int BANNER_CLICK_TYPE_SHOP = 2;
    public static final int BANNER_CLICK_TYPE_CATEGORY = 3;
    public static final int BANNER_CLICK_TYPE_NONE = 4;
    public static final int BANNER_CLICK_TYPE_PRODUCT = 5;

    // Home Category Layout... Type...
    public static final int TYPE_HOME_SHOP_BANNER_SLIDER = 2;
    public static final int TYPE_HOME_CATEGORY_LAYOUT = 5;
    public static final int TYPE_HOME_SHOP_STRIP_AD = 1; // or TYPE_HOME_SHOP_BANNER_AD
    public static final int TYPE_HOME_SHOP_ITEMS_CONTAINER = 6;



    // Type of List : Home Category List or Shop Name List...
    // Use For Local Only...
    public static final int TYPE_LIST_MAIN_HOME_CATEGORY = 25;
    public static final int TYPE_LIST_SHOPS_VIEW_NAME = 26;
    public static final int TYPE_BANNER_MAIN_HOME = 27;
    public static final int TYPE_BANNER_SHOPS_VIEW = 28;
    public static final int TYPE_BANNER_SHOP_HOME = 29;

    // Use For Local...
    public static final int LIST_MAIN_HOME_PAGE = 30;
    public static final int LIST_SHOP_VIEW_PAGE = 31;

    // Common Main Home Container... // Any One Shop Home Page...
    // Path = "SHOPS" > (SHOP_ID) > "HOME" > (order By.. )
    public static final int SHOP_HOME_STRIP_AD_CONTAINER = 1;
    public static final int SHOP_HOME_BANNER_SLIDER_CONTAINER = 2;
    public static final int SHOP_HOME_CAT_LIST_CONTAINER = 5;
    public static final int HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER = 3;
    public static final int GRID_PRODUCTS_LAYOUT_CONTAINER = 4;


    public static final int VIEW_HORIZONTAL_LAYOUT = 0;
    public static final int VIEW_RECTANGLE_LAYOUT = 1;
    public static final int VIEW_GRID_LAYOUT = 2;

    public static final int VIEW_ALL_ACTIVITY = 13;
    public static final int RECYCLER_PRODUCT_LAYOUT = 11;
    public static final int GRID_PRODUCT_LAYOUT = 12;

    public static final int CATEGORIES_ITEMS_VIEW_ACTIVITY = 16;

    public static final int PRODUCT_DETAILS_ACTIVITY = 10;
    public static final int BUY_NOW_ACTIVITY = 19;
    public static final int SHOP_HOME_ACTIVITY = 20;
    public static final int CONFORM_ORDER_ACTIVITY = 21;

    public static final int CONTINUE_SHOPPING_FRAGMENT = 22;
    public static final int SHOP_PRODUCT_CAT_ACTIVITY = 23;


    public static final int SHOP_TYPE_VEG = 1;
    public static final int SHOP_TYPE_NON_VEG = 2;
    public static final int SHOP_TYPE_VEG_NON = 3;
    public static final int SHOP_TYPE_NO_SHOW = 4;

    public static String SHOP_ID_CURRENT = "1";
    public static String SHOP_ID_PREVIOUS = "1";

    // Product Details... Temp List.
    public static List <String> productDetailTempList = new ArrayList <>();

    /*

    Index 0 - Product ID
    Index 1 - Product Image
    Index 2 - Product Name or Full Name
    Index 3 - Product Price
    Index 4 - Product Cut Price
    Index 5 - Product COD or NO_COD info
    index 6 - Product IN_STOCK or OUT_OF_STOCK info
    index 7 - product qty_Type ... product_qty_type

    // add Quantity
    index 7 - product Quantity

     */


    // Communicate Fragment Code..
    public static final int FRAGMENT_HELP = 20;
    public static final int FRAGMENT_ABOUT_US = 21;


}
