package com.nilu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootTest
class SpringModuleApplicationTests {

    @Test
    void contextLoads() {
        Calendar calendar = new GregorianCalendar();
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
    }

}
