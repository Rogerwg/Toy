package com.toy.tools;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by roger on 2017/3/17.
 */
public class StringSimilarTest {
    @Test
    public void isSameNameByFuzzyMatch() throws Exception {

    }

    @Test
    public void getLCStringSize() throws Exception {
        String a = "abcdfehcjq";
        String b = "bcdfghcjq";
        int s = StringSimilar.getLCStringSize(a.toCharArray(), b.toCharArray());
        System.out.println(s);
    }

}