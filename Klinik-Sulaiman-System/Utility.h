# pragma once

class LinkedList;
class Doctor;
class Nurse;

class Utility {
public :
	static LinkedList* mergeSort(LinkedList* linkedList, int searchMode);


	/*
		Method : getPassword
		Parameter : None
		Description : Prompts user to enter password and masks the password input with * and returns the password as string.
	*/

	static std::string getPassword(); 
	static void viewPatient(LinkedList* linkedList);
	static int login(std::string userName, std::string password, char isDoctor, Doctor* doctor, Nurse* nurse); 
	
};