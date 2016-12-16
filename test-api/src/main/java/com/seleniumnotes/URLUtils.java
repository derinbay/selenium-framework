package com.seleniumnotes;

/**
 * Created by Taylan on 28.05.2016.
 */
public class URLUtils {

    public static boolean compareAsEquals(String url1, String url2) {
        String url1Cropped = url1.replace("https", "").replace("http", "");
        String url2Cropped = url2.replace("https", "").replace("http", "");

        return url1Cropped.equalsIgnoreCase(url2Cropped);
    }

    public static boolean compareAsContains(String url1, String url2) {
        String url1Cropped = url1.replace("https", "").replace("http", "").toLowerCase();
        String url2Cropped = url2.replace("https", "").replace("http", "").toLowerCase();

        return url1Cropped.contains(url2Cropped);
    }
}
