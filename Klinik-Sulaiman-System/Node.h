#pragma once

class Patient;

class Node {
	Node* previousNode;
	Patient* patient;
	Node* nextNode;

public :
	Node(Patient* patient);
	Node* getPreviousNode();
	void setPreviousNode(Node* newPreviousNode);
	Node* getNextNode();
	void setNextNode(Node* newNextNode);
	Patient* getPatient();
};
