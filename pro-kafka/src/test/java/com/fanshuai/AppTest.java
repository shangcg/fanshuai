package com.fanshuai;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        String text = "111122";
        text = text.replace("1", "");
        System.out.println(text);
    }

    @Test
    public void test() {
        String  s = "123456789";

        Pattern pattern = Pattern.compile("(12)(34)(56)");
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
        }
    }
}
