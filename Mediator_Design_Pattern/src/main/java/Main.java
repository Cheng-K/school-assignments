public class Main {
    public static void main(String[] args)  {
        AutomatedPassportControlSystem apcs = new AutomatedPassportControlSystem();
        Thread automatedPassportControlSystem = new Thread(apcs);
        Thread passengerGenerator = new Thread(new PassengerFactory(apcs,1));
        automatedPassportControlSystem.start();
        passengerGenerator.start();
        try {
            passengerGenerator.join();
            automatedPassportControlSystem.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n\n ------------------- Simulation Program Terminated ---------------------------- ");

        // Testing encryption and decryption
//        String toEncrypt = "Halalled21rq";
//        byte[] encrypted = Utility.encryptData(toEncrypt.getBytes(StandardCharsets.UTF_8));
//        String decrypted = new String(Utility.decryptData(encrypted));
//        System.out.println("Original : " + toEncrypt);
//        System.out.println("Encrypted : " + new String(encrypted));
//        System.out.println("Decrypted : " + decrypted);

        // Testing signing and verifying
//        String toSign = "Lim";
//        byte[] signed;
//        try {
//            signed = Utility.signObject(toSign.getBytes(StandardCharsets.UTF_8));
//            System.out.println(Arrays.toString(signed));
//            System.out.println(Utility.verifyObject(signed));
//        } catch (SignatureException e) {
//            System.out.println("Unable to sign on the object");
//        }





    }
}
