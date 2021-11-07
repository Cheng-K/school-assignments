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
    void modifyMedicalInformation(Patient* patient);
    void treatPatient(); 
    void searchPatient();
    void patientEditor(LinkedList* patientList);
    void setName(std::string name);
    std::string getName();

};

