package utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utils {
    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    //Fragments Tags
    public static final String Hotel_Fragment = "Hotel_Fragment";
    public static final String Package_Fragment = "Package_Fragment";
    public static final String Flight_Fragment = "Flight_Fragment";
    public static final String Car_Fragment = "Car_Fragment";
    public static final String Suv_Fragment = "Suv_Fragment";
    public static final String Van_Fragment = "Van_Fragment";

    /**
     * Check if textInputLayout contains editText view. If so, then set text value to the view.
     *
     * @param textInputLayout wrapper for the editText view where the text value should be set.
     * @param text            text value to display.
     */
//    public static void setTextToInputLayout(TextInputLayout textInputLayout, String text) {
//        if (textInputLayout != null && textInputLayout.getEditText() != null) {
//            textInputLayout.getEditText().setText(text);
//        } else {
//            //Timber.e("Setting text to null input wrapper, or without editText");
//        }
//    }

    /**
     * Check if textInputLayout contains editText view. If so, then return text value of the view.
     *
     * @param textInputLayout wrapper for the editText view.
     * @return text value of the editText view.
     */
//    public static String getTextFromInputLayout(TextInputLayout textInputLayout) {
//        if (textInputLayout != null && textInputLayout.getEditText() != null) {
//            return textInputLayout.getEditText().getText().toString();
//        } else {
//            return null;
//        }
//    }
    /**
     * Method checks if text input layout exist and contains some value.
     * If layout is empty, then show error value under the textInputLayout.
     *
     * @param
     * @param
     * @return true if everything ok.
     */
//    public static boolean checkTextInputLayoutValueRequirement(TextInputLayout textInputLayout, String errorValue) {
//        if (textInputLayout != null && textInputLayout.getEditText() != null) {
//            String text = Utils.getTextFromInputLayout(textInputLayout);
//            if (text == null || text.isEmpty()) {
//                textInputLayout.setErrorEnabled(true);
//                textInputLayout.setError(errorValue);
//                //Timber.d("Input field %s missing text.", textInputLayout.getHint());
//                return false;
//            } else {
//                textInputLayout.setErrorEnabled(false);
//                //Timber.d("Input field: %s OK.", textInputLayout.getHint());
//                return true;
//            }
//        } else {
//            //Timber.e(new RuntimeException(), "Checking null input field during order send.");
//            return false;
//        }
//    }

    public static String sanitizeText(String text){
        String str = text == null ? "" : text;
        return str;
    }

    public static String todayPlusDays(int days){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DAY_OF_MONTH, days); // subtracting months
        return sdf.format(c.getTime());
    }

    public static Calendar getCalendarTime(String time){
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = parseFormat.parse(time);
        } catch (ParseException e) {
            Log.d("ERROR LOG", "printStackTraceD");
        }
        c.setTime(date);
        return c;
    }



}
