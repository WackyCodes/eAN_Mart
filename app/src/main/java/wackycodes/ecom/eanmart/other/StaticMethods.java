package wackycodes.ecom.eanmart.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static wackycodes.ecom.eanmart.databasequery.DBQuery.firebaseFirestore;

public class StaticMethods {



    //Database References...
    public static DocumentReference getFirebaseDocumentRef(@NonNull String cityName){
        return firebaseFirestore.collection( "HOME" ).document( cityName.toUpperCase() );
    }
    //Database References...
    public static DocumentReference getFirebaseDocumentRef(@NonNull String cityName, @NonNull String areaCode){
        DocumentReference documentReference = firebaseFirestore.collection( "HOME" )
                .document( cityName.toUpperCase() ).collection( "SUB_LOCATION" ).document( areaCode );
        return documentReference;
    }


    // Static Toast...
    public static void showToast(Context context, String message){
        Toast.makeText( context, message, Toast.LENGTH_SHORT ).show();
    }


    // create a random order ID of 10 Digits...
    public static String getRandomOrderID(){

        Random random = new Random();
        // Generate random integers in range 0 to 9999
        int rand_int1 = 0;
        String randNum = "";
        do {
            rand_int1 = random.nextInt(10000);
        }while ( rand_int1 < 0 );

        if (rand_int1 < 9999){
            rand_int1 = rand_int1*100;
            randNum = String.valueOf( rand_int1 ) + String.valueOf( rand_int1 );
            randNum = randNum.substring( 0, 6 );
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHH", Locale.getDefault());
        //You can change "yyyyMMdd_HHmmss as per your requirement
        String random12 = simpleDateFormat.format(new Date()) + randNum;

        // Get current mm, dd, hh and add Get Random 6 digit ..
        return random12;

    }

    public static String getCurrentDateDay(){
    //        Calendar calendar = Calendar.getInstance();
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        //You can change "yyyyMMdd_HHmmss as per your requirement
    //        String crrDate = simpleDateFormat.format(new Date()) ;

        String crrDateDay =
                simpleDateFormat.format(new Date())+ " " + new SimpleDateFormat( "EEEE", Locale.ENGLISH).format( date.getTime() );

        return crrDateDay;
    }

    public static String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String crrTime = simpleDateFormat.format(new Date()) ;
        return crrTime;
    }


    public static void writeFileInLocal(Context context, String fileName, String textMsg){
        try {
//            FileOutputStream fileOS = openFileOutput( fileName, MODE_PRIVATE );
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOS );
//            outputStreamWriter.write( textMsg );
//            outputStreamWriter.close();
            File folder1 = new File(context.getExternalFilesDir( Environment.getExternalStorageDirectory().getAbsolutePath() ), fileName);
            folder1.mkdirs();
            File pdfFile = new File(folder1, fileName + ".txt");
//            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream( pdfFile );
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOutputStream );
            outputStreamWriter.write( textMsg );
            outputStreamWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // yyyy/mm/dd hh:mm:ss

    public static String getScheduleTimeForOTP(){
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss", Locale.getDefault());

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = simpleDateFormat.format(new Date()) ;

//        String otpTime = currentTime.substring( 17 )

        return currentTime;

    }

    public static int getRandomOTP(){
        Random random = new Random();
        int OTP_Number = random.nextInt(999999 - 111111) + 111111;
        return OTP_Number;
    }

    public static String getCorrectMobile(String mobile){
        if (mobile.length() == 10){
            return mobile;
        }else if (mobile.length()==11) {
            mobile = mobile.substring( 1 );
            return mobile;
        }else if (mobile.length()==12) {
            mobile = mobile.substring( 2 );
            return mobile;
        }else if (mobile.length()==13) {
            mobile = mobile.substring( 3 );
            return mobile;
        }else{
            return null;
        }
    }



}
