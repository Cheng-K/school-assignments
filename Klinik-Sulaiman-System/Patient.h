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
	bool isDisabled() {
		return disabled;
	}

	std::string toString()
	{
		std::string patientProfile = "Patient ID: " + patientID + "\n" + "Patient first Name: " + firstName + "\n" + "Responsible Doctor: " + doctorName + "\n"
			+ "Sickness Description: " + sicknessDescription + "\n" + "Medical Information: " + medicineInformation + "\n";
		return patientProfile;
	}

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


};


