package com.toy.component.user.domain;

import java.util.Set;

/**
 * Created by roger on 2017/3/17.
 */
public class Socre {
    public Socre (int idA,int idB,boolean isSimilar,int id) {
        this.idA = idA;
        this.idB = idB;
        this.isSimilar=isSimilar;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    int idA;

    int idB;

    boolean isSimilar;

    double similarScore;

    Set<Integer> inter;

    public Set<Integer> getInter() {
        return inter;
    }

    public void setInter(Set<Integer> inter) {
        this.inter = inter;
    }



    public int getIdA() {
        return idA;
    }

    public void setIdA(int idA) {
        this.idA = idA;
    }

    public int getIdB() {
        return idB;
    }

    public void setIdB(int idB) {
        this.idB = idB;
    }

    public boolean isSimilar() {
        return isSimilar;
    }

    public void setSimilar(boolean similar) {
        isSimilar = similar;
    }


    public double getSimilarScore() {
        return similarScore;
    }

    public void setSimilarScore(double similarScore) {
        this.similarScore = similarScore;
    }

}
