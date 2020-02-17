package ru.stqa.pft.soap;

import com.lavasoft.GeoIPService;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GeoIpServiceTests {

  @Test
  public void testMyIp() {
    System.out.print("\n\n***** Внутри метода testMyIp() *****\n\n");
    final String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("94.180.104.53");
    System.out.println("ipLocation: " + ipLocation);
    assertEquals(ipLocation, "<GeoIP><Country>RU</Country><State>53</State></GeoIP>");
    assertTrue(ipLocation.contains("RU"));
  }
}
