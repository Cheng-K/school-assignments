public abstract class Passenger {
    private Passport passport;
    private String name;
    private String facialData;
    private String thumbprintData;

    enum TYPE {NORMAL, FAKE_PASSPORT, FAIL_VERIFICATION, REQUIRE_THUMBPRINT}

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

}

class NormalPassenger extends Passenger {
    public NormalPassenger(String name) {
        super();
        setFacialData(name);
        setName(name);
        setThumbprintData(name);
        setPassport(new Passport(name));
    }
}

class FakePassportPassenger extends Passenger {

}

class FailVerificationPassenger extends Passenger {

}

class RequireThumbprintPassenger extends Passenger {

}





