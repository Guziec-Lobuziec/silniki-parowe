package pl.tau.cwiczenia;

import static org.junit.Assert.*;

import org.junit.Test;

public class SilnikParowyTest {

    @Test
    public void testWtryskPary() {
        int objetoscDzialania = 3000;
        SilnikParowy silnik = new SilnikParowy(objetoscDzialania);
        assertNotNull(silnik);
        assertFalse(slinik.tlokWysuniety());
        slinik.pobierzPare(1000);
        assertFalse(slinik.tlokWysuniety());
        silnik.pobierzPare(2000);
        assertTrue(slinik.tlokWysuniety());
        assertEquals(silnik.iloscPary(), 0);

    }

}
