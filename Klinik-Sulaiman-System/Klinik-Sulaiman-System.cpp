// Klinik-Sulaiman-System.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <string>
#include "Utility.h"
#include "LinkedList.h"
#include "Doctor.h"
#include "Nurse.h"
#include "PatientQueue.h"
#include "Patient.h"




Patient* INIT_PATIENTQUEUE[5] = { new Patient("PID6", "Hui Lin", "Loe", "Fever", "", "Ong", false),
	new Patient("PID7", "Benson", "Junior", "Migraine", "", "Ong", true),
	new Patient("PID8", "Chi En", "Chew", "Malnutrition", "", "Wong", false),
	new Patient("PID9", "Bruh", "Kong", "", "Infection", "Ong", true),
	new Patient("PID10", "Hua Iong", "Lee", "", "Prostate disease", "Wong", false) };

Patient* INIT_HISTORYLIST[5] = { new Patient("PID1", "Hui Lin", "Loe", "Fever", "", "Ong", false),
	new Patient("PID2", "Benson", "Junior", "Migraine", "", "Ong", true),
	new Patient("PID3", "Chi En", "Chew", "Malnutrition", "", "Wong", false),
	new Patient("PID4", "Bruh", "Kong", "", "Infection", "Ong", true),
	new Patient("PID5", "Hua Iong", "Lee", "", "Prostate disease", "Wong", false) };

int INIT_HISTORYTIME[5][3] = {
	{9,01,01},
	{9,30,59},
	{10,05,0},
	{13,00,00},
	{16,55,0},
};


int main() {
	PatientQueue* p = new PatientQueue();
	LinkedList* h = new LinkedList();
	Doctor* doctor = new Doctor(p,h);
	Nurse* nurse = new Nurse(p,h);
	
	for (int i = 0;i < 5; i++)
	{
		p->insertPatient(INIT_PATIENTQUEUE[i]);
		std::cout << "Memory of patient : " << INIT_HISTORYLIST[i] << std::endl;
		INIT_HISTORYLIST[i]->setTime(INIT_HISTORYTIME[i][0], INIT_HISTORYTIME[i][1], INIT_HISTORYTIME[i][2]);
		h->insertAtFront(INIT_HISTORYLIST[i]);
		
	}
	//system("cls");
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
		loginStatus = Utility::login(username,password,loginAsDoctor,doctor,nurse);
		system("cls");
		if (loginStatus == -1) {
			std::cout << "Login failed. Invalid credentials provided. Please try again." << std::endl;
			system("pause");
		}
		// Doctor
		else if (loginStatus == 0) {
			doctor->displayDoctorMenu();
		}
		else {
			//nurse
			nurse->displayNurseMenu();
			
		}
		system("cls");
	}
	return 0;

}



int test() {

	//doctor* d = new doctor(p);
	//nurse* n = new nurse(p);
	//for (int i = 0; i < 3; i++) {
	//	patient* a;
	//	a = n->createpatient();
	//	if (i % 2 == 0)*/
	//	a = new patient("a" + std::to_string(i), "a", "a", "a", "a","a", true);*/
	//	else
	//		a = new patient("a" + std::to_string(i), "a", "a", "a", "a", false);
	//	p->insertpatient(a);
	//}
	//p->displayinqueue();
	//d->viewinfo();
	PatientQueue* p = new PatientQueue();
	LinkedList* h = new LinkedList();
	Doctor* doctor = new Doctor(p, h);
	Nurse* nurse = new Nurse(p, h);

	Patient* patient1 = new Patient("PID1", "Hui Lin", "Loe", "Fever", "", "Ong", false);
	Patient* patient2 = new Patient("PID2", "Benson", "Junior", "Migraine", "", "Ong", true);
	Patient* patient3 = new Patient("PID3", "Chi En", "Chew", "Malnutrition", "", "Wong", false);
	Patient* patient4 = new Patient("PID4", "Bruh", "Kong", "", "Infection", "Ong", true);
	Patient* patient5 = new Patient("PID4", "Hua Iong", "Lee", "", "Prostate disease", "Wong", false);
	Patient* patientArr[5] = { patient1,patient2,patient3,patient4,patient5 };
	for (int i = 0;i < 5 ; i++)
	{
		nurse->addPatient(patientArr[i]);
	}
	return 0;
}
