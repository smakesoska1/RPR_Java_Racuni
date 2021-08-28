package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Kasa {
    private Map<Proizvod,Integer> stanje=new HashMap<>();
    private List<Racun> racuni=new ArrayList<>();
    private Long idRacuna=100L;

    public Kasa(){

    }


    public Map<Proizvod, Integer> getStanje() {
        return stanje;
    }

    public List<Racun> getRacuni() {
        return racuni;
    }


    public void dodajProizvod(Proizvod proizvod, int kolicina) {
        if(kolicina<=0) throw new IllegalArgumentException("Količina mora biti veća od 0");
        if(stanje.containsKey(proizvod)) {
            stanje.put(proizvod, stanje.get(proizvod) + kolicina);
        }else{
            stanje.put(proizvod,kolicina);}
    }

    public Racun otvoriKupovinu(Map<Proizvod, Integer> primila, LocalDateTime datum) throws NedozvoljenaAkcijaException {
            Racun racun=new Racun(idRacuna,datum);
            idRacuna++;

            for(Map.Entry<Proizvod,Integer> e:primila.entrySet()) {
                if (!stanje.containsKey(e.getKey())) {
                    throw new IllegalArgumentException("Proizvod ne postoji u prodavnici");
                }
                Proizvod p = e.getKey();
                if (stanje.get(p) < e.getValue()) {
                    throw new IllegalArgumentException("Na stanju je " + stanje.get(p) + " proizvoda " + p.getNaziv() + ", a vi pokušavate dodati " + e.getValue());
                }
                racun.dodajProizvod(p, e.getValue());
                stanje.put(p, stanje.get(p) - e.getValue());
                if (e.getValue() <= 0) {
                    throw new IllegalArgumentException("“Količina mora biti veća od 0");
                }
            }
                racuni.add(racun);
                return racun;
    }


    public Racun kreirajKupovinu(Map<Proizvod, Integer> primila, LocalDateTime datum) throws NedozvoljenaAkcijaException {
                Racun racun=otvoriKupovinu(primila,datum);
                racun.zatvoriRacun();
                return racun;
    }


    public Racun dopuniKupovinu(Long id, Map<Proizvod, Integer> primljena) throws NedozvoljenaAkcijaException {
        Optional<Racun> noviRacun=racuni.stream().filter(racun -> racun.getId().equals(id) && !racun.isZatvoren()).findFirst();

        if(!noviRacun.isPresent())
            throw new IllegalArgumentException("Račun #"+id+ "nije otvoren");

        for(Map.Entry<Proizvod,Integer>e:primljena.entrySet()){
            if(!stanje.containsKey(e.getKey()))
                throw new IllegalArgumentException("Proizvod ne postoji u prodavnici");

            Proizvod p = e.getKey();
            if (stanje.get(p) < e.getValue()) {
                throw new IllegalArgumentException("Na stanju je " + stanje.get(p) + " proizvoda " + p.getNaziv() + ", a vi pokušavate dodati " + e.getValue());
            }

            if(e.getValue()<0){
                noviRacun.get().obrisiProizvod(p,e.getValue()*-1);
                stanje.put(p,stanje.get(p)+e.getValue()*-1);
            }
            else if(e.getValue()!=0){
                noviRacun.get().dodajProizvod(p,e.getValue());
                stanje.put(p,stanje.get(p)+e.getValue());
            }
        }
        return noviRacun.get();
    }


    public boolean zakljuciKupovinu(long id) {
        Optional<Racun> noviRacun=racuni.stream().filter(racun -> racun.getId().equals(id) && !racun.isZatvoren()).findFirst();
        if(noviRacun.isPresent()){
            noviRacun.get().zatvoriRacun();
            return true;
        }
        return false;
    }


    public List<Racun> filtrirajRacune(Predicate<Racun> kriterij) {
        return racuni.stream().filter(kriterij).collect(Collectors.toList());
    }

    public List<Racun> dajRacuneZaDatum(LocalDate datum) {
        return filtrirajRacune(racun -> racun.getDatumIzdavanja().toLocalDate().equals(datum));
    }


    public List<Racun> dajRacunePreko(int cijena) {
        return filtrirajRacune(racun -> racun.dajUkupnuCijenu()>cijena);
    }


    public Set<Racun> dajSortiranePoDatumu() {
        return new TreeSet<>(racuni);
    }

    public String presjekDana(LocalDate datum){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder rezultat = new StringBuilder("Računi za " + datum.format(formatter) + ":\n---\n");
        Set<Racun> racuniZaDatum = new TreeSet<>(dajRacuneZaDatum(datum));
        double ukupno = 0;
        for (Racun racun : racuniZaDatum){
            rezultat.append(racun).append("\n---\n");
            ukupno += racun.dajUkupnuCijenu();
        }

        rezultat.append("Ukupno: ").append(String.format("%.2f", ukupno)).append(" KM");
        return rezultat.toString();
    }


}
