# pragma once
# include <iostream>
# include "LinkedList.h"

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

	void viewPatient(LinkedList* linkedList) 
	{
		bool terminate = false;
		Node* currentNode = linkedList->getHeadReference();

		while (terminate)
		{
			system("cls");
			std::cout << currentNode->getPatient()->toString();

			bool traverseNode = false;
			int choice = 0;

			if (currentNode->getPreviousNode() == NULL)
			{
				if (!traverseNode)
				{
					std::cout << "Please enter [1] to view next page or [2] to exit viewing";
					std::cin >> choice;
					if (std::cin.fail())
					{
						std::cin.ignore(256, '\n');
						continue;
					}
					switch (choice)
					{
					case 1:
						currentNode = currentNode->getNextNode();
						traverseNode = true;
						break;

					case 2:
						terminate = true;
						traverseNode = true;
						break;

					default:
						std::cout << "\nSorry, but we do not support this input, please try again";
						break;
					}
				}
			}
			else if (currentNode->getNextNode == NULL)
			{
				if (!traverseNode)
				{
					std::cout << "Please enter [0] to view previous page or [2] to exit viewing";
					std::cin >> choice;
					if (std::cin.fail())
					{
						std::cin.ignore(256, '\n');
						continue;
					}
					switch (choice)
					{
					case 0:
						currentNode = currentNode->getPreviousNode();
						traverseNode = true;
						break;

					case 2:
						terminate = true;
						traverseNode = true;
						break;

					default:
						std::cout << "\nSorry, but we do not support this input, please try again";
						break;
					}
				}
			}
			else 
			{
				if (!traverseNode)
				{
					std::cout << "Please enter [0] to view previous page or [1] to view next page or [2] to exit viewing";
					std::cin >> choice;
					if (std::cin.fail())
					{
						std::cin.ignore(256, '\n');
						continue;
					}
					switch (choice)
					{
					case 0:
						currentNode = currentNode->getPreviousNode();
						traverseNode = true;
						break;

					case 1:
						currentNode = currentNode->getNextNode();
						traverseNode = true;
						break;

					case 2:
						terminate = true;
						traverseNode = true;
						break;

					default:
						std::cout << "\nSorry, but we do not support this input, please try again";
						break;
					}
				}
			}

		}
		return;
	}


};