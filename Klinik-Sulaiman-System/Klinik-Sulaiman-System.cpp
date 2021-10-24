// Klinik-Sulaiman-System.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include "LinkedList.h"
#include "Node.h"
#include "Patient.h"
#include "PatientQueue.h"
#include "Utility.h"
#include "Doctor.h"
#include "Nurse.h"



int main()
{

	PatientQueue* p = new PatientQueue();
	Doctor* d = new Doctor(p);
	Nurse* n = new Nurse(p);
	for (int i = 0; i < 3; i++) {
		Patient* a;
		a = n->createPatient();
	/*	if (i % 2 == 0)*/
		/*a = new Patient("a" + std::to_string(i), "a", "a", "a", "a","a", true);*/
		//else
		//	a = new patient("a" + std::to_string(i), "a", "a", "a", "a", false);
		p->insertPatient(a);
	}
	p->displayInQueue();
	//d->viewInfo();
}

