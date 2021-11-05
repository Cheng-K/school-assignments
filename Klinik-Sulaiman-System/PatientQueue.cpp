# include <string>
# include "LinkedList.h"
# include "PatientQueue.h"
# include "Patient.h"
# include "Node.h"


void PatientQueue::insertPatient(Patient* patient) { 
    if (patient == NULL) {
        return;
    }
    if (patient->isDisabled()) {
        if (lastDisabledPatient == NULL) {
            insertAtFront(patient);
            lastDisabledPatient = head;
        }
        else {
            Node* newNode = new Node(patient);
            newNode->setPreviousNode(lastDisabledPatient);
            newNode->setNextNode(lastDisabledPatient->getNextNode());
            if (lastDisabledPatient->getNextNode() != NULL) {
                lastDisabledPatient->getNextNode()->setPreviousNode(newNode);
            }
            lastDisabledPatient->setNextNode(newNode);
            lastDisabledPatient = newNode;
            size++;
        }

    }
    else {
        append(patient);
    }
}

Patient* PatientQueue::getNextPatient()
{
    Node* current;
    if (head == NULL)
    {
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

int PatientQueue::removePatient(std::string patientID)
{
    Node* current;
    if ((head->getPatient()->getPatientID()) == patientID)
    {
        current = head;
        head = head->getNextNode();
        if (head != NULL)
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
        current = head->getNextNode();
        while (current != NULL)
        {
            // Error when deleting the last node !!!
            if (current->getPatient()->getPatientID() == patientID)
            {
                current->getPreviousNode()->setNextNode(current->getNextNode());
                if (current->getNextNode() != NULL) // added 
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
                current = current->getNextNode();
            }
        }
        return 0;
    }
}


