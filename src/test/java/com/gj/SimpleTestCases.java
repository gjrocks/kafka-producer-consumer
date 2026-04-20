package com.gj;

import org.junit.jupiter.api.Test;

public class SimpleTestCases {

    @Test
    public void simpleTest(){

        String city="New  Delhi";
        System.out.println("City is: " + city);
        city=city.replace(" ","_").toLowerCase();
        System.out.println("City after replace is: " + city);
    }
}
