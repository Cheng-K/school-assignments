package com.apcs;

/*Description : The Passenger class is an abstract class that contains all necessary methods that will be needed by
 * the simulated system. Concrete implementations will be needed. It is made as an abstract class to allow flexibility
 * and variations in the actual passengers implemented.
 * */
public abstract class Passenger {
    private Passport passport;
    private String name;
    private String facialData;
    private String thumbprintData;


    // Getters and Setters
    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacialData() {
        return facialData;
    }

    public void setFacialData(String facialData) {
        this.facialData = facialData;
    }

    public String getThumbprintData() {
        return thumbprintData;
    }

    public void setThumbprintData(String thumbprintData) {
        this.thumbprintData = thumbprintData;
    }

    public void enterPlatform() {
        System.out.println(name + " : Entered the passport control system platform.");
    }

    public void exitPlatform() {
        System.out.println(name + " : Exited the passport control system platform.");
    }

    public void scanPassport() {
        System.out.println(name + " : Placed passport for scanning.");
    }

    public void scanThumb() {
        System.out.println(name + " : Placed thumb for scanning.");
    }


}

/* Description: A simple implementation of the abstract passenger class. This passenger behaves normally as expected for
 * the entire simulation.
 * */
class NormalPassenger extends Passenger {
    public NormalPassenger(String name) {
        super();
        setFacialData(name);
        setName(name);
        setThumbprintData(name);
        setPassport(new Passport(name));
    }
}







