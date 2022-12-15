package com.project.web4.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Data implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private static final long serialVersionUID = 1L;
    @Column(nullable = false)
    private Double x;
    @Column(nullable = false)
    private Double y;
    @Column(nullable = false)
    private Double r;
    @Column(nullable = false)
    private String time;
    @Column(nullable = false)
    private Boolean answer;
    @ManyToOne
    private User user;

    public Data(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public String toString() {
        return "Data{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", time=" + time +
                ", answer=" + answer +
                '}';
    }

    @Override
    public int hashCode() {
        return x.hashCode() + y.hashCode() +
                r.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Data) {
            Data dataObj = (Data) obj;
            return x.equals(dataObj.getX()) &&
                    y.equals(dataObj.getY()) &&
                    r.equals(dataObj.getR()) &&
                    time.equals(dataObj.getTime()) &&
                    answer.equals(dataObj.getAnswer());
        }
        return false;
    }
}