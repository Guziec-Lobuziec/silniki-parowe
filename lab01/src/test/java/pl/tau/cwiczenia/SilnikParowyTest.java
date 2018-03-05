package pl.tau.cwiczenia;

import static org.junit.Assert.*;

import org.junit.Test;

public class SilnikParowyTest {

    @Test
    public void testWtryskPary() {
        int objetoscDzialania = 3000;
        SilnikParowy silnik = new SilnikParowy(objetoscDzialania);
        assertNotNull(silnik);
        assertFalse(silnik.tlokWysuniety());
        silnik.pobierzPare(1000);
        assertFalse(silnik.tlokWysuniety());
        silnik.pobierzPare(3000);
        assertTrue(silnik.tlokWysuniety());
        assertEquals(silnik.iloscPary(), 0);

    }

}
