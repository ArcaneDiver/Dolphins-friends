package com.arcanediver.dolphins_friends;

public class DolphinsFriendsHelpers {

    public static float normalize(float toNormalize, float min, float max) {
        return (2 * ( toNormalize - min ) / (max - min) ) - 1;
    }

    public static float round(float x, int places) {
        return (float) (Math.round(x * Math.pow(10, places)) / Math.pow(10, places));
    }

    public static double round(double x, int places) {
        return Math.round(x * Math.pow(10, places)) / Math.pow(10, places);
    }

}
