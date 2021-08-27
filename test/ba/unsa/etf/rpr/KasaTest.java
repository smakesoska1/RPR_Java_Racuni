/*ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KasaTest {
    private static List<Proizvod> proizvodi = new ArrayList<>();
    private static LocalDateTime datum;
    private Kasa kasa;

    @BeforeAll
    public static void setUpClass() throws NeispravanFormatException {
        proizvodi.add(new Proizvod("1234567891234", "Kafa", 1.5));
        proizvodi.add(new Proizvod("1234567898524", "Mlijeko", 1.3));
        proizvodi.add(new Proizvod("1234946891234", "Sok", 2));
        proizvodi.add(new Proizvod("1234567891147", "Čokolada", 2.4));

        datum = LocalDateTime.of(2021, 2, 23, 11, 45);
    }

    @BeforeEach
    public void setUpTest(){
        kasa = new Kasa();
        proizvodi.forEach(proizvod -> kasa.dodajProizvod(proizvod, 100));
    }

    @Test
    void testKonstruktorGetter(){
        Kasa kasa = new Kasa();
        assertAll(
                () -> assertTrue(kasa.getRacuni().isEmpty()),
                () -> assertTrue(kasa.getStanje().isEmpty())
        );
    }

    @Test
    void testDodajProizvod1(){
        Kasa kasa = new Kasa();
        kasa.dodajProizvod(proizvodi.get(0), 100);
        assertAll(
                () -> assertEquals(100, kasa.getStanje().get(proizvodi.get(0))),
                () -> assertEquals(1, kasa.getStanje().size())
        );
    }

    @Test
    void testDodajProizvod2(){
        Kasa kasa = new Kasa();
        kasa.dodajProizvod(proizvodi.get(0), 100);
        kasa.dodajProizvod(proizvodi.get(0), 30);
        assertAll(
                () -> assertEquals(130, kasa.getStanje().get(proizvodi.get(0))),
                () -> assertEquals(1, kasa.getStanje().size())
        );
    }

    @Test
    void testDodajProizvod3(){
        Kasa kasa = new Kasa();
        kasa.dodajProizvod(proizvodi.get(0), 100);
        kasa.dodajProizvod(proizvodi.get(1), 30);
        assertAll(
                () -> assertEquals(100, kasa.getStanje().get(proizvodi.get(0))),
                () -> assertEquals(30, kasa.getStanje().get(proizvodi.get(1))),
                () -> assertEquals(2, kasa.getStanje().size())
        );
    }

    @Test
    void testDodajProizvod4(){
        Kasa kasa = new Kasa();
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.dodajProizvod(proizvodi.get(0), 0));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.dodajProizvod(proizvodi.get(0), -3));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());
    }

    @Test
    void testKreirajKupovinu1() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);

        Racun racun = kasa.kreirajKupovinu(zaDodati, datum);
        assertAll(
                () -> assertTrue(racun.isZatvoren()),
                () -> assertEquals(3, racun.getProizvodi().size()),
                () -> assertEquals(datum, racun.getDatumIzdavanja()),
                () -> assertEquals(95, kasa.getStanje().get(proizvodi.get(0))),
                () -> assertEquals(96, kasa.getStanje().get(proizvodi.get(1))),
                () -> assertEquals(97, kasa.getStanje().get(proizvodi.get(3))),
                () -> assertEquals(100, kasa.getStanje().get(proizvodi.get(2))),
                () -> assertEquals(5, racun.getProizvodi().get(proizvodi.get(0))),
                () -> assertEquals(100, racun.getId()),
                () -> assertTrue(kasa.getRacuni().contains(racun))
        );
    }

    @Test
    void testKreirajKupovinu2() throws NeispravanFormatException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(new Proizvod("0000000000000", "Ne postoji", 4.5), 3);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.kreirajKupovinu(zaDodati, datum));
        assertEquals("Proizvod ne postoji u prodavnici", exception.getMessage());
    }

    @Test
    void testKreirajKupovinu3() {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 103);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.kreirajKupovinu(zaDodati, datum));
        assertEquals("Na stanju je 100 proizvoda Čokolada, a vi pokušavate dodati 103", exception.getMessage());
    }

    @Test
    void testKreirajKupovinu4() {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), -8);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.kreirajKupovinu(zaDodati, datum));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());
    }

    @Test
    void testOtvoriKupovinu1() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);

        Racun racun = kasa.otvoriKupovinu(zaDodati, datum);
        assertAll(
                () -> assertFalse(racun.isZatvoren()),
                () -> assertEquals(3, racun.getProizvodi().size()),
                () -> assertEquals(datum, racun.getDatumIzdavanja()),
                () -> assertEquals(95, kasa.getStanje().get(proizvodi.get(0))),
                () -> assertEquals(96, kasa.getStanje().get(proizvodi.get(1))),
                () -> assertEquals(97, kasa.getStanje().get(proizvodi.get(3))),
                () -> assertEquals(100, kasa.getStanje().get(proizvodi.get(2))),
                () -> assertEquals(5, racun.getProizvodi().get(proizvodi.get(0))),
                () -> assertEquals(100, racun.getId()),
                () -> assertTrue(kasa.getRacuni().contains(racun))
        );
    }

    @Test
    void testOtvoriKupovinu2() throws NeispravanFormatException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(new Proizvod("0000000000000", "Ne postoji", 4.5), 3);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.otvoriKupovinu(zaDodati, datum));
        assertEquals("Proizvod ne postoji u prodavnici", exception.getMessage());
    }

    @Test
    void testOtvoriKupovinu3() {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 103);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.otvoriKupovinu(zaDodati, datum));
        assertEquals("Na stanju je 100 proizvoda Čokolada, a vi pokušavate dodati 103", exception.getMessage());
    }

    @Test
    void testOtvoriKupovinu4() {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), -8);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> kasa.otvoriKupovinu(zaDodati, datum));
        assertEquals("Količina mora biti veća od 0", exception.getMessage());
    }

    @Test
    void testDopuniKupovinu1() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun = kasa.otvoriKupovinu(zaDodati, datum);

        zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 3);
        zaDodati.put(proizvodi.get(2), 7);
        kasa.dopuniKupovinu(racun.getId(), zaDodati);

        assertAll(
                () -> assertFalse(racun.isZatvoren()),
                () -> assertEquals(4, racun.getProizvodi().size()),
                () -> assertEquals(8, racun.getProizvodi().get(proizvodi.get(0))),
                () -> assertEquals(7, racun.getProizvodi().get(proizvodi.get(2))),
                () -> assertEquals(38.4, racun.dajUkupnuCijenu())

        );
    }

    @Test
    void testDopuniKupovinu2() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun = kasa.otvoriKupovinu(zaDodati, datum);

        zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(2), 0);
        kasa.dopuniKupovinu(racun.getId(), zaDodati);

        assertAll(
                () -> assertFalse(racun.isZatvoren()),
                () -> assertEquals(3, racun.getProizvodi().size()),
                () -> assertFalse(racun.getProizvodi().containsKey(proizvodi.get(2)))
        );
    }

    @Test
    void testDopuniKupovinu3() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun = kasa.otvoriKupovinu(zaDodati, datum);

        zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), -4);
        kasa.dopuniKupovinu(racun.getId(), zaDodati);

        assertAll(
                () -> assertFalse(racun.isZatvoren()),
                () -> assertEquals(3, racun.getProizvodi().size()),
                () -> assertEquals(1, racun.getProizvodi().get(proizvodi.get(0))),
                () -> assertEquals(99, kasa.getStanje().get(proizvodi.get(0)))
        );
    }

    @Test
    void testZakljuciKupovinu1() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun = kasa.otvoriKupovinu(zaDodati, datum);

        boolean status = kasa.zakljuciKupovinu(racun.getId());
        assertTrue(racun.isZatvoren());
        assertTrue(status);
    }

    @Test
    void testZakljuciKupovinu2() {
        assertFalse( kasa.zakljuciKupovinu(578L));
    }

    @Test
    void testFiltrirajRacune() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        kasa.otvoriKupovinu(zaDodati, datum);
        Racun racun2 = kasa.kreirajKupovinu(zaDodati, datum.plusHours(2));
        kasa.otvoriKupovinu(zaDodati, datum.plusHours(3));

        List<Racun> filtrirano = kasa.filtrirajRacune(Racun::isZatvoren);
        assertAll(
                () -> assertTrue(filtrirano.contains(racun2)),
                () -> assertEquals(1, filtrirano.size())
        );
    }

    @Test
    void testDajRacuneZaDatum() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun1 = kasa.otvoriKupovinu(zaDodati, datum.plusDays(1));
        Racun racun2 = kasa.kreirajKupovinu(zaDodati, datum.plusDays(1).plusHours(2));
        kasa.otvoriKupovinu(zaDodati, datum.plusHours(3));

        List<Racun> filtrirano = kasa.dajRacuneZaDatum(datum.plusDays(1).toLocalDate());
        assertAll(
                () -> assertTrue(filtrirano.contains(racun1)),
                () -> assertTrue(filtrirano.contains(racun2)),
                () -> assertEquals(2, filtrirano.size())
        );
    }

    @Test
    void testDajRacunePreko() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        kasa.kreirajKupovinu(zaDodati, datum.plusDays(1));

        zaDodati.put(proizvodi.get(3), 8);
        Racun racun2 = kasa.kreirajKupovinu(zaDodati, datum.plusDays(1).plusHours(2));

        zaDodati.put(proizvodi.get(2), 4);
        Racun racun3 = kasa.kreirajKupovinu(zaDodati, datum.plusHours(3));

        List<Racun> filtrirano1 = kasa.dajRacunePreko(30);
        List<Racun> filtrirano2 = kasa.dajRacunePreko(35);
        assertAll(
                () -> assertTrue(filtrirano1.contains(racun2)),
                () -> assertTrue(filtrirano1.contains(racun3)),
                () -> assertEquals(2, filtrirano1.size()),
                () -> assertTrue(filtrirano2.contains(racun3)),
                () -> assertEquals(1, filtrirano2.size())
        );
    }

    @Test
    void testDajSortiranePoDatumu() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun1 = kasa.otvoriKupovinu(zaDodati, datum.plusDays(1));
        Racun racun2 = kasa.kreirajKupovinu(zaDodati, datum.plusDays(1).minusHours(3));
        Racun racun3 = kasa.otvoriKupovinu(zaDodati, datum.plusHours(3));

        Set<Racun> filtrirano = kasa.dajSortiranePoDatumu();
        List<Racun> filtriranoLista = new ArrayList<>(filtrirano);

        assertAll(
                () -> assertEquals(racun1, filtriranoLista.get(2)),
                () -> assertEquals(racun2, filtriranoLista.get(1)),
                () -> assertEquals(racun3, filtriranoLista.get(0))
        );
    }

    @Test
    void testPresjekDana() throws NedozvoljenaAkcijaException {
        Map<Proizvod, Integer> zaDodati = new HashMap<>();
        zaDodati.put(proizvodi.get(0), 5);
        zaDodati.put(proizvodi.get(1), 4);
        zaDodati.put(proizvodi.get(3), 3);
        Racun racun1 = kasa.otvoriKupovinu(zaDodati, datum.plusDays(1));
        Racun racun2 = kasa.kreirajKupovinu(zaDodati, datum.plusDays(1).plusHours(3));
        Racun racun3 = kasa.otvoriKupovinu(zaDodati, datum.plusHours(3));

        String rezultat = "Računi za 24.02.2021:\n---\n";
        rezultat += racun1.toString();
        rezultat += "\n---\n";
        rezultat += racun2.toString();
        rezultat += "\n---\nUkupno: 39.80 KM";

        String rezultat2 = "Računi za 23.02.2021:\n---\n";
        rezultat2 += racun3.toString();
        rezultat2 += "\n---\nUkupno: 19.90 KM";

        assertEquals(rezultat, kasa.presjekDana(datum.plusDays(1).toLocalDate()));
        assertEquals(rezultat2, kasa.presjekDana(datum.toLocalDate()));
    }
}*/