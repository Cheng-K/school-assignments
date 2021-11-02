#pragma once
#include <iostream>
#include <string>
#include "PatientQueue.h"
#include "Patient.h"


class Nurse {
	PatientQueue* patientQueue = NULL;
	LinkedList* historyList = NULL;
	Patient* newPatient;

public:
	Nurse(PatientQueue* patientQueue, LinkedList* historyList) {
		this->patientQueue = patientQueue;
		this->historyList = historyList;
	}

	void displayNurseMenu()
	{
		int choice = 0;
		std::string confirm;
		while (choice != 5)
		{
			system("cls");
			std::cout << "\t\t\t=== Nurse Menu ===\n";
			std::cout << "\t\t\t1. View patient waiting list\n";
			std::cout << "\t\t\t2. Add patient to the waiting list\n";
			std::cout << "\t\t\t3. Call next patient for doctor visit\n";
			std::cout << "\t\t\t4. Search patient from waiting list\n";
			std::cout << "\t\t\t5. Exit\n\n";
			std::cout << "\t\t\t Enter a number above: ";

			std::cin >> choice;
			std::cin.clear();
			std::cin.ignore(256, '\n');
			if (std::cin.fail())
			{
				std::cin.ignore(256, '\n');
				continue;
			}

			switch (choice)
			{
			case 1:
				viewWaitingList();
				break;

			case 2:
				newPatient = createPatient();
				std::cout << "\n---Patient Details---\n";
				std::cout<<newPatient->toString();
				std::cout << "\nConfirm adding patient into the waiting list? (yes/no): ";
				getline(std::cin, confirm);
				while (confirm != "yes" && confirm != "no")
				{
					std::cout << "\nInvalid input, please enter 'yes' or 'no' to confirm for adding patient: ";
					getline(std::cin, confirm);
				}
				if (confirm == "no")
				{
					std::cout << "\nProcess has been cancelled, press 'enter' to continue.";
					getline(std::cin, confirm);
					break;
				}
				else
				{
					addPatient(newPatient);
				}
				break;

			case 3:
				callPatient();
				// transition to history list
				break;

			case 4:
				searchPatient();
				break;

			case 5:
				std::cout << "Good bye!";
				break;

			default:
				std::cout << "\n\n\t\t\t WARNING: The input you have entered is not supported by the system, please select from the menu\n";
				break;
			}
			system("pause");
		}

		return;
	}

	void callPatient() {
		Patient* currentPatient = patientQueue->getNextPatient();
		if (currentPatient == NULL)
		{
			historyList->append(currentPatient);
		}
		else
		{
			std::cout << currentPatient->toString() << std::endl;
		}
	}

	Patient* createPatient() {
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
		std::string patientID = "PID" + std::to_string(patientQueue->getSize() + 1);
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


	void viewWaitingList() {
		std::string cont;
		std::cout << "\nHere are all the patients in the waiting list: \n\n";
		patientQueue->displayList();
		std::cout << "\nPlease press 'enter' to continue";
		getline(std::cin, cont);
		return;
	}

	void addPatient(Patient* newPatient) {
		std::cout << "Inserting patient into the waiting list..." << std::endl << std::endl;
		patientQueue->insertPatient(newPatient);
		std::cout << "Patient added successfully" << std::endl;

	}

	void deletePatient() {
		std::string patientIDDelete;
		std::cout << "Enter ID of patient to be deleted : ";
		getline(std::cin,patientIDDelete);
		int result = patientQueue->removePatient(patientIDDelete);
		if (result == 1)
			std::cout << "Patient successfully deleted. " << std::endl;
		else
			std::cout << "Invalid Patient ID or Patient with this ID does not exist..." << std::endl;
	}

	void searchPatient()
	{
		int searchMode;
		std::string cont;
		std::string searchReference;
		std::cout << "1. Search by Patient ID \n2. Search by Patient First Name"<<
			"\n3. Go back\n\nPlease select an option to search for the patients' profile: ";
		std::cin >> searchMode;
		std::cin.clear();
		std::cin.ignore(256, '\n');
		while (searchMode < 1 || searchMode>3)
		{
			std::cout << "\nInvalid input, please select an option shown in the menu above (1/2/3): ";
			std::cin >> searchMode;
			std::cin.clear();
			std::cin.ignore(256, '\n');
		}
		if (searchMode == 3)
		{
			return;
		}
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
		return;
	}
};

