#pragma once
#include <string>

class Patient {
	std::string patientID;
	std::string firstName;
	std::string sicknessDescription;
	std::string medicineInformation;
	std::string doctorName;
	bool disabled;

public :
	Patient (std::string patientID, std::string firstName, std::string sicknessDescription, std::string medicineInformation, std:: string doctorName, bool disabled) {
		this->patientID = patientID;
		this->firstName = firstName;
		this->sicknessDescription = sicknessDescription;
		this->medicineInformation = medicineInformation;
		this->doctorName = doctorName;
		this->disabled = disabled;
	}


	bool isDisabled() {
		return disabled;
	}

	std::string toString()
	{
		std::string patientProfile = "Patient ID: " + patientID + "\n" + "Patient first Name: " + firstName + "\n" + "Responsible Doctor: " + doctorName + "\n"
			+ "Sickness Description: " + sicknessDescription + "\n" + "Medical Information: " + medicineInformation + "\n" + "Disabled : " + std::to_string(disabled) + "\n";
		return patientProfile;
	}

	//Getters
	std::string getPatientID() {
		return patientID;
	}

	std::string getFirstName() {
		return firstName;
	}

	std::string getSicknessDescription() {
		return sicknessDescription;
	}
	std::string getMedicineInformation() {
		return medicineInformation;
	}
	std::string getDoctorName() {
		return doctorName;
	}


	//Setters
	void setPatientID(std::string patientID) {
		this->patientID = patientID;
	}

	void setFirstName(std::string firstName) {
		this->firstName = firstName;
	}

	void setSicknessDescription(std::string sicknessDescription) {
		this->sicknessDescription = sicknessDescription;
	}

	void setMedicineInformation(std::string medicineInformation) {
		this->medicineInformation = medicineInformation;
	}

	void setDoctorName(std::string doctorName) {
		this->doctorName = doctorName;
	}

};


