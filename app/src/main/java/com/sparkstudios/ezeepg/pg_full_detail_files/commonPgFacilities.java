package com.sparkstudios.ezeepg.pg_full_detail_files;

public class commonPgFacilities {
    private boolean wifi,nearCampus,meals,roWater,ac,cctv,tea,lift;

    public commonPgFacilities(boolean wifi, boolean nearCampus, boolean meals, boolean roWater, boolean ac, boolean cctv, boolean tea, boolean lift) {
        this.wifi = wifi;
        this.nearCampus = nearCampus;
        this.meals = meals;
        this.roWater = roWater;
        this.ac = ac;
        this.cctv = cctv;
        this.tea = tea;
        this.lift = lift;
    }

     boolean isWifi() {
        return wifi;
    }

     boolean isNearCampus() {
        return nearCampus;
    }

     boolean isMeals() {
        return meals;
    }

     boolean isRoWater() {
        return roWater;
    }

     boolean isAc() {
        return ac;
    }

     boolean isCctv() {
        return cctv;
    }

     boolean isTea() {
        return tea;
    }

     boolean isLift() {
        return lift;
    }

    public commonPgFacilities() {
    }
}
