#include <iostream>
#include <string>
#include "Patient.h"
#include "Utility.h"
#include "PatientQueue.h"

class Doctor
{
    PatientQueue* patientQueue = NULL;
public:
    std::string doctorName;
    std::string toString() { return doctorName; }

    Doctor(PatientQueue* patientQueue) {
        this->patientQueue = patientQueue;
    }

    void viewInfo() 
    {
        int choice = 0;

        while (choice != 3)
        {
            system ("cls");
            std::cout << "\t\t\t=== Doctor Menu ===\n";
            std::cout << "\t\t\t1. View waiting list\n";
            std::cout << "\t\t\t2. View patient visit history\n";
            std::cout << "\t\t\t3. Go Back to previous menu\n\n";
            std::cout << "\t\t\t Enter a number above: ";
        
            std::cin >> choice;
            if (std::cin.fail()) 
            {
                std::cin.ignore(256,'\n');
                continue;
            }

            switch (choice)
            {
            case 1:
                patientQueue->displayList();
                break;

            case 2:                
                Utility::viewPatient();
                break;

            case 3:
                std::cout << "Good bye!";
                break;

            default:
                std::cout << "\n\n\t\t\t WARNING: The input you have entered is not supported by the system, please select from the menu\n";
                break;
            }
            system("pause");
        }

        return;
    }

    void modifyPatient(Patient* patient) 
    {
        std::string doctorName;
        std::string medicineInfo;
        std::string changeSicknessDescription;

        std::cout << "Please enter doctor name: ";
        getline(std::cin, doctorName);


        std::cout << "Enter the medicine information prescribed for this patient: ";
        getline(std::cin, medicineInfo);
        

        patient->setDoctorName(doctorName);
        patient->setMedicineInformation(medicineInfo);


        std::cout << "\nEnter [1] if you wish to modify the sickness description of patient or any other key to terminate the operation: ";
        std::cin >> changeSicknessDescription;
        std::cin.ignore(256, '\n');

        if (changeSicknessDescription == "1") 
        {
            std::string newSicknessDescription;
            std::cout << "Enter the sickness description for this patient: ";
            getline(std::cin, newSicknessDescription);
            patient->setSicknessDescription(newSicknessDescription);
        }

        std::cout << "All modification saved successfully\n";
        return;
    }

};
