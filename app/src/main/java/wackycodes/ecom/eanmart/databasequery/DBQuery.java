package wackycodes.ecom.eanmart.databasequery;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmart.apphome.HomeFragmentModel;

public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    // Home : Main Recycler view List...
    public static List <HomeFragmentModel> homePageCategoryList = new ArrayList <>();
    // Shops View List : For Same category Multiple shops...
    public static List <HomeFragmentModel> shopsViewFragmentList = new ArrayList <>();





}



