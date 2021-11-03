#pragma once


class Patient {

	struct tm* visitTime;
	std::string patientID;
	std::string firstName;
	std::string lastName;
	std::string sicknessDescription;
	std::string medicineInformation;
	std::string doctorName;
	bool disabled;

public :
	Patient (std::string patientID, std::string firstName, std::string lastName, std::string sicknessDescription, std::string medicineInformation, std:: string doctorName, bool disabled); 


	bool isDisabled(); 

	std::string toString();
	
	//Getters
	std::string getPatientID(); 

	std::string getFirstName(); 

	std::string getSicknessDescription(); 
	std::string getMedicineInformation(); 
	std::string getDoctorName();

	tm* getVisitTime();


	//Setters
	void setPatientID(std::string patientID); 

	void setFirstName(std::string firstName); 

	void setSicknessDescription(std::string sicknessDescription); 

	void setMedicineInformation(std::string medicineInformation); 

	void setDoctorName(std::string doctorName);

};


