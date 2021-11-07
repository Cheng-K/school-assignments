#pragma warning(disable : 4996)
#include <iostream>
#include <string>
#include <algorithm>
#include <cctype>
#include <time.h>
#include "Patient.h"
#include "Utility.h"
 
Patient::Patient(std::string patientID, std::string firstName, std::string lastName, std::string sicknessDescription, std::string medicineInformation, std::string doctorName, bool disabled) {
	this->patientID = patientID;
	this->firstName = firstName;
	this->lastName = lastName;
	this->sicknessDescription = sicknessDescription;
	this->medicineInformation = medicineInformation;
	this->doctorName = doctorName;
	this->disabled = disabled;

	time_t now;
	struct tm* visitTime;
	now = time(NULL);
	visitTime = localtime(&now);
	visitHour = visitTime->tm_hour;
	visitMinute = visitTime->tm_min;
	visitSecond = visitTime->tm_sec;
}


bool Patient::isDisabled() {
	return disabled;
}

std::string Patient::toString()
{
	std::string patientProfile = "Patient ID: " + patientID + "\n" + "Patient first Name: " + firstName + "\n" + "Patient last Name: " + lastName + "\n" + "Responsible Doctor: " + doctorName + "\n"
		+ "Sickness Description: " + sicknessDescription + "\n" + "Medical Information: " + medicineInformation + "\n" + "Disabled : " + std::to_string(disabled) + "\nVisiting Hour: " + std::to_string(visitHour) + "\tVisiting Min: " + std::to_string(visitMinute) + "\tVisiting Sec: " + std::to_string(visitSecond) + "\n";
	return patientProfile;
}

void Patient::setTime(int hour, int minute, int second) {
	visitHour = hour;
	visitMinute = minute;
	visitSecond = second;
}

void Patient::modifyRecord(char mode) {
	if (mode == '0') {
		std::cout << "Enter the patient's new first name : ";
		getline(std::cin, firstName);
	}
	else if (mode == '1') {
		std::cout << "Enter the patient's new last name : ";
		getline(std::cin, );
	}
	else if (mode == '2') {
		while (true) {
			std::cout << "Enter the patient's new age : ";
			std::cin;
			if (std::cin.fail()) {
				std::cin.clear();
			}
			else {
				std::cin.ignore(256, '\n');
				return;
			}
		}
	}
	else if (mode == '3') {
		std::cout << "Enter the patient's gender : ";
		getline(std::cin,)
	}
	else if (mode == '4') {
		std::cout << "Enter the patient's new phone number : ";
		getline(std::cin, );
		while (!Utility::stringNumber()) {
			std::cout << "\nInvalid input, please enter numeric values for phone numbers only.\n";
			std::cout << "Please enter the patient's new phone number again: ";
			getline(std::cin, phone);
		}

	}
	else if (mode == '5') {
		std::cout << "Enter the patient's new address : ";
		getline(std::cin,)
	}
	else if (mode == '6') {
		std::cout << "Enter the patient's new sickness description : ";
		getline(std::cin, sicknessDescription);

	}
	else if (mode == '7') {
		std::cout << "Is the patient disabled ? (Yes/No): ";
		std::string disabledStatus;
		getline(std::cin, disabledStatus);
		std::transform(disabledStatus.begin(), disabledStatus.end(), disabledStatus.begin(), [](unsigned char c)
			{ return std::tolower(c); });
		while (disabledStatus != "yes" && disabledStatus != "no")
		{
			std::cout << "\nInvalid input, please try again.\n";
			std::cout << "Is the patient disabled? (Yes/No): ";
			getline(std::cin, disabledStatus);
			std::transform(disabledStatus.begin(), disabledStatus.end(), disabledStatus.begin(), [](unsigned char c)
				{ return std::tolower(c); });
		}
		if (disabledStatus == "yes")
		{
			disabled = true;
		}
		else
		{
			disabled = false;
		}
	}
	else if (mode == '8') {
		std::cout << "Enter the patient's new doctor name : ";
	}
	else if (mode == '9') {
		std::cout << "Enter the patient's new medicine information : ";
		getline(std::cin, medicineInformation);
	}
}

//Getters
std::string Patient::getPatientID() {
	return patientID;
}

std::string Patient::getFirstName() {
	return firstName;
}

std::string Patient::getSicknessDescription() {
	return sicknessDescription;
}
std::string Patient::getMedicineInformation() {
	return medicineInformation;
}
std::string Patient::getDoctorName() {
	return doctorName;
}

int Patient::getVisitHour() {
	return visitHour;
}

int Patient::getVisitMinute() {
	return visitMinute;
}

int Patient::getVisitSecond() {
	return visitSecond;
}

//Setters
void Patient::setPatientID(std::string patientID) {
	this->patientID = patientID;
}

void Patient::setFirstName(std::string firstName) {
	this->firstName = firstName;
}

void Patient::setSicknessDescription(std::string sicknessDescription) {
	this->sicknessDescription = sicknessDescription;
}

void Patient::setMedicineInformation(std::string medicineInformation) {
	this->medicineInformation = medicineInformation;
}

void Patient::setDoctorName(std::string doctorName) {
	this->doctorName = doctorName;
}


