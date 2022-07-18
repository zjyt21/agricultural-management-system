package com.hlp;

import com.hlp.agrisys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class AgriculturalManagementSystemApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testBCryptPasswordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encode = encoder.encode("admin");
//        System.out.println(encode);
        //$2a$10$H3VUUJeatoXUoAH4WMx.ve56c5Pq5V0/5VgvJjun890bAcZ9WLTZa
        System.out.println(encoder.matches("admin", "$2a$10$H3VUUJeatoXUoAH4WMx.ve56c5Pq5V0/5VgvJjun890bAcZ9WLTZa"));
    }

    @Test
    void contextLoads() {
    }

}
