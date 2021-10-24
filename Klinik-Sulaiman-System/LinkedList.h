#pragma once
# include <iostream>
# include "Node.h";
# include "Patient.h";

class LinkedList {
protected : 
	Node* head;
	Node* tail;
	int size;

public :
	LinkedList() {
		head = NULL;
		tail = NULL;
		size = 0;
	}

	void insertAtFront(Patient* patient) {
		Node* newNode = new Node(patient);
		if (head == NULL) {
			tail = newNode;
		}
		else {
			newNode->setNextNode(head);
			head->setPreviousNode(newNode);
		}
		head = newNode;
		size++;
	}

    void append(Patient* patient)
    {
        Node* newNode = new Node(patient);

        if (tail == NULL)
        {
            head = newNode;
            tail = newNode;
            size++;
            return;
        }

        tail->setNextNode(newNode);
        newNode->setPreviousNode(tail);
        tail = newNode;
        size++;
    }


    LinkedList* search(std::string searchReference, int searchMode)
    {
        if (head == NULL) { 
            std::cout << "Sorry but the list is empty, nothing to see here\n"; 
            return NULL; 
        }

        LinkedList* results = new LinkedList;
        Node* currentNode = head;
        std::string searchComparator;


        while (currentNode != NULL)
        {
            switch (searchMode)
            {
            case 1:
                searchComparator = currentNode->getPatient()->getPatientID();
                if (searchComparator == searchReference)
                {
                    results->append(currentNode->getPatient());
                    return results;
                }
                break;

            case 2:
                searchComparator = currentNode->getPatient()->getFirstName();
                break;

            case 3:
                searchComparator = currentNode->getPatient()->getSicknessDescription();
                break;

            case 4:
                searchComparator = currentNode->getPatient()->getMedicineInformation();
                break;

            case 5:
                searchComparator = currentNode->getPatient()->getDoctorName();
                break;

            default:
                break;
            }

            if (searchComparator == searchReference)
            {
                results->append(currentNode->getPatient());
            }
            currentNode = currentNode->getNextNode(); 
        }

        //The function or pointer receiving this "results" linked list will need to use getHeadReference() to start printing the Linked list
        return results;
    }

    void displayList()
    {
        Node* currentNode = head;

        //Check if the list is empty
        if (currentNode == NULL)
        {
            std::cout << "The List is currently empty";
            return;
        }

        while (currentNode != NULL)
        {
            std::cout << currentNode->getPatient()->toString();
            std::cout << "-----------------------------------------\n";
            currentNode = currentNode->getNextNode();
        }

        return;
    }

    void displayInQueue() {
        Node* currentNode = head;
        while (currentNode != NULL)
        {
            std::cout << currentNode->getPatient()->getPatientID();
            std::cout << "->";
            currentNode = currentNode->getNextNode();
        }
        std::cout << std::endl;
    }

    void reverseDisplayInQueue() {
        Node* currentNode = tail;
        while (currentNode != NULL)
        {
            std::cout << currentNode->getPatient()->getPatientID();
            std::cout << "->";
            currentNode = currentNode->getPreviousNode();
        }
        std::cout << std::endl;
    }

    Node* getHeadReference() { return head; }

    int getSize() {
        return size;
    }
};





