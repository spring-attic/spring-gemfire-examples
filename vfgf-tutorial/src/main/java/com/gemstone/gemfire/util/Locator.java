package com.gemstone.gemfire.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Locator {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String resource = ("spring-cache-locator-context.xml");
        ClassPathXmlApplicationContext mainContext = new ClassPathXmlApplicationContext(new String[] {resource}, false);
        mainContext.setValidating(true);
        mainContext.refresh();

        Thread.sleep(Long.MAX_VALUE);
    }

}
