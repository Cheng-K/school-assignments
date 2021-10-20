#pragma once
# include "LinkedList.h";
# include "Node.h";


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
			//append(patient)
		}
	}
};
