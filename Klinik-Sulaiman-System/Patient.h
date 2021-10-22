class Patient
{
public:
    std::string patientID;
    std::string firstName;
    std::string sicknessDescription;
    std::string medicineInformation;
    std::string doctorName;

	std::string toString()
	{
		std::string patientProfile = "Patient ID: " + patientID + "\n" + "Patient first Name: " + firstName + "\n" + "Responsible Doctor: " + doctorName + "\n"
			+ "Sickness Description: " + sicknessDescription + "\n" + "Medical Information: " + medicineInformation + "\n";
		return patientProfile;
	}

};


