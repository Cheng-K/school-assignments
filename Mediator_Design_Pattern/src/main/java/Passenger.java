public abstract class Passenger {
    private final Passport passport;
    private String name;
    private String facialData;
    private String thumbprintData;

    enum TYPE {NORMAL, FAKE_PASSPORT, FAIL_VERIFICATION, REQUIRE_THUMBPRINT}

    public Passenger(String name, TYPE passengerType) {
        passport = new Passport();
        this.name = name;
    }
}

class NormalPassenger extends Passenger {

    public NormalPassenger(String name) {
        super(name, TYPE.NORMAL);
    }
}

class FakePassportPassenger extends Passenger {
    public FakePassportPassenger(String name) {
        super(name, TYPE.FAKE_PASSPORT);
    }
}

class FailVerificationPassenger extends Passenger {
    public FailVerificationPassenger(String name) {
        super(name, TYPE.FAIL_VERIFICATION);
    }
}

class RequireThumbprintPassenger extends Passenger {
    public RequireThumbprintPassenger(String name) {
        super(name, TYPE.REQUIRE_THUMBPRINT);
    }
}





