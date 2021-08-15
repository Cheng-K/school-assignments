package com.company;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/*----------Class Constructor----------*/

public class Admin {
    private final String ID;
    private final String SportsCenterCode;

    public Admin(String ID, String sportsCenterCode) {
        this.ID = ID;
        SportsCenterCode = sportsCenterCode;
    }

    /*
    Method      : adminLogin
    Description : Verify the details of admin, if correct, return an instance of admin, otherwise null
    Parameter   : username (String) / password (String)
    Return      : Admin instance / Null
 */
    public static Admin adminLogin(String username, String password) {
        String[] adminFileContent = FileServer.readFile("", "Admin.txt");
        for (String line : adminFileContent) {
            String[] tokens = line.split("\\|");
            if (tokens[0].equals(username) && tokens[1].equals(password)) {
                return new Admin(tokens[2], tokens[3]);
            }
        }
        return null;
    }


    /*----------Create records methods----------*/

    /* Method : createAccount
       Description : Takes in students' profile details and passwords as parameters to create an account
    */

    public int createAccount(String[] studentDetail, String password) //ID passed in will be null, and sports centre same with admins'
    {
        RegisteredStudent newStudent = new RegisteredStudent(studentDetail, RegisteredStudent.findMyCoach(studentDetail[8],studentDetail[7]));
        String fileCheck[] = FileServer.readFile("UnregStudent.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
                return 1; //When profile with same name and sports centre already created and waiting for admins' approval
            }
        }
        fileCheck = FileServer.readFile(getSportsCenterCode(), "Student.txt");
        for (String line : fileCheck) {
            String[] token = line.split("\\|");
            if (token[0].equals(newStudent.getName())) {
                return 2; //Profile with same name already exists in the sports center database
            }
        }
        newStudent.setStudentID(newStudentID());
        FileServer.appendFile("Student.txt", newStudent.getName() + "|" + password + "|" + getSportsCenterCode() + "\n");
        FileServer.appendFile(getSportsCenterCode(), "Student.txt", newStudent.toString()+"\n");
        return 0;
    }


    /* Method : newStudentID
       Description : Generates new student ID when creating account for students
    */

    public String newStudentID() {
        int max = 0;
        String newID="";
        String[] fileContent = FileServer.readFile(getSportsCenterCode(), "Student.txt");
        for (String line : fileContent) {
            String[] token = line.split("\\|");
            if (Integer.parseInt(token[1].replace("S", "")) > max) {
                max = Integer.parseInt(token[1].replace("S", ""));
            }
        }
        max++;
        if (max<10) {
            newID = "S00"+max;
        }
        else if (max<100) {
            newID = "S0"+max;
        }
        else {
            newID = "S"+max;
        }
        return newID;
    }

    /* Method : approveAccount
       Description : Approve students' account creation request made by guests logins using names entered as the parameter
    */

    public void approveAccount(String name)
    {
        String[] unregStudent = FileServer.readFile("UnregStudent.txt");
        for(String line:unregStudent)
        {
            String[] token = line.split("\\|");
            if (name.equals(token[0]))
            {
                RegisteredStudent student = new RegisteredStudent(token, RegisteredStudent.findMyCoach(token[8],token[7]));
                student.setStudentID(newStudentID());
                FileServer.appendFile(getSportsCenterCode(),"Student.txt",student.toString()+"\n");
                FileServer.appendFile("Student.txt",student.getName()+"|"+token[10]+"|"+getSportsCenterCode()+"\n");
                String[] fileContent = FileServer.readFile("UnregStudent.txt");
                FileServer.writeFile("UnregStudent.txt","");
                for (String newlines:fileContent)
                {
                    if(!(newlines.equals(line)))
                    {
                        FileServer.appendFile("UnregStudent.txt",newlines+"\n");
                    }
                }
            }
        }

    }

    /* Method : rejectAccount
       Description : Reject students' account creation request made by guests logins using names entered as the parameter
    */

    public void rejectAccount(String name)
    {
        String[] fileContent = FileServer.readFile("UnregStudent.txt");
        FileServer.writeFile("UnregStudent.txt","");
        for (String line:fileContent)
        {
            if(!(line.contains(name))){
                FileServer.appendFile("UnregStudent.txt",line+"\n");
            }
        }
    }

    /* Method : createSports
       Description : Creates new sport using the sports name and fees entered as the parameter
    */

    public int createSports(String sportsName,String fees)
    {
        String[] fileContent = FileServer.readFile(getSportsCenterCode(),"Sports.txt");
        String sportsID="";
        int max = 0;
        for (String lines:fileContent)
        {
            String[] token = lines.split("\\|");
            if (token[0].equals(sportsName)) {
                return 1; //Sports name already exists
            }
            if (Integer.parseInt(token[1].replace("B",""))>max) {
                max = Integer.parseInt(token[1].replace("B",""));
            }
        }
        max++;
        if (max<10) {
            sportsID = "B00"+max;
        }
        else if (max<100) {
            sportsID = "B0"+max;
        }
        else {
            sportsID = "B"+max;
        }
        Sports sports = new Sports(getSportsCenterCode(),new String[]{sportsName,sportsID,fees});
        FileServer.appendFile(getSportsCenterCode(),"Sports.txt", sports +"\n");
        return 0;
    }

    /* Method : createCoach
       Description : Takes in coach's profile details as parameters to create a new coach profile
    */

    public int createCoach(String[] coachDetails) //coachID and date terminated will be passed as "null"
    {
        Coach newCoach = new Coach(coachDetails);
        String[] fileContent = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        for (String line:fileContent)
        {
            String[] token = line.split("\\|");
            if (token[0].equals(newCoach.getName()))
            {
                return 1; //name already exists
            }
        }
        newCoach.setCoachID(newCoachID());
        FileServer.appendFile(getSportsCenterCode(),"Coach.txt",newCoach.toString()+"\n");
        return 0;
    }

    /* Method : newCoachID
       Description : Generates new coach ID when creating new coach profiles
    */

    public String newCoachID()
    {
        int max = 0;
        String coachID="";
        String[] fileContent = FileServer.readFile(getSportsCenterCode(),"Coach.txt");
        for (String line:fileContent)
        {
            String[] token = line.split("\\|");
            if (Integer.parseInt(token[1].replace("C",""))>max)
            {
                max = Integer.parseInt(token[1].replace("C",""));
            }
        }
        max++;
        if (max<10) {
            coachID = "C00"+max;
        }
        else if (max<100) {
            coachID = "C0"+max;
        }
        else {
            coachID = "C"+max;
        }
        return coachID;
    }

    /* Method : createSession
       Description : Takes in session details as parameters to create a new sport session
    */

    public void createSession(String[] sessionDetail,String day, String sportCode) {
        int max = 0;
        String[] sessionFileContent = FileServer.readFile(getSportsCenterCode(), "Session.txt");
        for (String line : sessionFileContent) {
            String[] token = line.split("\\|");
            if (Integer.parseInt(token[1].replace("T", "")) > max) {
                max = Integer.parseInt(token[1].replace("T", ""));
            }
        }
        if (max<9){
            sessionDetail[1] = "T0" + (max+1);
        }else{
            sessionDetail[1] = "T" + (max+1);
        }
        Session newSession = new Session(sessionDetail);
        FileServer.appendFile(getSportsCenterCode(),"Session.txt",newSession.getWriteToFileString()+"\n");
        Schedule.updateScheduleFile(getSportsCenterCode());
    }


    /*  Method name : sort (Generic Method)
        Parameter   : tArrayList (Array list containing T typed *preferably instances of Coach/Students*) ,
                      sorter (Comparator instance tells how to sort tArrayList ),
                      ascending (True for ascending, False for descending)
     */
    public <T> void sort (ArrayList<T> tArrayList, Comparator<T> sorter, boolean ascending){
        Collections.sort(tArrayList,sorter);
        if (!ascending) // reverse the sorted list if its descending
            Collections.reverse(tArrayList);
    }

    /*---------Getters and Setters---------*/

    public String getID() {
        return ID;
    }

    public String getSportsCenterCode() {
        return SportsCenterCode;
    }


    /*---------Modify records methods---------*/

    /*
    Method      : modCoach
    Description : Modify the coach details
    Parameter   : newDetails (List of strings) , Coach object with details to be modified
    Return      : 0 -- modify success
                  1 -- modify failed
 */
    public int modCoach(List<String>newDetails,Coach coach){
        String oldString = coach.toString();
        coach.setDateJoined(LocalDate.parse(newDetails.get(2)));
        try {
            coach.setDateTerminated(LocalDate.parse(newDetails.get(3)));
        }
        catch (Exception e){
            coach.setDateTerminated(null);
        }
        coach.setHourlyRate(Integer.parseInt(newDetails.get(4)));
        coach.setPhone(newDetails.get(5));
        coach.setAddress(newDetails.get(6));
        String newString = coach.toString();
        String[] coachFileContent = FileServer.readFile(this.SportsCenterCode,"Coach.txt");
        FileServer.findAndReplace(coachFileContent,oldString,newString);
        return FileServer.writeFile(this.SportsCenterCode,"Coach.txt",String.join("\n",coachFileContent)+"\n");


    }

    /*
    Method      : modSports
    Description : Modify a sport details
    Parameter   : newFees (updated sport fees) , sports object with fees to be modified
    Return      : 0 -- modify success
                  1 -- modify failed
   */
    public int modSports (Integer newFees, Sports sports){
        String oldString = sports.toString();
        sports.setSportFees(newFees);
        String[] currentFileContent = FileServer.readFile(SportsCenterCode,"Sports.txt");
        FileServer.findAndReplace(currentFileContent,oldString,sports.toString());
        return FileServer.writeFile(SportsCenterCode,"Sports.txt",String.join("\n",currentFileContent)+"\n");
    }

    /*
        Method      : modSession
        Description : Modify the session details
        Parameter   : newDetails (List of strings) , Session object with details to be modified
        Return      : 0 -- modify success
                      1 -- modify failed
*/
    public int modSession (List<String>newDetails,Session session){
        String oldString = session.getWriteToFileString();
        // modifying attributes
        String newDay = newDetails.get(0);
        String[] newStartTime = newDetails.get(2).split(":");
        String[] newEndTime = newDetails.get(3).split(":");
        session.setDay(newDay.substring(0,1).toUpperCase() + newDay.substring(1));
        session.setStartTime(LocalTime.of(Integer.parseInt(newStartTime[0]),Integer.parseInt(newStartTime[1])));
        session.setEndTime(LocalTime.of(Integer.parseInt(newEndTime[0]),Integer.parseInt(newEndTime[1])));
        session.setDuration(Duration.between(session.getStartTime(),session.getEndTime()));
        // get new string representation
        String newString = session.getWriteToFileString();
        String[] sessionFileContent = FileServer.readFile(this.SportsCenterCode,"Session.txt");
        FileServer.findAndReplace(sessionFileContent,oldString,newString);
        if (FileServer.writeFile(this.SportsCenterCode,"Session.txt",String.join("\n",sessionFileContent)+"\n") == 1)
            return 1;
        else
            return Schedule.updateScheduleFile(this.SportsCenterCode);
    }

    /*---------Search records methods---------*/

    /*
    Method      : searchCoach
    Description : used to search for coach in a list of coaches
    Parameter   : coachList (List of coaches) , ID (Coach ID to be searched)
    Return      : An array list of coaches (contains the coach with similar ID)
    */

    public  ArrayList<Coach> searchCoach(List<Coach>coachList,String ID){
        ArrayList<Coach>found = new ArrayList<>();
        for (Coach coach:coachList){
            if (ID.equalsIgnoreCase(coach.getCoachID()))
                found.add(coach);
        }
        return found;
    }
    /*
    Method      : searchCoach (Overloaded)
    Description : used to search for coach in a list of coaches
    Parameter   : coachList (List of coaches) , rating (coach rating to be searched)
    Return      : An array list of coaches (contains the coach with similar rating)
*/
    public ArrayList<Coach> searchCoach(List<Coach>coachList,int rating){
        ArrayList<Coach>found = new ArrayList<>();
        for (Coach coach:coachList){
            try {
                if (rating == (coach.getRating() / coach.getTotalRates()))
                    found.add(coach);
            }catch (ArithmeticException e){
                if (rating == 0)
                    found.add(coach);
            }
        }
        return found;
    }

    /*
        Method      : searchCoach (Overloaded)
        Description : used to search for coach in a list of coaches
        Parameter   : coachList (List of coaches) , ID (Coach ID to be searched), rating (coach rating to be searched)
        Return      : An array list of coaches (contains the coach with similar rating & similar ID)
*/
    public ArrayList<Coach> searchCoach(List<Coach>coachList,String ID, int rating){
        return searchCoach(searchCoach(coachList,ID),rating);
    }

    /*
    Method      : searchSports
    Description : used to search for sports in a list of sports
    Parameter   : sportsList (List of sports) , ID (Sport ID to be searched)
    Return      : An array list of sports (contains the sports with similar ID)
*/
    public ArrayList<Sports> searchSports(List <Sports> sportsList, String ID){
        ArrayList<Sports>found = new ArrayList<>();
        for (Sports sports : sportsList){
            if (ID.equalsIgnoreCase(sports.getSportsID()))
                found.add(sports);
        }
        return found;
    }

    /*
    Method      : searchStudent
    Description : used to search for student in a list of coaches
    Parameter   : studentList (List of students) , ID (Student ID to be searched)
    Return      : An array list of students (contains the students with similar ID)
*/
    public ArrayList<RegisteredStudent> searchStudent(List<RegisteredStudent>studentList, String ID){
        ArrayList<RegisteredStudent>found = new ArrayList<>();
        for (RegisteredStudent student : studentList){
            if (ID.equalsIgnoreCase(student.getStudentID()))
                found.add(student);
        }
        return found;
    }

    /*---------Delete records methods---------*/

    /*
        Method      : deleteCoachRecord
        Description : Used to delete a coach in the system
        Parameter   : coachList (List of coaches) , coachToRemove (Coach object to be removed)
        Return      : 0 -- remove successful
                      1 -- remove unsuccessful
    */

    public int deleteCoachRecord (List<Coach> coachList, Coach coachToRemove){
        int foundCoach = 0;
        for (;foundCoach < coachList.size();foundCoach++){
            if (coachList.get(foundCoach).getName().equals(coachToRemove.getName())){
                coachList.remove(foundCoach);
                if (FileServer.writeFile(this.SportsCenterCode,"Coach.txt","") == 1)
                    return 1;
                for (Coach coach : coachList){
                    if (FileServer.appendFile(this.SportsCenterCode,"Coach.txt",coach.toString()+"\n") == 1)
                        return 1;
                }
                return 0;
            }
        }
        return 1;
    }

    /*
        Method      : deleteSportsRecord
        Description : Used to delete a sport in the system
        Parameter   : sportsList (List of coaches) , sportsToRemove (sports object to be removed)
        Return      : 0 -- remove successful
                      1 -- remove unsuccessful
    */
    public int deleteSportsRecord (List<Sports> sportsList, Sports sportsToRemove){
        int foundSports = 0;
        for (;foundSports < sportsList.size();foundSports++){
            if (sportsList.get(foundSports).getName().equals(sportsToRemove.getName())){
                sportsList.remove(foundSports);
                if (FileServer.writeFile(this.SportsCenterCode,"Sports.txt","") == 1)
                    return 1;
                for (Sports sports : sportsList){
                    if (FileServer.appendFile(this.SportsCenterCode,"Sports.txt",sports.toString()+"\n") == 1)
                        return 1;
                }
                return 0;
            }
        }
        return 1;
    }


    /*
        Method      : deleteStudentRecord
        Description : Used to delete a student in the system
        Parameter   : studentList (List of students) , studentToRemove (Student object to be removed)
        Return      : 0 -- remove successful
                      1 -- remove unsuccessful
    */
    public int deleteStudentRecord (List<RegisteredStudent> studentList, RegisteredStudent studentToRemove){
        // remove student at credentials file
        String nameToBeRemoved = studentToRemove.getName();
        String[] credentialsFile = FileServer.readFile("Student.txt");
        List<String> credentials = new ArrayList<>(Arrays.asList(credentialsFile));
        int rowToBeRemove = 0;
        for (int row = 0; row < credentials.size();row++){
            String[] credentialsTokens = credentials.get(row).split("\\|");
            if (credentialsTokens[0].equals(nameToBeRemoved)){
                rowToBeRemove = row;
                break;
            }
        }
        credentials.remove(rowToBeRemove);
        if (FileServer.writeFile("Student.txt","") == 1) {
            return 1;
        }
        for (String line : credentials){
            if (FileServer.appendFile("Student.txt",line+"\n") == 1)
                return 1;
        }

        // remove student at student.txt in subfolder
        int foundStudent = 0;
        for (;foundStudent < studentList.size();foundStudent++){
            if (studentList.get(foundStudent).getName().equals(studentToRemove.getName())){
                studentList.remove(foundStudent);
                if (FileServer.writeFile(this.SportsCenterCode,"Student.txt","") == 1)
                    return 1;
                for (RegisteredStudent student : studentList){
                    if (FileServer.appendFile(this.SportsCenterCode,"Student.txt",student.toString()+"\n") == 1)
                        return 1;
                }
                return 0;
            }
        }
        return 1;
    }

    /*
        Method      : deleteSessionRecord
        Description : Used to delete a session in the system
        Parameter   : sessionToBeRemove (session object to be removed)
        Return      : 0 -- remove successful
                      1 -- remove unsuccessful
    */

    public int deleteSessionRecord (Session sessionToBeRemove){
        // initialize all sessions
        List<Session> allSession = new ArrayList<>();
        String[] sessionFileContent = FileServer.readFile(this.SportsCenterCode,"Session.txt");
        for (String line : sessionFileContent){
            allSession.add(new Session(line.split("\\|")));
        }
        allSession.remove(sessionToBeRemove);
        if (FileServer.writeFile(this.SportsCenterCode,"Session.txt","") == 1)
            return 1;
        for (Session session : allSession){
            if (FileServer.appendFile(this.SportsCenterCode,"Session.txt",session.getWriteToFileString()+"\n") == 1)
                return 1;
        }
        // update schedule.txt
        return Schedule.updateScheduleFile(this.SportsCenterCode);
    }

}
