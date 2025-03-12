package Entity;

import java.util.Date;

public class Inchiriere extends Entitate {
    private Masina masina;
    private Date dataInceput;
    private Date dataSfarsit;

    public Inchiriere(int id, Masina masina, Date dataInceput, Date dataSfarsit) {
        super(id);
        this.masina = masina;
        this.dataInceput = dataInceput;
        this.dataSfarsit = dataSfarsit;
    }

    public Masina getMasina() {
        return masina;
    }

    public void setMasina(Masina masina) { this.masina = masina; }

    public Date getDataInceput() {
        return dataInceput;
    }

    public Date getDataSfarsit() {
        return dataSfarsit;
    }

    @Override
    public String toString() {
        return "Inchiriere{" + "id=" + id + ", masina=" + masina + ", dataInceput=" + dataInceput + ", dataSfarsit=" + dataSfarsit + '}';
    }
}