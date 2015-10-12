package com.smalt.smaltcheck.Utils;

/**
 * Created by yassinebouderbala on 10/10/2015.
 */
public class Message {
    public static String title(String argUrl){
        return ""+argUrl+" is down";
    }

    public static String description(String argUrl,Integer argErrorCode){
        return ""+argErrorCode+" "+argUrl+"";
    }

    public static String add_url = "Add a new url";

    public static String format = "Format: http://www.example.com";

    public static String time_saved = "Time saved";
}
