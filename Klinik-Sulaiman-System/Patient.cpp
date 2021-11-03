#pragma warning(disable : 4996)
#include <iostream>
#include <string>
#include <time.h>
#include "Patient.h"
 
Patient::Patient(std::string patientID, std::string firstName, std::string lastName, std::string sicknessDescription, std::string medicineInformation, std::string doctorName, bool disabled) {
	this->patientID = patientID;
	this->firstName = firstName;
	this->lastName = lastName;
	this->sicknessDescription = sicknessDescription;
	this->medicineInformation = medicineInformation;
	this->doctorName = doctorName;
	this->disabled = disabled;

	time_t now ;
	time(&now);
	visitTime = localtime(&now);
}


bool Patient::isDisabled() {
	return disabled;
}

std::string Patient::toString()
{
	std::string patientProfile = "Patient ID: " + patientID + "\n" + "Patient first Name: " + firstName + "\n" + "Patient last Name: " + lastName + "\n" + "Responsible Doctor: " + doctorName + "\n"
		+ "Sickness Description: " + sicknessDescription + "\n" + "Medical Information: " + medicineInformation + "\n" + "Disabled : " + std::to_string(disabled) + "\nVisiting Hour: " + std::to_string(visitTime->tm_hour) + "\tVisiting Min: " + std::to_string(visitTime->tm_min) + "\tVisiting Sec: " + std::to_string(visitTime->tm_sec) + "\n";
	return patientProfile;
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

tm* Patient::getVisitTime() {
	return visitTime;
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




