#pragma once

class PatientQueue;
class LinkedList;
class Patient;

class Nurse {
	PatientQueue* patientQueue = NULL;
	LinkedList* historyList = NULL;
	std::string name;

public:
	Nurse(PatientQueue* patientQueue, LinkedList* historyList);
	void displayNurseMenu();
	void callPatient(); 
	Patient* createPatient(); 
	void viewWaitingList(); 
	void addPatient(Patient* newPatient); 
	void deletePatient(); 
	void searchPatient();
	std::string getName();
	void setName(std::string name);
};

