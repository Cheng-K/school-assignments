#pragma once
#include <string>

class Patient {
	bool disabled;
	std::string patientID;
public :
	bool isDisabled() {
		return disabled;
	}
	std::string getPatientID()
	{
		return patientID;
	}
};
