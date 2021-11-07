#pragma once
#include <iostream>
#include <algorithm>
#include <cctype>
#include <string>
#include "Nurse.h"
#include "Utility.h"
#include "LinkedList.h"
#include "PatientQueue.h"
#include "Patient.h"




Nurse::Nurse(PatientQueue* patientQueue, LinkedList* historyList) {
	this->patientQueue = patientQueue;
	this->historyList = historyList;
}

void Nurse::displayNurseMenu()
{
	int choice = 0;
	int viewChoice = 0;
	std::string confirm;
	while (choice != 7)
	{
		system("cls");
		std::cout << "=== Nurse Menu ===\n";
		std::cout << "1. View patient waiting list\n";
		std::cout << "2. Add patient to the waiting list\n";
		std::cout << "3. Call next patient for doctor visit\n";
		std::cout << "4. Search patient from waiting list\n";
		std::cout << "5. View patient waiting list sorted by time\n";
		std::cout << "6. View patient waiting list sorted by patient ID\n";
		std::cout << "7. Logout\n\n";
		std::cout << "Enter a number above: ";

		std::cin >> choice;
		std::cin.clear();
		std::cin.ignore(256, '\n');
		if (std::cin.fail())
		{
			std::cin.clear();
			std::cin.ignore(256, '\n');
			continue;
		}

		switch (choice)
		{
		case 1:
			std::cout << "Please enter [1] to view full waiting list, and [2] to view via page-by-page: ";
			std::cin >> viewChoice;
			if (std::cin.fail() || (viewChoice != 1 && viewChoice != 2))
			{
				std::cin.clear();
				std::cin.ignore(256, '\n');
				std::cout << "\n\nWARNING: The input you have entered is not supported by the system, please select from the menu\n";
			}
			else
			{
				std::cin.clear();
				std::cin.ignore(256, '\n');
				if (viewChoice == 1)
					viewWaitingList();
				else if (viewChoice == 2)
					Utility::viewPatient(patientQueue);
			}
			break;
		case 2: {
			Patient* newPatient = createPatient();
			std::cout << "\n---Patient Details---\n";
			std::cout << newPatient->toString();
			std::cout << "\nConfirm adding patient into the waiting list? (Yes/No): ";
			getline(std::cin, confirm);
			std::transform(confirm.begin(), confirm.end(), confirm.begin(), [](unsigned char c) 
				{ return std::tolower(c); });
			while (confirm != "yes" && confirm != "no")
			{
				std::cout << "\nInvalid input, please enter 'Yes' or 'No' to confirm for adding patient: ";
				getline(std::cin, confirm);
				std::transform(confirm.begin(), confirm.end(), confirm.begin(), [](unsigned char c)
					{ return std::tolower(c); });
			}
			if (confirm == "no")
			{
				std::cout << "\nProcess has been cancelled.\n";
				break;
			}
			else
			{
				addPatient(newPatient);
			}
		}
			break;

		case 3:
			callPatient();
			break;

		case 4:
			searchPatient();
			break;

		case 5: {
			LinkedList* visitTimeSorted = Utility::mergeSort(patientQueue, 3);
			if (visitTimeSorted->getHeadReference() != NULL)
				visitTimeSorted->displayList();
			else
				std::cout << "\nThere are 0 patients in the patient queue" << std::endl;
		}
			  break;

		case 6: {
			LinkedList* patientIDSorted = Utility::mergeSort(patientQueue, 0);
			if (patientIDSorted->getHeadReference() != NULL)
				patientIDSorted->displayList();
			else
				std::cout << "\nThere are 0 patients in the patient queue" << std::endl;
		}
			  break;

		case 7:
			return;

		default:
			std::cout << "\n\nWARNING: The input you have entered is not supported by the system, please select from the menu\n";
		}
		std::cout << std::endl << std::endl;
		system("pause");
	}
	return;
}

void Nurse::callPatient() {
	Patient* currentPatient = patientQueue->getNextPatient();
	if (currentPatient != NULL) {
		std::cout << "\n-----Details of patient called----\n\n";
		std::cout << currentPatient->toString() << std::endl << std::endl;
		historyList->insertAtFront(currentPatient);
	}

	else {
		std::cout << "There are 0 patients in the waiting list right now." << std::endl;
	}

}

Patient* Nurse::createPatient() {
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
	std::string patientID = "PID" + std::to_string(patientQueue->getSize() + historyList->getSize()+ 1);
	system("cls");
	std::cout << "-----Create patient menu-----\n\n";
	std::cout << "Please enter the patient's first name: ";
	getline(std::cin, firstName);
	std::cout << "Please enter the patient's last name: ";
	getline(std::cin, lastName);
	std::cout << "Please enter the patient's age: ";
	std::cin >> age;
	std::cin.clear();
	std::cin.ignore(256, '\n');
	std::cout << "Please enter the patient's gender: ";
	getline(std::cin, gender);

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
	while (!(Utility::stringNumber(phone)))
	{
		std::cout << "\nInvalid input, please enter numeric values for phone numbers only.\n";
		std::cout << "Please enter the patient's phone number: ";
		getline(std::cin, phone);
	}
	std::cout << "Please enter the patient's sickness description: ";
	getline(std::cin, sicknessDescription);
	std::cout << "Please enter the patient's address: ";
	getline(std::cin, address);
	std::cout << "Is the patient disabled? (Yes/No): ";
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
		isDisabled = true;
	}
	else
	{
		isDisabled = false;
	}
	std::cout << "Please enter the patient's doctor name: ";
	getline(std::cin, doctorName);
	Patient* newPatient = new Patient(patientID, firstName, lastName, sicknessDescription, medicineInformation, doctorName, isDisabled, gender, age);
	return newPatient;
}


void Nurse::viewWaitingList() {
	if (patientQueue->getHeadReference() == NULL)
	{
		std::cout << "\nThere are 0 patients in the waiting list.\n\n";
	}
	else
	{
		std::cout << "\nHere are all the patients in the waiting list: \n\n";
		patientQueue->displayList();
	}
	return;
}

void Nurse::addPatient(Patient* newPatient) {
	std::cout << "\nInserting patient into the waiting list..." << std::endl << std::endl;
	patientQueue->insertPatient(newPatient);
	std::cout << "Patient added successfully" << std::endl;

}

void Nurse::deletePatient() {
	std::string patientIDDelete;
	std::cout << "Enter ID of patient to be deleted : ";
	getline(std::cin, patientIDDelete);
	int result = patientQueue->removePatient(patientIDDelete);
	if (result == 1)
		std::cout << "Patient successfully deleted. " << std::endl;
	else
		std::cout << "Invalid Patient ID or Patient with this ID does not exist..." << std::endl;
}

void Nurse::searchPatient()
{
	int searchMode = 0;
	std::string cont;
	std::string searchReference;
	while (searchMode != 3)
	{
		system("cls");
		std::cout << "=== Nurse Searching Menu ===\n";
		std::cout << "1. Search by Patient ID \n2. Search by Patient First Name\n3.Go Back" <<
			"\n\nPlease select an option to search for the patients' profile: ";
		std::cin >> searchMode;
		if (searchMode < 1 || searchMode>3 || std::cin.fail())
		{
			std::cin.clear();
			std::cin.ignore(256, '\n');
			std::cout << "\n\n WARNING: The input you have entered is not supported by the system, please select from the menu.\n";
			std::cout << std::endl << std::endl;
			system("pause");
			continue;
		}
		std::cin.clear();
		std::cin.ignore(256, '\n');
		if (searchMode == 3)
		{
			continue;
		}
		else
		{
			std::cout << "\nPlease enter the patient details to be searched with: ";
			getline(std::cin, searchReference);
			LinkedList* patientList = patientQueue->search(searchReference, searchMode);
			if (patientList->getHeadReference() != NULL)
			{
				patientList->displayList();
			}
			else
			{
				std::cout << "\nPatient not found!\n";
			}
		}
		std::cout << std::endl << std::endl;
		system("pause");
	}
	return;
}

std::string Nurse::getName() {
	return name;
}

void Nurse::setName(std::string name) {
	this->name = name;
}