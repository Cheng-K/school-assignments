#pragma once
# include "Patient.h"

class Node {
	Node* previousNode;
	Patient* patient;
	Node* nextNode;

public :
	Node(Patient* patient) {
		previousNode = NULL;
		nextNode = NULL;
		this->patient = patient;
	}

	Node* getPreviousNode() {
		return previousNode;
	}

	Node* setPreviousNode(Node* newPreviousNode) {
		previousNode = newPreviousNode;
	}

	Node* getNextNode() {
		return nextNode;
	}

	Node* setNextNode(Node* newNextNode) {
		nextNode = newNextNode;
	}

	Patient* getPatient() {
		return patient;
	}


};
