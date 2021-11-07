#pragma once


class Patient {
	int visitDay;
	int visitMonth;
	int visitYear;
	int visitHour;
	int visitMinute;
	int visitSecond;
	int age;
	std::string gender;
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

	void setTime(int hour, int minute, int second);
	
	//Getters
	std::string getPatientID(); 

	std::string getFirstName(); 

	std::string getSicknessDescription(); 
	std::string getMedicineInformation(); 
	std::string getDoctorName();
	
	int getVisitHour();
	int getVisitMinute();
	int getVisitSecond();

	//Setters
	void setPatientID(std::string patientID); 

	void setFirstName(std::string firstName); 

	void setSicknessDescription(std::string sicknessDescription); 

	void setMedicineInformation(std::string medicineInformation); 

	void setDoctorName(std::string doctorName);

	void Patient::setVisitDay(int Day);

	void Patient::setVisitMonth(int Month);

	void Patient::setVisitYear(int Year);

};


