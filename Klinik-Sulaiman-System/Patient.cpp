#pragma warning(disable : 4996)
#include <iostream>
#include <string>
#include <algorithm>
#include <cctype>
#include <time.h>
#include "Patient.h"
#include "Utility.h"
 


Patient::Patient(std::string patientID, std::string firstName, std::string lastName, std::string age, std::string gender, std::string phone, std::string address, std::string sicknessDescription, std::string medicineInformation, std::string doctorName, bool disabled) {
	this->patientID = patientID;
	this->firstName = firstName;
	this->lastName = lastName;
	this->age = age;
	this->gender = gender;
	this->phone = phone;
	this->address = address;
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

	std::string patientProfile = "Patient ID: " + patientID + "\n" + "Patient first Name: " + firstName + "\n" + "Patient last Name: " + lastName + "\n" + "Patient Age: " + age + "\n" + "Patient Gender: " + gender + "\n" + "Patient Contact: " + phone + "\n" + 
		+ "Address : " + address + "\n" + "Responsible Doctor: " + doctorName + "\n"
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

void Patient::modifyRecord(char mode) {
	if (mode == '0') {
		std::cout << "Enter the patient's new first name : ";
		getline(std::cin, firstName);
	}
	else if (mode == '1') {
		std::cout << "Enter the patient's new last name : ";
		getline(std::cin, lastName);
	}
	else if (mode == '2') {
			std::cout << "Enter the patient's new age : ";
			getline(std::cin, age);
			while (!(Utility::stringNumber(age)) || age == "")
			{
				std::cout << "\nInvalid input, please enter numeric values only.\n";
				std::cout << "Please enter the patient's new age: ";
				getline(std::cin, age);
			}
	}
	else if (mode == '3') {
		std::cout << "Enter the patient's gender (Male/Female/Others) : ";
		getline(std::cin, gender);
		std::transform(gender.begin(), gender.end(), gender.begin(), [](unsigned char c)
			{ return std::tolower(c); });
		while (gender != "male" && gender != "female" && gender != "others")
		{
			std::cout << "\nInvalid input, please try again with options given.\n";
			std::cout << "Please enter the patient's gender (Male/Female/Others): ";
			getline(std::cin, gender);
			std::transform(gender.begin(), gender.end(), gender.begin(), [](unsigned char c)
				{ return std::tolower(c); });
		}
	}
	else if (mode == '4') {
		std::cout << "Enter the patient's new phone number : ";
		getline(std::cin, phone);
		while (!(Utility::stringNumber(phone)) || phone == "")
		{
			std::cout << "\nInvalid input, please enter numeric values for phone numbers only.\n";
			std::cout << "Please enter the patient's new phone number: ";
			getline(std::cin, phone);
		}

	}
	else if (mode == '5') {
		std::cout << "Enter the patient's new address : ";
		getline(std::cin, address);
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
		int numberofDoctors = 0;
		bool validDoctor = false;
		std::string* availableDoctors = Utility::getDoctors(&numberofDoctors);
		std::cout << "\nAvailable doctors:" << std::endl;
		for (int i = 0; i < numberofDoctors; i++)
		{
			std::cout << " - " << availableDoctors[i] << std::endl;;
		}
		std::cout << "\nPlease enter the patient's doctor name from above: ";
		getline(std::cin, doctorName);
		while (validDoctor == false)
		{
			for (int i = 0; i < numberofDoctors; i++)
			{
				if (doctorName == availableDoctors[i])
				{
					validDoctor = true;
					break;
				}
			}
			if (validDoctor == false)
			{
				std::cout << "\nInvalid input, please select a doctor from the list above!";
				std::cout << "\nPlease enter the patient's new doctor name from above: ";
				getline(std::cin, doctorName);
			}
		}
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

void Patient::setVisitDay(int day, int month, int year) {
	this->visitDay = day;
	this->visitMonth = 1+month;
	this->visitYear = 1900+year;

}




