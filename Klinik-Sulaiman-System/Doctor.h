#include <iostream>
#include <string>
#include "Patient.h"
#include "Utility.h"
#include "PatientQueue.h"

class Doctor
{
    PatientQueue* patientQueue = NULL;
    LinkedList* historyList = NULL;

    
public:
    std::string doctorName;
    std::string toString() { return doctorName; }
    Doctor(PatientQueue* waitingList, LinkedList* historyList) {
        this->patientQueue = waitingList;
        this->historyList = historyList;
    }

    void viewInfo()
    {
        int choice = 0;

        while (choice != 3)
        {
            system("cls");
            std::cout << "=== Doctor Menu ===\n";
            std::cout << "1. View waiting list\n";
            std::cout << "2. View patient visit history\n";
            std::cout << "3. Go Back to previous menu\n\n";
            std::cout << "Enter a number above: ";

            std::cin >> choice;
            if (std::cin.fail())
            {
                std::cin.ignore(256, '\n');
                continue;
            }

            switch (choice)
            {
            case 1:
                patientQueue->displayList();
                break;

            case 2:                
                Utility::viewPatient(patientQueue);
                break;

            case 3:
                break;

            default:
                std::cout << "\n\n WARNING: The input you have entered is not supported by the system, please select from the menu\n";
                break;
            }
            system("pause");
        }
        system("cls");
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

    void searchPatient()
    {
        int searchMode;
        std::string cont;
        std::string searchReference;
        system("cls");
        std::cout << "-----Patient Search Menu-----\n\n";
        std::cout << "1. Search by Patient ID \n2. Search by Patient First Name \n3. Search by Sickness Description" <<
            "\n4. Medicine Information \n5. Go back\n\nPlease select an option to search for the patients' profile: ";
        std::cin >> searchMode;
        std::cin.clear();
        std::cin.ignore(256, '\n');
        while (searchMode < 1 || searchMode>5)
        {
            std::cout << "\nInvalid input, please select an option shown in the menu above (1/2/3/4/5): ";
            std::cin >> searchMode;
            std::cin.clear();
            std::cin.ignore(256, '\n');
        }
        if (searchMode == 5)
        {
            return;
        }
        std::cout << "\nPlease enter the patient details to be searched with: ";
        getline(std::cin, searchReference);
        LinkedList* patientList = patientQueue->search(searchReference, searchMode);
        if (patientList->getHeadReference() != NULL)
        {
            patientList->displayList();
        }
        else
        {
            std::cout << "\nPatient not found!\n";
        }
        return;
    }
};

