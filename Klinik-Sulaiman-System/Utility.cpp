# include <iostream>
# include <string>
# include <conio.h>
#include <string.h>
# include "Utility.h"
# include "Doctor.h"
# include "Nurse.h"
# include "Patient.h"
# include "LinkedList.h"
# include "Node.h"

// Global variable : Doctor username + password
std::string doctorCredentials[2][2] = { {"Ong","qwe"},
							 {"doctor2Name", "doctor2Password"} };


// Global variable : Doctor username + password
std::string nurseCredentials[2][2] = { {"Marry","nurse1Password"},
					 {"nurse2Name", "nurse2Password"} };

LinkedList* Utility::mergeSort(LinkedList* linkedList, int sortMode) {
	if (sortMode != 0 && sortMode != 1 && sortMode != 2 && sortMode != 3)
		return NULL;

	LinkedList* sortedList = linkedList;
	int segmentSize = 1;
	while (true) {
		Node* firstPart = sortedList->getHeadReference();
		Node* secondPart = firstPart;
		sortedList = new LinkedList();
		int totalMerges = 0;
		int firstPartSize;
		int secondPartSize;
		while (firstPart != NULL) {
			totalMerges++;
			firstPartSize = 0;
			secondPartSize = segmentSize;
			// Set two pointers to correct starting position
			while (firstPartSize < segmentSize && secondPart != NULL) {
				firstPartSize++;
				secondPart = secondPart->getNextNode();
			}
			// Compare
			while (firstPartSize > 0 && (secondPartSize > 0 && secondPart != NULL)) {
				if (sortMode == 0) { // Compare patient ID
					if (firstPart->getPatient()->getPatientID().length() < secondPart->getPatient()->getPatientID().length()) {
						sortedList->append(firstPart->getPatient());
						firstPart = firstPart->getNextNode();
						firstPartSize--;
					}
					else if (firstPart->getPatient()->getPatientID().length() > secondPart->getPatient()->getPatientID().length()) {
						sortedList->append(secondPart->getPatient());
						secondPart = secondPart->getNextNode();
						secondPartSize--;
					}
					else {
						if (firstPart->getPatient()->getPatientID() <= secondPart->getPatient()->getPatientID()) {
							sortedList->append(firstPart->getPatient());
							firstPart = firstPart->getNextNode();
							firstPartSize--;
						}
						else {
							sortedList->append(secondPart->getPatient());
							secondPart = secondPart->getNextNode();
							secondPartSize--;
						}
					}
				}
				else if (sortMode == 1) { // Compare patient first name
					if (firstPart->getPatient()->getFirstName() <= secondPart->getPatient()->getFirstName()) {
						sortedList->append(firstPart->getPatient());
						firstPart = firstPart->getNextNode();
						firstPartSize--;
					}
					else {
						sortedList->append(secondPart->getPatient());
						secondPart = secondPart->getNextNode();
						secondPartSize--;
					}
				}
				else if (sortMode == 2) { // Compare patient sickness description
					if (firstPart->getPatient()->getSicknessDescription() <= secondPart->getPatient()->getSicknessDescription()) {
						sortedList->append(firstPart->getPatient());
						firstPart = firstPart->getNextNode();
						firstPartSize--;
					}
					else {
						sortedList->append(secondPart->getPatient());
						secondPart = secondPart->getNextNode();
						secondPartSize--;
					}
				}

				else if (sortMode == 3) { // Compare patient time
					if (firstPart->getPatient()->getVisitHour() < secondPart->getPatient()->getVisitHour()) {
						sortedList->append(firstPart->getPatient());
						firstPart = firstPart->getNextNode();
						firstPartSize--;
					}
					else if (firstPart->getPatient()->getVisitHour() > secondPart->getPatient()->getVisitHour()) {
						sortedList->append(secondPart->getPatient());
						secondPart = secondPart->getNextNode();
						secondPartSize--;
					}
					else if (firstPart->getPatient()->getVisitMinute() < secondPart->getPatient()->getVisitMinute()) {
						sortedList->append(firstPart->getPatient());
						firstPart = firstPart->getNextNode();
						firstPartSize--;
					}
					else if (firstPart->getPatient()->getVisitMinute() > secondPart->getPatient()->getVisitMinute()) {
						sortedList->append(secondPart->getPatient());
						secondPart = secondPart->getNextNode();
						secondPartSize--;
					}
					else if (firstPart->getPatient()->getVisitSecond() <= secondPart->getPatient()->getVisitSecond()) {
						sortedList->append(firstPart->getPatient());
						firstPart = firstPart->getNextNode();
						firstPartSize--;
					}
					else {
						sortedList->append(secondPart->getPatient());
						secondPart = secondPart->getNextNode();
						secondPartSize--;
					}
				}
			}

			while (firstPartSize > 0) {
				sortedList->append(firstPart->getPatient());
				firstPart = firstPart->getNextNode();
				firstPartSize--;
			}
			while (secondPartSize > 0 && secondPart != NULL) {
				sortedList->append(secondPart->getPatient());
				secondPart = secondPart->getNextNode();
				secondPartSize--;
			}

			firstPart = secondPart;

		}
		if (totalMerges <= 1) {
			return sortedList;
		}
		segmentSize *= 2;
	}
}



 std::string Utility::getPassword() {
	std::string password;
	char lastChar = ' ';
	while (lastChar != 13) {
		lastChar = _getch();
		if (lastChar != 13 && lastChar != '\b') {
			std::cout << '*';
			password += lastChar;
		}
		else if (lastChar == '\b' && password.length() > 0) {
			std::cout << "\b \b"; 
			password.pop_back();
		}
	}
	return password;
}



void Utility::viewPatient(LinkedList* linkedList)
{
	bool terminate = false;
	Node* currentNode = linkedList->getHeadReference();

	if (currentNode != NULL) {
		while (!terminate)
		{
			system("cls");
			std::cout << currentNode->getPatient()->toString();

			bool traverseNode = false;
			int choice = 0;
			std::cout << "Press : " << std::endl;
			if (currentNode->getPreviousNode() != NULL)
				std::cout << "[0] to view previous page " << std::endl;
			if (currentNode->getNextNode() != NULL)
				std::cout << "[1] to view next page "<<std::endl;
			std::cout << "[2] to exit viewing:  "<<std::endl;
			std::cin >> choice;
			if (std::cin.fail()) {
				std::cin.clear();
				std::cin.ignore(256, '\n');
				std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
				system("pause");
				continue;
			}
			std::cin.ignore(256, '\n');
			switch (choice) {
			case 0:
				if (currentNode->getPreviousNode() != NULL) {
					currentNode = currentNode->getPreviousNode();
					break;
				}
				else {
					std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
					system("pause");
					break;
				}
			case 1:
				if (currentNode->getNextNode() != NULL) {
					currentNode = currentNode->getNextNode();
					break;
				}
				else {
					std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
					system("pause");
					break;
				}
			case 2:
				terminate = true;
				break;
			default :
				std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
				system("pause");

			}
			

			//if (currentNode->getPreviousNode() == NULL && currentNode->getNextNode() == NULL)
			//{
			//		std::cout << "\n[1] to exit viewing: ";
			//		std::cin >> choice;
			//		if (std::cin.fail())
			//		{
			//			std::cin.clear();
			//			std::cin.ignore(256, '\n');
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			continue;
			//		}
			//		switch (choice)
			//		{
			//		case 1:
			//			terminate = true;
			//			break;

			//		default:
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//				system("pause");
			//			break;
			//		}
			//}
			//else if (currentNode->getPreviousNode() == NULL)
			//{
			//		std::cout << "\nPlease enter [1] to view next page or [2] to exit viewing: ";
			//		std::cin >> choice;
			//		if (std::cin.fail())
			//		{
			//			std::cin.clear();
			//			std::cin.ignore(256, '\n');
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			continue;
			//		}
			//		switch (choice)
			//		{
			//		case 1:
			//			currentNode = currentNode->getNextNode();
			//			break;

			//		case 2:
			//			terminate = true;
			//			break;

			//		default:
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			break;
			//		}
			//}
			//else if (currentNode->getNextNode() == NULL)
			//{

			//		std::cout << "\nPlease enter [0] to view previous page or [2] to exit viewing: ";
			//		std::cin >> choice;
			//		if (std::cin.fail())
			//		{
			//			std::cin.clear();
			//			std::cin.ignore(256, '\n');
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			continue;
			//		}
			//		switch (choice)
			//		{
			//		case 0:
			//			currentNode = currentNode->getPreviousNode();
			//			break;

			//		case 2:
			//			terminate = true;
			//			break;

			//		default:
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			break;
			//		}
			//}
			//else
			//{
			//	//if (!traverseNode)
			//	//{
			//		std::cout << "\nPlease enter [0] to view previous page or [1] to view next page or [2] to exit viewing: ";
			//		std::cin >> choice;
			//		if (std::cin.fail())
			//		{
			//			std::cin.clear();
			//			std::cin.ignore(256, '\n');
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			continue;
			//		}
			//		switch (choice)
			//		{
			//		case 0:
			//			currentNode = currentNode->getPreviousNode();
			//			traverseNode = true;
			//			break;

			//		case 1:
			//			currentNode = currentNode->getNextNode();
			//			traverseNode = true;
			//			break;

			//		case 2:
			//			terminate = true;
			//			traverseNode = true;
			//			break;

			//		default:
			//			std::cout << "\nSorry, but we do not support this input, please try again.\n\n";
			//			system("pause");
			//			break;
			//		}
			//	//}
			//}

		}
	}
	else
		std::cout << "\nThere are 0 patients in the waiting list.";
}

int Utility::login(std::string userName, std::string password, char isDoctor,Doctor* doctor, Nurse* nurse)
{

	bool found = false;
	int rowSize = 0;

	if (isDoctor == '1')
	{
		rowSize = sizeof doctorCredentials / sizeof doctorCredentials[0];

		for (int i = 0; i < rowSize; i++)
		{
			if (doctorCredentials[i][0] == userName && doctorCredentials[i][1] == password)
			{
				found = true;
				doctor->setName(userName);
				return 0;
			}
		}

	}
	else
	{
		rowSize = sizeof nurseCredentials / sizeof nurseCredentials[0];

		for (int i = 0; i < rowSize; i++)
		{
			if (nurseCredentials[i][0] == userName && nurseCredentials[i][1] == password)
			{
				found = true;
				nurse->setName(userName);
				return 1;
			}
		}
	}

	std::cout << "Sorry, but their is no matching records found\n";
	return -1;
}


bool Utility::stringNumber(std::string str)
{
	for (char &c : str)
	{
		if (std::isdigit(c) == 0)
			return false;
	}
	return true;
}