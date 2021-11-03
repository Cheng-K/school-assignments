#pragma once
# include <iostream>
# include "LinkedList.h"
# include "Node.h"
# include "Patient.h"


LinkedList::LinkedList() {
    head = NULL;
    tail = NULL;
    size = 0;
}

void LinkedList::insertAtFront(Patient* patient) {
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

void LinkedList::append(Patient* patient)
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


LinkedList* LinkedList::search(std::string searchReference, int searchMode)
{
    LinkedList* results = new LinkedList;
    Node* currentNode = head;
    std::string searchComparator;
    if (head == NULL) {
    }
    else
    {
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
    }

    //The function or pointer receiving this "results" linked list will need to use getHeadReference() to start printing the Linked list
    return results;
}

void LinkedList::displayList()
{
    Node* currentNode = head;
    while (currentNode != NULL)
    {
        std::cout << currentNode->getPatient()->toString();
        std::cout << "-----------------------------------------\n";
        currentNode = currentNode->getNextNode();
    }
}

void LinkedList::displayInQueue() {
    Node* currentNode = head;
    while (currentNode != NULL)
    {
        std::cout << currentNode->getPatient()->getPatientID();
        std::cout << "->";
        currentNode = currentNode->getNextNode();
    }
    std::cout << std::endl;
}

void LinkedList::reverseDisplayInQueue() {
    Node* currentNode = tail;
    while (currentNode != NULL)
    {
        std::cout << currentNode->getPatient()->getPatientID();
        std::cout << "->";
        currentNode = currentNode->getPreviousNode();
    }
    std::cout << std::endl;
}

Node* LinkedList::getHeadReference()
{
    return head;
}

int LinkedList::getSize() {
    return size;
}






