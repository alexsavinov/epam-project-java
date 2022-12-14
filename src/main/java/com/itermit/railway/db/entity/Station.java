package com.itermit.railway.db.entity;

import java.io.Serializable;

/**
 * Station entity.
 *
 * @author O.Savinov
 */
public class Station implements Serializable {

    private int id;
    private String name;

    private Station() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static class Builder {
        private Station station;

        public Builder() {
            station = new Station();
        }

        public Builder withId(int id) {
            station.setId(id);
            return this;
        }

        public Builder withName(String name) {
            station.setName(name);
            return this;
        }

        public Station build() {
            return station;
        }
    }
}
