# include <iostream>
# include "Node.h"
# include "Patient.h"


Node::Node(Patient* patient) {
	previousNode = NULL;
	nextNode = NULL;
	this->patient = patient;
}

Node* Node::getPreviousNode() {
	return previousNode;
}

void Node::setPreviousNode(Node* newPreviousNode) {
	previousNode = newPreviousNode;
}

Node* Node::getNextNode() {
	return nextNode;
}

void Node::setNextNode(Node* newNextNode) {
	nextNode = newNextNode;
}


Patient* Node::getPatient() {
	return patient;
}

