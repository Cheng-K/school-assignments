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
};
