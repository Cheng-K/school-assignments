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



int main() {
	PatientQueue* p = new PatientQueue();
	Doctor* doctor = new Doctor(p);
	Nurse* nurse = new Nurse(p);
	std::string username;
	std::string password;
	char loginAsDoctor;
	int loginStatus = 0;
	while (true) {
		std::cout << "Login Page" << std::endl;
		std::cout << "Username : ";
		getline(std::cin, username);
		std::cout << "Password : ";
		password = Utility::getPassword();
		std::cout << "\nEnter 1 to login as doctor, otherwise enter any key to login as nurse :";
		std::cin >> loginAsDoctor;
		std::cin.ignore(256, '\n');
		loginStatus = Utility::login(username,password,loginAsDoctor);
		system("cls");
		if (loginStatus == -1) {
			std::cout << "Login failed. Invalid credentials provided. Please try again." << std::endl;
			system("pause");
		}
		// Doctor
		else if (loginStatus == 0) {
			char choice = 0;
			while (true) {
				Utility::displayDoctorMenu();
				std::cin >> choice;
				cin.ignore(256, '\n');
				if (choice == '1')
					doctor->viewInfo();
				else if (choice == '2') {
					// search and modify patients ("treat patient")
				}
				else if (choice == '3') {
					// search patient
				}
				else {
					break;
				}
			}
		}
		else {
			//nurse
			while (true) {

			}
		}
		system("cls");
	}
	return 0;

}

int test() {
	PatientQueue* p = new PatientQueue();
	Doctor* d = new Doctor(p);
	Nurse* n = new Nurse(p);
	for (int i = 0; i < 1; i++) {
		Patient* a;
		/*a = n->createPatient();*/
		if (i % 2 == 0)
			a = new Patient("a" + std::to_string(i), "a", "a", "a", "a", "a", true);
		else
			a = new Patient("a" + std::to_string(i), "a", "a", "a", "a", "a", false);
		n->addPatient(a);
	}
	p->displayInQueue();
	n->deletePatient();
	p->displayInQueue();

	d->viewInfo();
	return 0;
}
