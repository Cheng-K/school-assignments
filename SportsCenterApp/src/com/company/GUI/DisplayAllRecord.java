package com.company.GUI;

import com.company.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.*;

public class DisplayAllRecord {
    public JFrame frame;
    private JTabbedPane tabbedPane1;
    private JPanel rootPanel;
    private JPanel studentRecordPanel;
    private JPanel coachRecordPanel;
    private JPanel sportsRecordPanel;
    private JScrollPane studentTableContainer;
    private JScrollPane coachTableContainer;
    private JScrollPane sportsTableContainer;
    private JTable studentRecordTable;
    private JTable coachRecordTable;
    private JTable sportsRecordTable;
    private JComboBox<Comparator <Coach>> sortByCoachMenu;
    private JComboBox<Comparator <Student>> sortByStudentMenu;
    private JComboBox<Comparator <Sports>> sortBySportsMenu;
    private JLabel orderLabel;
    private ButtonGroup radioButtonGroup = new ButtonGroup();
    private JRadioButton ascendingRadioButton;
    private JRadioButton descendingRadioButton;
    private JButton sortTableButton;
    private JButton backToMenuButton;
    private JButton modifyDetailsButton;
    private JPanel scheduleRecordPanel;
    private JScrollPane scheduleTableContainer;
    private JTable scheduleRecordTable;
    private JComboBox<Schedule> scheduleSelector;
    private JComboBox<Comparator<Session>> sortByScheduleMenu;
    private JButton refreshButton;
    private JButton deleteRecordButton;
    private DefaultTableModel coachTableModel;
    private DefaultTableModel studentTableModel = (DefaultTableModel) studentRecordTable.getModel();
    private DefaultTableModel sportsTableModel;
    private DefaultTableModel scheduleTableModel;
    private Admin admin;
    public SetCoachPanel coachPanelManager;
    public SetStudentPanel studentPanelManager;
    public SetSportsPanel sportsPanelManager;
    public final SetSchedulePanel schedulePanelManager;



    /*  Class : SetCoachPanel
        Description : Responsible for setting up/ changing components in Coach Tab (Coach Tab Manager)
     */
    public class SetCoachPanel {

        private ArrayList<Coach> coachList = new ArrayList<>();
        private ArrayList<Coach> currentDisplayList = new ArrayList<>();

        public SetCoachPanel(){
            getAllCoach();
            prepareCoachTable();
            updateCoachTable();
            setSortDropMenu();
        }

        public void clearUpdateTable() {
            clearCoachTable();
            coachList.clear();
            getAllCoach();
            updateCoachTable();
        }

        /*  Method Name : getAllCoach
            Description : Read coach file and instantiate all coach objects & store it in array list
         */

        private void getAllCoach () {
            String[] coachFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Coach.txt");
            for (String coachInfo: coachFileContent){
                Coach coach = new Coach(coachInfo.split("\\|"));
                coachList.add(coach);
            }
            currentDisplayList = (ArrayList<Coach>) coachList.clone();
        }

        /*  Method Name : prepareCoachTable
            Description : Set the number of columns in JTable
         */
        private void prepareCoachTable (){
            coachRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            coachTableModel = new DefaultTableModel(Coach.getAllAttributes(),0){
                @Override
                public boolean isCellEditable (int row, int column){ return false; }
            };
            coachRecordTable.setModel(coachTableModel);
            coachRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        /*  Method Name : updateCoachTable
            Description : Add rows in JTable based on the order of arrayList containing coaches
                          (pair with clearCoachTable Method to clear & update table order)
         */
        private void updateCoachTable() {
            for (Coach coach : currentDisplayList){
                coachTableModel.addRow(coach.getDisplayString().split("\\|"));
            }
        }

        public void showFoundCoaches(ArrayList<Coach>searchResults){
            clearCoachTable();
            currentDisplayList = searchResults;
            updateCoachTable();
            tabbedPane1.setSelectedIndex(1);
        }

        /*  Method Name : prepareCoachTable
            Description : Set the number of columns in JTable
         */
        private void setSortDropMenu (){
            sortByCoachMenu.addItem(new Coach.sortByID());
            sortByCoachMenu.addItem(new Coach.sortByRating());
            sortByCoachMenu.addItem(new Coach.sortByPay());
        }

        /*  Method Name : prepareCoachTable
            Description : Clear all the rows in JTable
         */
        private void clearCoachTable() {coachTableModel.setRowCount(0);}
        public ArrayList<Coach> getCoachList() {return coachList;}

    }

    /*  Class : SetStudentPanel
        Description : Responsible for setting up/ changing components in Coach Tab (Student Tab Manager)
     */

    public class SetStudentPanel {
        private ArrayList<Student> studentList = new ArrayList<>();
        private ArrayList<Student> currentDisplayList = new ArrayList<>();

        public SetStudentPanel() {
            getAllStudent();
            prepareStudentTable();
            updateStudentTable();
            setDropMenu();
        }

        private void getAllStudent () {
            String[] studentFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Student.txt");
            for (String studentInfo : studentFileContent){
                String[] tokens = studentInfo.split("\\|");
                Student student = new Student(tokens,Student.findMyCoach(tokens[8],tokens[7]));
                studentList.add(student);
            }
            currentDisplayList = (ArrayList<Student>) studentList.clone();
        }


        private void prepareStudentTable() {
            studentRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            studentTableModel = new DefaultTableModel(Student.getAllAttributes(),0){
                @Override
                public boolean isCellEditable (int row, int column){ return false;}
            };
            studentRecordTable.setModel(studentTableModel);
            studentRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        private void updateStudentTable() {
            for (Student student:currentDisplayList)
                studentTableModel.addRow(student.toString().split("\\|"));
        }

        public void showFoundStudents(ArrayList<Student>searchResults){
            clearStudentTable();
            currentDisplayList = searchResults;
            updateStudentTable();
            tabbedPane1.setSelectedIndex(0);
        }

        private void clearStudentTable() {studentTableModel.setRowCount(0);}

        private void setDropMenu () {sortByStudentMenu.addItem(new Student.sortByName());}

        public void refreshStudentList() {
            studentList.clear();
            getAllStudent();
            clearStudentTable();
            updateStudentTable();
        }
        public ArrayList<Student> getStudentList() {return studentList;}
    }

    public class SetSportsPanel {
        public ArrayList<Sports> currentDisplayList = new ArrayList<>();
        private ArrayList<Sports> sportsArrayList = new ArrayList<>();
        private SetSportsPanel(){
            getAllSports();
            prepareSportsTable();
            updateSportsTable();
            setSortDropMenu();
        }
        private void getAllSports() {
            String[] sportsFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Sports.txt");
            for (String sportsInfo : sportsFileContent){
                Sports sports = new Sports(admin.getSportsCenterCode(),sportsInfo.split("\\|"));
                sportsArrayList.add(sports);
            }
            currentDisplayList = (ArrayList<Sports>) sportsArrayList.clone();
        }
        private void prepareSportsTable() {
                sportsRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                sportsTableModel = new DefaultTableModel(Sports.getAllAttributes(),0){
                    @Override
                    public boolean isCellEditable (int row, int column ){return false;}
                };
                sportsRecordTable.setModel(sportsTableModel);
                sportsRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        }

        private void updateSportsTable() {
            for (Sports sport : currentDisplayList){
                sportsTableModel.addRow(sport.toString().split("\\|"));
            }
        }
        public void clearUpdateTable() {
            clearSportsTable();
            sportsArrayList.clear();
            getAllSports();
            updateSportsTable();
        }
        public void showFoundSports (ArrayList<Sports> results){
            clearSportsTable();
            currentDisplayList = results;
            updateSportsTable();
            tabbedPane1.setSelectedIndex(2);
        }
        private void setSortDropMenu() {
            sortBySportsMenu.addItem(new Sports.sortByName());
            sortBySportsMenu.addItem(new Sports.sortByFees());
        }
        private void clearSportsTable() {sportsTableModel.setRowCount(0);}

        public ArrayList<Sports> getSportsArrayList() {
            return sportsArrayList;
        }

    }

    public class SetSchedulePanel {
        private ArrayList<Schedule> allScheduleList = new ArrayList<>();

        public SetSchedulePanel() {
            getAllSchedule();
            prepareScheduleTable();
            updateScheduleTable();
            prepareScheduleSelector();
            setSortDropMenu();
        }

        public SetSchedulePanel(Sports sportsToDisplay){
            allScheduleList.add(sportsToDisplay.getSchedule());
            prepareScheduleTable();
            updateScheduleTable();
            prepareScheduleSelector();
            setSortDropMenu();
        }

        private void getAllSchedule() {
            String[] scheduleFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Schedule.txt");
            for (int line = 0; line<scheduleFileContent.length ; line++){
                String[] tokens = scheduleFileContent[line].split("\\|");
                allScheduleList.add(new Schedule(admin.getSportsCenterCode(),tokens[0],Arrays.copyOfRange(tokens,1,tokens.length)));
            }
        }

        private void prepareScheduleTable() {
            scheduleRecordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scheduleTableModel = new DefaultTableModel(Schedule.getAllAttributes(),0){
               @Override
               public boolean isCellEditable(int row, int column) {return false;}
            };
            scheduleRecordTable.setModel(scheduleTableModel);
            scheduleRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        private void updateScheduleTable(Schedule schedule) {
            for (Session session : schedule.getAllSession())
                scheduleTableModel.addRow(session.toString().split("\\|"));
        }

        private void updateScheduleTable() {
            updateScheduleTable(allScheduleList.get(0));
        }

        public void clearUpdateTable(){
            int previouslySelectedIndex = scheduleSelector.getSelectedIndex();
            clearScheduleTable();
            allScheduleList.clear();
            getAllSchedule();
            scheduleSelector.removeItemListener(scheduleSelector.getItemListeners()[0]);
            scheduleSelector.removeAllItems();
            prepareScheduleSelector();
            updateScheduleTable();
            try {
                scheduleSelector.setSelectedIndex(previouslySelectedIndex);
            } catch (Exception e){
                // do nothing prevent error
            }
        }

        private void clearScheduleTable() {scheduleTableModel.setRowCount(0);}

        private void prepareScheduleSelector() {
            for (Schedule schedule : allScheduleList)
                scheduleSelector.addItem(schedule);

            scheduleSelector.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    clearScheduleTable();
                    updateScheduleTable((Schedule)scheduleSelector.getSelectedItem());
                }
            });
        }

        private void setSortDropMenu (){
            sortByScheduleMenu.addItem(new Session.sortByDay());
            sortByScheduleMenu.addItem(new Session.sortByName());
        }

    }

    /*  Class : sortTableButtonListener
        Description : Calls method to sort the list of objects (student,coach) in specified order
     */

    private class sortTableButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTab = tabbedPane1.getSelectedIndex();
            switch (selectedTab){
                case 0 :
                    studentPanelManager.clearStudentTable();
                    admin.sort(studentPanelManager.currentDisplayList,(Comparator<Student>)sortByStudentMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    studentPanelManager.updateStudentTable(); // display the table in sorted order
                   break;
                case 1 :
                    coachPanelManager.clearCoachTable();
                    admin.sort(coachPanelManager.currentDisplayList,(Comparator<Coach>) sortByCoachMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    coachPanelManager.updateCoachTable();
                    break;
                case 2:
                    sportsPanelManager.clearSportsTable();
                    admin.sort(sportsPanelManager.currentDisplayList,(Comparator<Sports>)sortBySportsMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    sportsPanelManager.updateSportsTable();
                    break;
                case 3:
                    schedulePanelManager.clearScheduleTable();
                    admin.sort(((Schedule)scheduleSelector.getSelectedItem()).getAllSession(),(Comparator<Session>)sortByScheduleMenu.getSelectedItem(),ascendingRadioButton.isSelected());
                    schedulePanelManager.updateScheduleTable((Schedule) scheduleSelector.getSelectedItem());
                    break;
                default :
                    break;
            }
        }
    }

    private class modifyDetailsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // determine which tab is selected
            int tabNumber = tabbedPane1.getSelectedIndex();
            // check whether it is -1
            switch (tabNumber){
                case 0 :
                    JOptionPane.showMessageDialog(frame , "Sorry, admin cannot edit student personal information");
                    break;
                case 1: {
                    int row = coachRecordTable.getSelectedRow();
                    Coach targetCoach;
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to modify");
                    else {
                        targetCoach = coachPanelManager.currentDisplayList.get(row);
                        frame.setVisible(false);
                        new AdminModifyMenu(targetCoach, admin, DisplayAllRecord.this);
                    }
                    break;
                }
                case 2: {
                    int row = sportsRecordTable.getSelectedRow();
                    Sports targetSports;
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to modify");
                    else {
                        targetSports = sportsPanelManager.currentDisplayList.get(row);
                        frame.setVisible(false);
                        new AdminModifyMenu(targetSports, admin, DisplayAllRecord.this);
                    }
                    break;
                }
                case 3: {
                    int row = scheduleRecordTable.getSelectedRow();
                    Session targetSession;
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to modify");
                    else {
                        targetSession = ((Schedule)scheduleSelector.getSelectedItem()).getSession(row);
                        frame.setVisible(false);
                        new AdminModifyMenu(targetSession,admin,DisplayAllRecord.this);
                    }
                    break;
                }
                default :
                    break;
            }
        }
    }

    private class RefreshButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (tabbedPane1.getSelectedIndex()){
                case 0 :
                    studentPanelManager.refreshStudentList();
                    break;
                case 1:
                    coachPanelManager.clearUpdateTable();
                    break;
                case 2:
                    sportsPanelManager.clearUpdateTable();
                    break;
                case 3 :
                    schedulePanelManager.clearUpdateTable();
                    break;
            }
        }
    }

    private class deleteRecordButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(tabbedPane1.getSelectedIndex()){
                case 0 : {
                    int row = studentRecordTable.getSelectedRow();
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to delete");
                    else {
                        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record? This operation cannot be undone later.",
                                "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirmation == 0) {
                            if (admin.deleteStudentRecord(studentPanelManager.studentList, studentPanelManager.currentDisplayList.get(row)) == 1)
                                JOptionPane.showMessageDialog(frame, "Cannot perform delete operation. File cannot be accessed. Check with technical assistance.");
                            else
                                studentPanelManager.refreshStudentList();
                        }
                    }
                    break;
                }
                case 1: {
                    int row = coachRecordTable.getSelectedRow();
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to delete");
                    else {
                        List<String> occupiedCoaches = getAllOccupiedCoach();
                        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record. This operation cannot be undone later.",
                                "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirmation == 0) {
                            if (coachIsOccupied(occupiedCoaches,coachPanelManager.currentDisplayList.get(row).getName()))
                                JOptionPane.showMessageDialog(frame,"Cannot delete coach. There are students under this coach.","Error",
                                        JOptionPane.ERROR_MESSAGE);
                            else {
                                if ((admin.deleteCoachRecord(coachPanelManager.coachList, coachPanelManager.currentDisplayList.get(row)))==1)
                                    JOptionPane.showMessageDialog(frame, "Cannot perform delete operation. File cannot be accessed. Check with technical assistance.");
                                else
                                    coachPanelManager.clearUpdateTable();
                            }
                        }
                    }
                    break;
                }
                case 2: {
                    int row = sportsRecordTable.getSelectedRow();
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to delete");
                    else {
                        List<String> occupiedSports = getAllOccupiedSports();
                        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record. This operation cannot be undone later.",
                                "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirmation == 0) {
                            if (sportsIsOccupied(occupiedSports,sportsPanelManager.currentDisplayList.get(row).getName())) {
                                JOptionPane.showMessageDialog(frame,"Cannot delete sports. There are students & coaches under this sport.","Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            else {
                                if (admin.deleteSportsRecord(sportsPanelManager.sportsArrayList,sportsPanelManager.currentDisplayList.get(row)) == 1)
                                    JOptionPane.showMessageDialog(frame, "Cannot perform delete operation. File cannot be accessed. Check with technical assistance.");
                                else
                                    sportsPanelManager.clearUpdateTable();
                            }

                        }
                    }
                    break;
                }
                case 3: {
                    int row = scheduleRecordTable.getSelectedRow();
                    if (row == -1)
                        JOptionPane.showMessageDialog(frame, "Please select a row to delete");
                    else {
                        int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record. This operation cannot be undone later.",
                                "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirmation == 0) {
                            if (admin.deleteSessionRecord(((Schedule)(Objects.requireNonNull(scheduleSelector.getSelectedItem()))).getSession(row)) == 1)
                                JOptionPane.showMessageDialog(frame, "Cannot perform delete operation. File cannot be accessed. Check with technical assistance.");
                            else
                                schedulePanelManager.clearUpdateTable();
                        }
                    }
                    break;
                }
            }
        }
        public List<String> getAllOccupiedCoach() {
            List<String> occupiedCoaches = new ArrayList<>();
            String[] studentFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Student.txt");
            for (String line : studentFileContent){
                String[] tokens = line.split("\\|");
                occupiedCoaches.add(tokens[8]);
            }
            return occupiedCoaches;
        }
        public boolean coachIsOccupied (List<String>occupiedCoaches,String coachName){
            return occupiedCoaches.contains(coachName);
        }

        public List<String> getAllOccupiedSports() {
            List<String> occupiedSports = new ArrayList<>();
            String[] studentFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Student.txt");
            String[] coachFileContent = FileServer.readFile(admin.getSportsCenterCode(),"Coach.txt");
            for (String line : studentFileContent){
                String[] tokens = line.split("\\|");
                occupiedSports.add(tokens[6]);
            }
            for (String line : coachFileContent){
                String[] tokens = line.split("\\|");
                occupiedSports.add(tokens[11]);
            }
            return occupiedSports;
        }
        public boolean sportsIsOccupied (List<String>occupiedSports,String sportsName){
            return occupiedSports.contains(sportsName);
        }
    }



    public DisplayAllRecord (Admin admin,boolean visible) {
        this.admin = admin;
        coachPanelManager = new SetCoachPanel();
        studentPanelManager = new SetStudentPanel();
        sportsPanelManager = new SetSportsPanel();
        schedulePanelManager = new SetSchedulePanel();
        radioButtonGroup.add(ascendingRadioButton);
        radioButtonGroup.add(descendingRadioButton);
        ascendingRadioButton.setSelected(true);
        sortTableButton.addActionListener(new sortTableButtonListener());
        modifyDetailsButton.addActionListener(new modifyDetailsListener());
        refreshButton.addActionListener(new RefreshButtonListener());
        deleteRecordButton.addActionListener(new deleteRecordButtonListener());
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new AdminMenu(admin);
            }
        });
        frame = new JFrame("Showing All Records");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(visible);

    }

    public DisplayAllRecord (Admin admin){
        this(admin,true);
    }

    public DisplayAllRecord (BaseStudent student, Sports sports){
        schedulePanelManager = new SetSchedulePanel(sports);
        deleteRecordButton.setVisible(false);
        modifyDetailsButton.setVisible(false);
        refreshButton.setVisible(false);
        radioButtonGroup.add(ascendingRadioButton);
        radioButtonGroup.add(descendingRadioButton);
        ascendingRadioButton.setSelected(true);
        sortTableButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Comparator<Session> sorter;
                if (!ascendingRadioButton.isSelected())
                     sorter = Collections.reverseOrder((Comparator<Session>)sortByScheduleMenu.getSelectedItem());
                else
                    sorter = (Comparator<Session>)sortByScheduleMenu.getSelectedItem();
                Collections.sort(schedulePanelManager.allScheduleList.get(0).getAllSession(),sorter);
                schedulePanelManager.clearScheduleTable();
                schedulePanelManager.updateScheduleTable();

            }
        });
        // back to menu button
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentMenu(student);
                frame.setVisible(false);
            }
        });
        tabbedPane1.setSelectedIndex(3);
        tabbedPane1.setEnabledAt(0,false);
        tabbedPane1.setEnabledAt(1,false);
        tabbedPane1.setEnabledAt(2,false);
        frame = new JFrame ("Showing Schedule");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
