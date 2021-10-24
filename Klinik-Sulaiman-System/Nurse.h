#pragma once
#include <iostream>
#include <string>
#include "PatientQueue.h"
#include "Patient.h"


class Nurse {
	PatientQueue* waitingList = NULL;

public:
	Nurse(PatientQueue* waitingList) {
		this->waitingList = waitingList;
	}
	void callPatient() {
		Patient* currentPatient = waitingList->getNextPatient();
		std::cout << currentPatient->toString() << std::endl;
	}

	Patient* createPatient()
	{
		std::string firstName;
		std::string lastName;
		std::string gender;
		std::string phone;
		std::string address;
		std::string sicknessDescription;
		std::string patientInformation;
		std::string doctorName;
		std::string medicineInformation = "";
		std::string disabledStatus;
		int age;
		bool isDisabled;
		std::string patientID = "PID" + std::to_string(waitingList->getSize() + 1);
		std::cout << "Please enter the patient's first name: ";
		getline(std::cin, firstName);
		std::cout << "Please enter the patient's last name: ";
		getline(std::cin, lastName);
		std::cout << "Please enter the patient's age: ";
		std::cin >> age;
		std::cin.clear();
		std::cin.ignore(256, '\n');

		while (age == 0)
		{
			std::cout << "\nInvalid input, please try again.\n";
			std::cout << "Please enter the patient's age: ";
			std::cin >> age;
			std::cin.clear();
			std::cin.ignore(256, '\n');
		}
		std::cout << "Please enter the patient's phone number: ";
		getline(std::cin, phone);
		std::cout << "Please enter the patient's sickness description: ";
		getline(std::cin, sicknessDescription);
		std::cout << "Please enter the patient's address: ";
		getline(std::cin, address);
		std::cout << "Is the patient disabled? (yes/no): ";
		getline(std::cin, disabledStatus);
		while (disabledStatus != "yes" && disabledStatus != "no")
		{
			// toupper / tolower pending
			std::cout << "\nInvalid input, please try again.\n";
			std::cout << "Is the patient disabled? (yes/no): ";
			getline(std::cin, disabledStatus);
		}
		if (disabledStatus == "yes")
		{
			// toupper / tolower pending
			isDisabled = true;
		}
		else
		{
			isDisabled = false;
		}
		std::cout << "Please enter the patient's doctor name: ";
		getline(std::cin, doctorName);
		//Prompt for patient visitation time
		Patient* newPatient = new Patient (patientID,firstName,lastName,sicknessDescription,medicineInformation,doctorName,isDisabled);
		return newPatient;
	}

	void viewWaitingList()
	{
		std::string cont;
		std::cout << "\nHere are all the patients in the waiting list: \n\n";
		waitingList->displayList();
		std::cout << "\nPlease press 'enter' to continue";
		getline(std::cin, cont);
		return;
	}
};