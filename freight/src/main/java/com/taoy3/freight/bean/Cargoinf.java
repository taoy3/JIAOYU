package com.taoy3.freight.bean;

import java.io.Serializable;

/**
 * Created by taoy2 on 15-12-13.
 */
public class Cargoinf  implements Serializable {
    private String name;
    private String cbm;
    private String kgs;

    public Cargoinf(String name, String cbm, String kgs) {
        this.name = name;
        this.cbm = cbm;
        this.kgs = kgs;
    }

    @Override
    public String toString() {
        return name + ":" + cbm +"cｍ³/"+kgs+"kg" ;
    }

    public Cargoinf() {
    }
    public Cargoinf copy(){
        return new Cargoinf(name,cbm,kgs);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCbm() {
        return cbm;
    }

    public void setCbm(String cbm) {
        this.cbm = cbm;
    }

    public String getKgs() {
        return kgs;
    }

    public void setKgs(String kgs) {
        this.kgs = kgs;
    }
}
