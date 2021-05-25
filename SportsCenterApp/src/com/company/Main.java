package com.company;



public class Main {

    public static void main(String[] args) {
        // TEST FOR FILE SERVER
//        System.out.println(subdir.isDirectory());

//        String[] arr = FileServer.readFile("","Admin.txt");
//        System.out.println(arr.length);
//        for (String string : arr){
//            System.out.println(string);
//        }
//    }
//        FileServer.appendFile("", "Admin.txt", "D*cK\n");
//        for (String str : FileServer.readFile("","Admin.txt")){
//            System.out.println(str);
//        }

        // TEST FOR ADMIN LOGIN
//        String[] content = FileServer.readFile("","Admin.txt");
//        System.out.println(Arrays.toString(content));
//        LoginScreen login = new LoginScreen();

//        String[] coachContent = FileServer.readFile("L002","Coach.txt");
//        Coach mycoach = new Coach(coachContent[0].split("\\|"));
//        System.out.println(mycoach);


//        Student A = new Student(new String[]{"Cheng Kei","S001","19","KL","012-3345","adam@gmail.com","Badminton","L001"});
//        new StudentProfile(A);
//        Admin admin = new Admin("bruh","L001");
////        String[] mylist = {"Boo","null","19","KL","012-3345","adam@gmail.com","Badminton","L001"};
////        admin.createAccount(mylist,"123");
//        admin.approveAccount("CK8");
//
//        String[] studentContent = FileServer.readFile("L001","Student.txt");
//        Student A = new Student(studentContent[0].split("\\|"));
//        new StudentProfile(A);
//        ArrayList<Coach> coachList = new ArrayList<>();
//        for (String coachDetails:FileServer.readFile("L002","Coach.txt")){
//            coachList.add(new Coach(coachDetails.split("\\|")));
//        }
//
//
//        Collections.sort(coachList,new Coach.sortByRating());

////        System.out.println(coachList);

//        System.out.println(coachList);

//        new LoginScreen();
//          Admin admin = Admin.adminLogin("Kei Zhong","345");
//          Schedule monday = new Schedule(admin.getSportsCenterCode(),"T01|T02|T03".split("\\|"));
//        System.out.println(monday);
//            String studentDetails[] = {"Enya","S001","19","Sri Petaling","011123","enya@gmail.com","Badminton","L001"};
//            String coachDetails[] = {"Kei Zhong","C001","2018-09-12","null","20","012-9994563","Bandar Utama","L001","B001","15","2"};
//            Student student = new Student(studentDetails);
//            Coach coach = new Coach(coachDetails);
//            student.giveRating(10,coach);




//            Admin admin = new Admin("Jack","L001");
//            new AdminMenu(admin);
//        new LoginScreen();

//        for (String line : FileServer.readFile("L002","Sports.txt")){
//            Sports sports = new Sports("L002",line.split("\\|"));
//            System.out.println(sports.toString());
//        }


//        Schedule.updateScheduleFile("L002");


//
//            Admin admin = new Admin("Jack","L001");
//            new AdminMenu(admin);
//        Schedule.updateScheduleFile("L001");
//
//
//        System.out.println(Arrays.toString(FileServer.readFile("L002","Session.txt")));
//        System.out.println(FileServer.readFile("L002","Session.txt").length);
//        Schedule.updateScheduleFile("L002");

        new LoginScreen();
    }
}
