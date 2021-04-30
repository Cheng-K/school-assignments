package com.company;

public class SportCenter {
    private String code;
    private String location;
    private Admin masterAdmin;

    public SportCenter(String username,String password){
        masterAdmin = Admin.adminLogin(username,password);
        if (masterAdmin != null){
            code = masterAdmin.getSportsCenterCode();
            String[] sportCenterFile = FileServer.readFile("","SportCenters.txt");
            for (String line : sportCenterFile){
                String[] tokens = line.split("\\|");
                if (tokens[0].equals(code)) {
                    location = tokens[1];
                    break;
                }
            }
        }
        else
            System.out.println("WRONG USERNAME/PASSWORD");

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Admin getMasterAdmin() {
        return masterAdmin;
    }

    public void setMasterAdmin(Admin masterAdmin) {
        this.masterAdmin = masterAdmin;
    }








}
