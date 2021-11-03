#pragma once

class PatientQueue;
class LinkedList;
class Patient;

class Doctor
{
    PatientQueue* patientQueue;
    LinkedList* historyList;
    std::string name;

    
public:
    Doctor(PatientQueue* waitingList, LinkedList* historyList);
    void displayDoctorMenu();
    void viewInfo();
    void modifyPatient(Patient* patient);
    void treatPatient(); 
    void searchPatient();
    void setName(std::string name);
    std::string getName();

};

