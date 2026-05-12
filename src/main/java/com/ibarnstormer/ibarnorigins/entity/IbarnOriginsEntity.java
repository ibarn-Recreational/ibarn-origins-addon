package com.ibarnstormer.ibarnorigins.entity;

public interface IbarnOriginsEntity {

    void setSpellCastTicks(int i);
    int getSpellCastTicks();

    boolean onSoulMageFire();
    void setOnSoulMageFire(boolean b);

    boolean onSoulFire();
    void setOnSoulFire(boolean b);

    boolean inflated();
    void setInflated(boolean b);

    boolean fireWeaknessShaking();
    void setShakingFromFireWeakness(boolean b);

    boolean isSoulMage();
    boolean isSandPerson();

    void setSoulMage(boolean b);
    void setSandPerson(boolean b);

}
