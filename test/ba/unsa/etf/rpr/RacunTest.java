package ba.unsa.etf.rpr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RacunTest {
    private static List<Proizvod> proizvodi = new ArrayList<>();
    private static LocalDateTime datum;

    @BeforeAll
    public static void setUpClass() throws NeispravanFormatException {
        proizvodi.add(new Proizvod("1234567891234", "Kafa", 1.5));
        proizvodi.add(new Proizvod("1234567898524", "Mlijeko", 1.3));
        proizvodi.add(new Proizvod("1234946891234", "Sok", 2));
        proizvodi.add(new Proizvod("1234567891147", "Čokolada", 2.4));

        datum = LocalDateTime.of(2021, 2, 23, 11, 35);
    }

    @Test
    void testKonstruktor(){
        Racun racun = new Racun(574L, datum);
        assertAll(
                () -> assertEquals(574L, racun.getId()),
                () -> assertEquals(datum, racun.getDatumIzdavanja()),
                () -> assertTrue(racun.getProizvodi().isEmpty()),
                () -> assertFalse(racun.isZatvoren())
        );
    }

    @Test
    void testDodajProizvod1() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        Map<Proizvod, Integer> proizvodiSRacuna = racun.getProizvodi();
        assertAll(
                () -> assertEquals(1, proizvodiSRacuna.size()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(proizvodiSRacuna.containsKey(proizvodi.get(0))),
                () -> assertEquals(3, proizvodiSRacuna.get(proizvodi.get(0)))
        );
    }

    @Test
    void testDodajProizvod2() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        racun.dodajProizvod(proizvodi.get(1), 4);
        Map<Proizvod, Integer> proizvodiSRacuna = racun.getProizvodi();
        assertAll(
                () -> assertEquals(2, proizvodiSRacuna.size()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(proizvodiSRacuna.containsKey(proizvodi.get(0))),
                () -> assertTrue(proizvodiSRacuna.containsKey(proizvodi.get(1))),
                () -> assertEquals(3, proizvodiSRacuna.get(proizvodi.get(0))),
                () -> assertEquals(4, proizvodiSRacuna.get(proizvodi.get(1)))
        );
    }

    @Test
    void testDodajProizvod3(){
        Racun racun = new Racun(574L, datum);
        racun.zatvoriRacun();
        Throwable exception = assertThrows(NedozvoljenaAkcijaException.class,
                () -> racun.dodajProizvod(proizvodi.get(0), 3));
        assertEquals("Račun #574 je već zatvoren", exception.getMessage());
    }

    @Test
    void testDodajProizvod4(){
        Racun racun = new Racun(574L, datum);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> racun.dodajProizvod(proizvodi.get(0), -2));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());
    }

    @Test
    void testDodajProizvod5(){
        Racun racun = new Racun(574L, datum);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> racun.dodajProizvod(proizvodi.get(0), 0));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());
    }

    @Test
    void testDodajProizvod6() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        racun.dodajProizvod(proizvodi.get(0), 4);
        Map<Proizvod, Integer> proizvodiSRacuna = racun.getProizvodi();
        assertAll(
                () -> assertEquals(1, proizvodiSRacuna.size()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(proizvodiSRacuna.containsKey(proizvodi.get(0))),
                () -> assertEquals(7, proizvodiSRacuna.get(proizvodi.get(0)))
        );
    }

    @Test
    void testObrisiProizvod1() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        Map<Proizvod, Integer> prijeBrisanja = new HashMap<>(racun.getProizvodi());
        racun.obrisiProizvod(proizvodi.get(0), 2);
        Map<Proizvod, Integer> poslijeBrisanja = racun.getProizvodi();
        assertAll(
                () -> assertEquals(1, prijeBrisanja.size()),
                () -> assertEquals(1, poslijeBrisanja.size()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(prijeBrisanja.containsKey(proizvodi.get(0))),
                () -> assertEquals(3, prijeBrisanja.get(proizvodi.get(0))),
                () -> assertTrue(poslijeBrisanja.containsKey(proizvodi.get(0))),
                () -> assertEquals(1, poslijeBrisanja.get(proizvodi.get(0)))
        );
    }

    @Test
    void testObrisiProizvod2(){
        Racun racun = new Racun(544L, datum);
        racun.zatvoriRacun();
        Throwable exception = assertThrows(NedozvoljenaAkcijaException.class,
                () -> racun.obrisiProizvod(proizvodi.get(0), 3));
        assertEquals("Račun #544 je već zatvoren", exception.getMessage());
    }

    @Test
    void testObrisiProizvod3() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(544L, datum);
        racun.dodajProizvod(proizvodi.get(0), 7);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> racun.obrisiProizvod(proizvodi.get(0), -3));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());
    }

    @Test
    void testObrisiProizvod4() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        Map<Proizvod, Integer> prijeBrisanja = new HashMap<>(racun.getProizvodi());
        racun.obrisiProizvod(proizvodi.get(0), 7);
        Map<Proizvod, Integer> poslijeBrisanja = racun.getProizvodi();
        assertAll(
                () -> assertTrue(poslijeBrisanja.isEmpty()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(prijeBrisanja.containsKey(proizvodi.get(0))),
                () -> assertEquals(3, prijeBrisanja.get(proizvodi.get(0))),
                () -> assertFalse(poslijeBrisanja.containsKey(proizvodi.get(0)))
        );
    }

    @Test
    void testObrisiProizvod5() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        racun.dodajProizvod(proizvodi.get(1), 8);
        Map<Proizvod, Integer> prijeBrisanja = new HashMap<>(racun.getProizvodi());
        racun.obrisiProizvod(proizvodi.get(0), 3);
        Map<Proizvod, Integer> poslijeBrisanja = racun.getProizvodi();
        assertAll(
                () -> assertEquals(2, prijeBrisanja.size()),
                () -> assertEquals(1, poslijeBrisanja.size()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(prijeBrisanja.containsKey(proizvodi.get(0))),
                () -> assertTrue(prijeBrisanja.containsKey(proizvodi.get(1))),
                () -> assertEquals(3, prijeBrisanja.get(proizvodi.get(0))),
                () -> assertFalse(poslijeBrisanja.containsKey(proizvodi.get(0)))
        );
    }

    @Test
    void testObrisiProizvod6() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 3);
        racun.dodajProizvod(proizvodi.get(1), 8);
        Map<Proizvod, Integer> prijeBrisanja = new HashMap<>(racun.getProizvodi());
        racun.obrisiProizvod(proizvodi.get(1));
        Map<Proizvod, Integer> poslijeBrisanja = racun.getProizvodi();
        assertAll(
                () -> assertEquals(2, prijeBrisanja.size()),
                () -> assertEquals(1, poslijeBrisanja.size()),
                () -> assertFalse(racun.isZatvoren()),
                () -> assertTrue(prijeBrisanja.containsKey(proizvodi.get(0))),
                () -> assertTrue(prijeBrisanja.containsKey(proizvodi.get(1))),
                () -> assertEquals(8, prijeBrisanja.get(proizvodi.get(1))),
                () -> assertFalse(poslijeBrisanja.containsKey(proizvodi.get(1)))
        );
    }

    @Test
    public void testZatvori(){
        Racun racun = new Racun(574L, datum);
        racun.zatvoriRacun();
        assertTrue(racun.isZatvoren());
    }

    @Test
    public void testUkupnaCijena() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(574L, datum);
        racun.dodajProizvod(proizvodi.get(0), 2);
        racun.dodajProizvod(proizvodi.get(1), 3);
        racun.dodajProizvod(proizvodi.get(2), 1);
        racun.dodajProizvod(proizvodi.get(3), 2);
        double cijena1 = racun.dajUkupnuCijenu();

        racun.dodajProizvod(proizvodi.get(0), 3);
        racun.obrisiProizvod(proizvodi.get(1), 1);
        double cijena2 = racun.dajUkupnuCijenu();

        assertAll(
                () -> assertEquals(13.7, cijena1),
                () -> assertEquals(16.9, cijena2)
        );
    }

    @Test
    public void testToString1() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(228L, datum);
        racun.dodajProizvod(proizvodi.get(0),2);
        racun.dodajProizvod(proizvodi.get(2),3);
        String rez1 = "Račun #228 (23.02.2021. 11:35):\nKafa, 2 kom, 3.00 KM\nSok, 3 kom, 6.00 KM\nUkupno: 9.00 KM";
        String rez2 = "Račun #228 (23.02.2021. 11:35):\nSok, 3 kom, 6.00 KM\nKafa, 2 kom, 3.00 KM\nUkupno: 9.00 KM";

        assertTrue(racun.toString().equals(rez1) || racun.toString().equals(rez2));
    }

    @Test
    public void testToString2() throws NedozvoljenaAkcijaException {
        Racun racun = new Racun(228L, datum);
        racun.dodajProizvod(proizvodi.get(0),3);
        racun.dodajProizvod(proizvodi.get(1),3);
        String rez1 = "Račun #228 (23.02.2021. 11:35):\nKafa, 3 kom, 4.50 KM\nMlijeko, 3 kom, 3.90 KM\nUkupno: 8.40 KM";
        String rez2 = "Račun #228 (23.02.2021. 11:35):\nMlijeko, 3 kom, 3.90 KM\nKafa, 3 kom, 4.50 KM\nUkupno: 8.40 KM";

        assertTrue(racun.toString().equals(rez1) || racun.toString().equals(rez2));
    }
}
