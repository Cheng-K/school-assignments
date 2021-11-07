# pragma once

class LinkedList;
class Doctor;
class Nurse;

class Utility {
public :
	static LinkedList* mergeSort(LinkedList* linkedList, int searchMode);
	static std::string getPassword(); 
	static void viewPatient(LinkedList* linkedList);
	static int login(std::string userName, std::string password, char isDoctor, Doctor* doctor, Nurse* nurse); 
	static bool stringNumber(const std::string str);
	static std::string* getDoctors();
};