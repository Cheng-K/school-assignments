#pragma once

class Node;
class Patient;

class LinkedList {
protected : 
	Node* head;
	Node* tail;
	int size;

public :
	LinkedList();

	void insertAtFront(Patient* patient);

    void append(Patient* patient);
    

    LinkedList* search(std::string searchReference, int searchMode);
    

    void displayList();
   
    void displayInQueue();

    void reverseDisplayInQueue();

    Node* getHeadReference();
    Node* getTailReference();
  
    int getSize();
    static LinkedList* concatLists(LinkedList* a, LinkedList* b);

    
};





