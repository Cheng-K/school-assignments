# pragma once
# include <iostream>
# include <string>
# include <conio.h>
# include "LinkedList.h"

using namespace std;
class Utility {
public :
	static LinkedList* mergeSort(LinkedList* linkedList, int searchMode) {
		if (searchMode != 0 && searchMode != 1 && searchMode != 2)
			return NULL;

		LinkedList* sortedList = linkedList;
		int segmentSize = 1;
		while (true) {
			Node* firstPart = sortedList->getHeadReference();
			Node* secondPart = firstPart;
			LinkedList* sortedList = new LinkedList();
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
					if (searchMode == 0) {
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
					else if (searchMode == 1) {
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
					else if (searchMode == 2) {
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

	static void viewPatient() {
		std::cout << "Inside view Patient Method" << std::endl;
	}

	/*
		Method : getPassword
		Parameter : None
		Description : Prompts user to enter password and masks the password input with * and returns the password as string.
	*/

	static std::string getPassword() {
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
		std::cout << "Password is : " << password << std::endl;
		system("pause"); // testing
		return password;
	}

	static void displayDoctorMenu() {
		std::cout << "Main menu " << std::endl;
		std::cout << "1. View Patient History List and Patient Waiting List" << std::endl;
		std::cout << "2. Treat Patients" << std::endl;
		std::cout << "3. Search Patients" << std::endl;
		std::cout << "Press any key to logout..." << std::endl;
		std::cout << "Select one of the options displayed above by entering the number : ";
	}
};