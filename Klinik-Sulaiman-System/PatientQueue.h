#pragma once

class Node;
class Patient;

class PatientQueue : public LinkedList {
	Node* lastDisabledPatient = NULL;
public :
	void insertPatient(Patient* patient); 

    Patient* getNextPatient();
    
    int removePatient(std::string patientID);

};
