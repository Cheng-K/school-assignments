// Klinik-Sulaiman-System.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include "LinkedList.h"
#include "Node.h"
#include "Patient.h"
#include "PatientQueue.h"
#include "Utility.h"
using namespace std;

int main()
{
	PatientQueue* p = new PatientQueue();
	for (int i = 0; i < 8; i++) {
		Patient* a;
		if (i % 2 == 0)
			a = new Patient("a" + std::to_string(i), "a", "a", "a","a", true);
		else
			a = new Patient("a" + std::to_string(i), "a", "a", "a","a", false);
		p->insertPatient(a);
	}
	p->reverseDisplayInQueue();
	p->displayInQueue();
	std::cout << "before remove" << std::endl;
	p->displayInQueue();
	std::cout << "remove patient" << std::endl;
	p->removePatient("a2");
	p->displayInQueue();
}

