package com.ik.githubbrowser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by ismailkhan on 23/11/17.
 */

public class AppUtils {

    /**
     * Converts the timestamp format returned by github api to a more user friendly format
     * @param timestamp returned by the api
     * @return String a more readable format
     */
    public static String dateFormat (String timestamp) {
        SimpleDateFormat githubDateFormat = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'");
        SimpleDateFormat viewFriendlyDateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss aaa");
        String viewFriendlyDate = "";
        try {
            Date date = githubDateFormat.parse(timestamp);
            viewFriendlyDate = viewFriendlyDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return viewFriendlyDate;

    }
}
