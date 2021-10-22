#pragma once
# include "LinkedList.h";
# include "Node.h";
#include <string>


class PatientQueue : public LinkedList {
	Node* lastDisabledPatient;
public :
	void insertPatient(Patient* patient) {
		if (patient->isDisabled()) {
			if (lastDisabledPatient == NULL) {
				insertAtFront(patient);
				lastDisabledPatient = head;
			}
			else {
				Node* newNode = new Node(patient);
				newNode->setPreviousNode(lastDisabledPatient);
				newNode->setNextNode(lastDisabledPatient->getNextNode());
				lastDisabledPatient->setNextNode(newNode);
				lastDisabledPatient->getNextNode()->setPreviousNode(newNode);
				lastDisabledPatient = newNode;
			}
		}
		else {
			append(patient);
		}
	}

    Patient* getNextPatient()
    {
        Node* current;
        std::string input;
        if (head == NULL)
        {
            std::cout << "Patient queue is already empty, there are no patients to call." << std::endl;
            std::cout << "Press 'enter' to continue";
            getline(std::cin, input);
            return NULL;
        }
        current = head;
        if (head == lastDisabledPatient)
        {
            lastDisabledPatient = NULL;
        }
        head = head->getNextNode();
        if (head == NULL)
        {
            tail = NULL;
        }
        else
        {
            head->setPreviousNode(NULL);
        }
        return current->getPatient();
    }

    // Debug *
    int removePatient(std::string patientID)
    {
        Node* current;
        if ((head->getPatient()->getPatientID())== patientID)
        {
            current = head;
            head = head->getNextNode();
            head->setPreviousNode(NULL);
            if (current == lastDisabledPatient)
            {
                lastDisabledPatient = NULL;
            }
            delete current;
            return 1;
        }
        else
        {
            //current->setPreviousNode(head);
            current = head->getNextNode();
            while (current != NULL)
            {
                if (current->getPatient()->getPatientID() == patientID)
                {
                    current->getPreviousNode()->setNextNode(current->getNextNode());
                    current->getNextNode()->setPreviousNode(current->getPreviousNode());
                    if (current == lastDisabledPatient)
                    {
                        lastDisabledPatient = current->getPreviousNode();
                    }
                    delete current;
                    return 1;
                }
                else
                {
                    //current->setPreviousNode(current);
                    current = current->getNextNode();
                }
            }
            return 0;
        }
    }

};
