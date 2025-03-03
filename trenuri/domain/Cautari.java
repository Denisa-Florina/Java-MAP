package src.trenuri.domain;

import java.util.Objects;

public class Cautari {
    private String id;
    private String idDeparture;
    private String idDestination;

    public Cautari(String id, String idDeparture, String idDestination) {
        this.id = id;
        this.idDeparture = idDeparture;
        this.idDestination = idDestination;
    }

    public String getId() {
        return id;
    }

    public String getIdDeparture() {
        return idDeparture;
    }

    public String getIdDestination() {
        return idDestination;
    }

    public void setIdDeparture(String idDeparture) {
        this.idDeparture = idDeparture;
    }

    public void setIdDestination(String idDestination) {
        this.idDestination = idDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cautari cautari = (Cautari) o;
        return Objects.equals(id, cautari.id) && Objects.equals(idDeparture, cautari.idDeparture) && Objects.equals(idDestination, cautari.idDestination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idDeparture, idDestination);
    }
}

