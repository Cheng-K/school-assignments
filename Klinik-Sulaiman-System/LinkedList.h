class LinkedList
{
    int linkedListSize;
    Node* headNode;
    Node* tailNode;

    Node* getHeadReference() { return headNode; }

    //void setHeadReference(Node* newNode) {headNode = newNode;}

    void append(Patient patient)
    {
        Node* newNode = new Node(patient);

        if (tailNode = NULL)
        {
            headNode = newNode;
            tailNode = newNode;
            return;
        }

        tailNode->nextAddress = newNode;
        newNode->previousAddress = tailNode;
        tailNode = newNode;
        return;
    }

    LinkedList* search(std::string searchReference, int searchMode)
    {
        if (headNode == NULL) { std::cout << "Sorry but the list is empty, nothing to see here\n"; return; }

        LinkedList* results = new LinkedList;
        Node* currentNode = headNode;
        std::string searchComparator;


        while (currentNode != NULL)
        {
            Patient searchPatient;
            switch (searchMode)
            {
            case 1:
                searchComparator = currentNode->patient.patientID;
                if (searchComparator == searchReference)
                {
                    results->append(searchPatient);
                    return results;
                }
                break;

            case 2:
                searchComparator = currentNode->patient.firstName;
                break;

            case 3:
                searchComparator = currentNode->patient.sicknessDescription;
                break;

            case 4:
                searchComparator = currentNode->patient.medicineInformation;
                break;

            case 5:
                searchComparator = currentNode->patient.doctorName;
                break;

            default:
                break;
            }

            if (searchComparator == searchReference)
            {
                results->append(searchPatient);
                currentNode = currentNode->nextAddress;
            }
            else { currentNode = currentNode->nextAddress; }
        }

        //The function or pointer receiving this "results" linked list will need to use getHeadReference() to start printing the Linked list
        return results;
    }


};

