package me.imlc.aliddns;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    public void canGetPublicIpFromIpIpNet() throws IOException {
        Utils u = new Utils();
        String ip = u.getPublicIpFromIpIpNet();
        System.out.println("Get public ip from www.ipip.net: " + ip);
        assertTrue(ip.length() > 0);
    }

}
