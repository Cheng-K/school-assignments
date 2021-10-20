class Node
{
public:
    Patient patient;
    Node* nextAddress;
    Node* previousAddress;

    Node() {}
    Node(Patient newPatient) { patient = newPatient; }

};


