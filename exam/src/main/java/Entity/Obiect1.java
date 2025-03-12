package Entity;

public class Obiect1 extends Entity {
    private String formatie;
    private String titlu;
    private String gen;
    private String durata;

    public Obiect1(int id, String formatie, String titlu, String gen, String durata) {
        super(id);
        this.formatie = formatie;
        this.titlu = titlu;
        this.gen = gen;
        this.durata = durata;
    }

    public String getFormatie() {
        return formatie;
    }
    public void setFormatie(String formatie) {
        this.formatie = formatie;
    }
    public String getTitlu() {
        return titlu;
    }
    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }
    public String getGen() {
        return gen;
    }
    public void setGen(String gen) {
        this.gen = gen;
    }
    public String getDurata() {
        return durata;
    }
    public void setDurata(String durata) {
        this.durata = durata;
    }

    @Override
    public String toString() {
        return "Piesa {" + "id=" + id + ", " + "formatie=" + formatie + ", " + "titlu=" + titlu + ", " + "gen=" + gen + ", " + "durata=" + durata + '}';
    }
}