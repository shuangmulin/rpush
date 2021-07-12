package com.regent.rpush.route;

import com.regent.rpush.route.utils.sdk.SdkGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Sdk {

    @Autowired
    private SdkGenerator sdkGenerator;

    @Test
    public void generate() {
        sdkGenerator.generate();
    }
}
