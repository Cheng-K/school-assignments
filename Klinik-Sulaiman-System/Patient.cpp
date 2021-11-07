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

	time_t now;
	struct tm* visitTime;
	now = time(NULL);
	visitTime = localtime(&now);
	visitYear = 1900 + visitTime->tm_year;
	visitMonth = 1+visitTime->tm_mon;
	visitDay = visitTime->tm_mday;
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
		+ "Sickness Description: " + sicknessDescription + "\n" + "Medical Information: " + medicineInformation + "\n" + "Disabled : " + std::to_string(disabled) + "\nVisiting Day(dd/mm/yy) : "+ std::to_string(visitDay) +"/" + std::to_string(visitMonth) +"/" + std::to_string(visitYear) 
		+ "\nVisiting Time(hour / min / sec) : ";
	if (visitHour <= 9)
		patientProfile += "0";
	patientProfile += std::to_string(visitHour) + ":";
	if (visitMinute <= 9)
		patientProfile += "0";
	patientProfile += std::to_string(visitMinute) + ":";
	if (visitSecond <= 9)
		patientProfile += "0";
	patientProfile += std::to_string(visitSecond) + "\n";

	return patientProfile;
}

void Patient::setTime(int hour, int minute, int second) {
	visitHour = hour;
	visitMinute = minute;
	visitSecond = second;
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


