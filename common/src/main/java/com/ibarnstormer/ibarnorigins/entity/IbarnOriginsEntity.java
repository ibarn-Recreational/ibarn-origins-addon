package com.ibarnstormer.ibarnorigins.entity;

public interface IbarnOriginsEntity {

    void setSpellCastTicks(int i);
    int getSpellCastTicks();

    boolean onSoulMageFire();
    void setOnSoulMageFire(boolean b);

    boolean isSoulMage();
    boolean isSandPerson();

    void setSoulMage(boolean b);
    void setSandPerson(boolean b);

}
