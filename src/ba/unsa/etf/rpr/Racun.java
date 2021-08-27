package ba.unsa.etf.rpr;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Racun {
    private final Long id;
    private final LocalDateTime datumIzdavanja;
    private Map<Proizvod,Integer> proizvodi=new HashMap<>();
    private boolean zatvoren=false;

   private void provjeriOtvorenRacun() throws NedozvoljenaAkcijaException {
       if(zatvoren) throw new NedozvoljenaAkcijaException("Račun #" + id + " je već zatvoren");
   }

    public Racun(Long id, LocalDateTime datumIzdavanja) {
        this.id = id;
        this.datumIzdavanja = datumIzdavanja;
    }
    public Racun (Long id){
       this.id=id;
       datumIzdavanja= LocalDateTime.now(); //trenutni datum
    }

    public void dodajProizvod(Proizvod proizvod, int kolicina) throws NedozvoljenaAkcijaException {
       provjeriOtvorenRacun();
       if(kolicina<=0) {
           throw new IllegalArgumentException("Količina mora biti veća od 0");
       }
       if(proizvodi.containsKey(proizvod)){
          proizvodi.put(proizvod,proizvodi.get(proizvod)+kolicina);
       }
       else{
           proizvodi.put(proizvod,kolicina);
       }
    }

    public void obrisiProizvod(Proizvod proizvod, int kolicina) throws NedozvoljenaAkcijaException {
        provjeriOtvorenRacun();
        if (kolicina <= 0) {
            throw new IllegalArgumentException("Količina mora biti veća od 0");
        }
        if (proizvodi.containsKey(proizvod)) {
            proizvodi.put(proizvod, proizvodi.get(proizvod) - kolicina);
            if (proizvodi.get(proizvod) <= 0) {
                proizvodi.remove(proizvod);
            }

        }
    }
    public void obrisiProizvod(Proizvod proizvod) throws NedozvoljenaAkcijaException {
       provjeriOtvorenRacun();
       if(proizvodi.containsKey(proizvod))
           proizvodi.remove(proizvod);
    }

    public Long getId() {
        return id;
    }


    public LocalDateTime getDatumIzdavanja() {
        return datumIzdavanja;
    }

    public Map<Proizvod, Integer> getProizvodi() {
        return proizvodi;
    }

    public boolean isZatvoren() {
        return zatvoren;
    }

    public void zatvoriRacun() {
                this.zatvoren=true;
    }

    public double dajUkupnuCijenu() {
       double broj=3;
       return broj;

    }
}
