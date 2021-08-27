package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProizvodTest {

    @Test
    void testKonstruktor1() throws NeispravanFormatException {
        Proizvod proizvod = new Proizvod("1111111111111", "Kafa", 1.5);
        assertAll(
                () -> assertEquals("1111111111111", proizvod.getBarKod()),
                () -> assertEquals("Kafa", proizvod.getNaziv()),
                () -> assertEquals(1.5, proizvod.getCijena())
        );
    }

    @Test
    void testKonstruktor2() {
        Throwable exception = assertThrows(NeispravanFormatException.class,
                () -> new Proizvod("111111111a111", "Kafa", 1.5));
        assertEquals("Bar kod treba imati 13 cifara", exception.getMessage());

        exception = assertThrows(NeispravanFormatException.class,
                () -> new Proizvod("1111111111", "Kafa", 1.5));
        assertEquals("Bar kod treba imati 13 cifara", exception.getMessage());
    }

   /* @Test
    void testGetterSetter() throws NeispravanFormatException {
        Proizvod proizvod = new Proizvod(barKod, naziv, cijena);
        proizvod.setBarKod("1112223334445");
        proizvod.setNaziv("Čaj");
        proizvod.setCijena(2);
        assertAll(
                () -> assertEquals("1112223334445", proizvod.getBarKod()),
                () -> assertEquals("Čaj", proizvod.getNaziv()),
                () -> assertEquals(2, proizvod.getCijena())
        );
    }*/

    @Test
    void testSetter2() throws NeispravanFormatException {
        Proizvod proizvod = new Proizvod("1231231231231", "Mlijeko", 1.2);
        Throwable exception = assertThrows(NeispravanFormatException.class,
                () -> proizvod.setBarKod("123"));
        assertEquals("Bar kod treba imati 13 cifara", exception.getMessage());

        exception = assertThrows(NeispravanFormatException.class,
                () -> proizvod.setBarKod("123*231231231"));
        assertEquals("Bar kod treba imati 13 cifara", exception.getMessage());
    }
}
