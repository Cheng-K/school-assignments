#pragma once
#include <iostream>
#include "PatientQueue.h"

class Nurse {
	PatientQueue* waitingList = NULL;

public:
	Nurse(PatientQueue* waitingList) {
		this->waitingList = waitingList;
	}
	void callPatient() {
		Patient* currentPatient = waitingList->getNextPatient();
		std::cout << currentPatient->toString() << std::endl;
	}
};