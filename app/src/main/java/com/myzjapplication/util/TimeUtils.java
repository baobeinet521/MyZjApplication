package com.myzjapplication.util;

public class TimeUtils {
    //时间秒为单位转换为00:00:00格式
    public static String secondsToString(long second){
        String timmHour;
        int hour = (int) Math.floor(second / 3600);
        int min = (int) (Math.floor(second / 60) % 60);
        int sec = (int) (second % 60);
        if(hour < 10){
            timmHour = "0" + String.valueOf(hour) + ":";
        }else{
            timmHour = String.valueOf(hour) + ":";
        }
        if(min < 10) {
            timmHour += "0";
        }
        timmHour += min + ":";
        if(sec < 10) {
            timmHour += "0";
        }
        timmHour += sec;
        return timmHour;
    }
}
