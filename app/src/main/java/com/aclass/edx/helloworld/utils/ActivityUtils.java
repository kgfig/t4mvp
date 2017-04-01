package com.aclass.edx.helloworld.utils;

import java.util.Random;

/**
 * Created by tictocproject on 01/04/2017.
 */

public class ActivityUtils {
    public static String generateRandomFilename() {
        Random random = new Random();
        return String.valueOf(random.nextLong());
    }
}
