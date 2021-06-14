package com.regent.rpush.route;

import com.regent.rpush.route.utils.sdk.SdkGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
