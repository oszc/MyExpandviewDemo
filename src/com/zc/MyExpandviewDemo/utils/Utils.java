package com.zc.MyExpandviewDemo.utils;

import java.util.List;
import java.util.Map;

/**
 * 1/19/15  5:36 PM
 * Created by JustinZhang.
 */
public class Utils {
    public static List<String> getValueFromMap(Map<String,List<String>> data,int val){

        int index = 0;

        for(Map.Entry<String,List<String>> entry:data.entrySet()){
            if(index == val){
                return entry.getValue();
            }
            index++;
        }
        return null;
    }

    public static String getKeyFromMap(Map<String,List<String>> data,int val){

        int index = 0;

        for(Map.Entry<String,List<String>> entry:data.entrySet()){
            if(index == val){
                return entry.getKey();
            }
            index++;
        }
        return null;
    }
}
