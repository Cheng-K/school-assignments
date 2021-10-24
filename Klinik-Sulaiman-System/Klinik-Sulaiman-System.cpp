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
			a = new Patient("a" + std::to_string(i), "a", "a", "a", "a", true);
		else
			a = new Patient("a" + std::to_string(i), "a", "a", "a", "a", false);
		p->insertPatient(a);
	}
	p->displayInQueue();
	p->getNextPatient();
	std::cout << "After getting patient :" << std::endl;
	p->displayInQueue();
}

