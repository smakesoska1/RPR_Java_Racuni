package ba.unsa.etf.rpr;

import java.util.Objects;

public class Proizvod {
    private String barKod;
    private String naziv;
    private double cijena;

    public Proizvod(){

    }

    public Proizvod(String barKod, String naziv, double cijena) throws NeispravanFormatException {
        provjeriBarKod(barKod); //moramo zbog provjere bar koda da ima 13 cifara
        this.barKod = barKod;
        this.naziv = naziv;
        this.cijena = cijena;
    }

    private static void provjeriBarKod(String barKod) throws NeispravanFormatException {
        if(!(barKod.length()==13)) throw new NeispravanFormatException("Bar kod treba imati 13 cifara");

        for(char c:barKod.toCharArray()){ //pretvaram string u niz carova
            if(!Character.isDigit(c)) //biblioteka character sadrzi isDigit provjeru
                throw new NeispravanFormatException("Bar kod treba imati 13 cifara");
        }
    }

    public String getBarKod() {
        return barKod;
    }

    public void setBarKod(String barKod) throws NeispravanFormatException {
        provjeriBarKod(barKod);
        this.barKod = barKod;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

}
