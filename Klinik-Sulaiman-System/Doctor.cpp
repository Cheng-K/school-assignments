#include <iostream>
#include <string>
#include "Doctor.h"
#include "Node.h"
#include "LinkedList.h"
#include "Patient.h"
#include "Utility.h"
#include "PatientQueue.h"


Doctor::Doctor(PatientQueue* waitingList, LinkedList* historyList) {
    this->patientQueue = waitingList;
    this->historyList = historyList;
}

void Doctor::displayDoctorMenu() {
    char choice = 0;
    while (true) {
        std::cout << "Doctor menu " << std::endl;
        std::cout << "1. View Patient History List and Patient Waiting List" << std::endl;
        std::cout << "2. Treat Patients" << std::endl;
        std::cout << "3. Search Patients" << std::endl;
        std::cout << "Press any key to logout..." << std::endl;
        std::cout << "Select one of the options displayed above by entering the number : ";
        std::cin >> choice;
        std::cin.ignore(256, '\n');
        std::cout << std::endl;
        if (choice == '1')
            viewInfo();
        else if (choice == '2')
            treatPatient();
        else if (choice == '3')
            searchPatient();
        else
            break;
        system("cls");
    }
}

void Doctor::viewInfo()
{
    int choice = 0;

    while (choice != 5)
    {
        system("cls");
        std::cout << "=== Doctor Viewing Menu ===\n";
        std::cout << "1. View entire patient waiting list\n";
        std::cout << "2. View patient waiting list in page by page mode\n";
        std::cout << "3. View entire patient visit history list\n";
        std::cout << "4. View patient visit history list in page by page mode\n";
        std::cout << "5. Go Back to previous menu\n\n";
        std::cout << " Enter a number above: ";

        std::cin >> choice;
        if (std::cin.fail())
        {
            std::cout << choice;
            std::cin.clear();
            std::cin.ignore(256, '\n');
            continue;
        }
        std::cin.ignore(256, '\n');
        std::cout << std::endl;
        switch (choice)
        {
        case 1:
            if (patientQueue->getHeadReference() != NULL)
                patientQueue->displayList();
            else
                std::cout << "There are 0 patients in the waiting list." << std::endl;
            break;

        case 2:
            if (patientQueue->getHeadReference() != NULL)
                Utility::viewPatient(patientQueue);
            else
                std::cout << "There are 0 patients in the waiting list." << std::endl;
            break;

        case 3:
            if (historyList->getHeadReference() != NULL)
                historyList->displayList();
            else
                std::cout << "There are 0 patients in the history list." << std::endl;
            break;
        case 4:
            if (historyList->getHeadReference() != NULL)
                Utility::viewPatient(historyList);
            else
                std::cout << "There are 0 patients in the history list." << std::endl;
            break;
        case 5:
            break;

        default:
            std::cout << "\n\n WARNING: The input you have entered is not supported by the system, please select from the menu\n";
        }
        std::cout << std::endl << std::endl;
        system("pause");
    }
    return;
}

void Doctor::modifyPatient(Patient* patient)
{
    std::string medicineInfo;
    char changeSicknessDescription;

    std::cout << "----------------- Current Patient Details -----------------" << std::endl;
    std::cout << patient->toString() << std::endl << std::endl;
    std::cout << "Enter the medicine information prescribed for this patient: ";
    getline(std::cin, medicineInfo);

    patient->setMedicineInformation(medicineInfo);

    std::cout << "\nEnter [1] if you wish to modify the sickness description of patient or any other key to terminate this operation: ";
    std::cin >> changeSicknessDescription;
    std::cin.ignore(256, '\n');

    if (changeSicknessDescription == '1')
    {
        std::string newSicknessDescription;
        std::cout << "\nEnter the sickness description for this patient: ";
        getline(std::cin, newSicknessDescription);
        patient->setSicknessDescription(newSicknessDescription);
    }

    std::cout << "\nAll modification saved successfully\n";
}

void Doctor::treatPatient() {
    LinkedList* allPatient = historyList->search(name, 5);
    LinkedList* treatPatient = allPatient->search("", 4);
    char confirmation = '1';

    std::cout << "There are " << treatPatient->getSize() << " patients that need your attention to enter their medical information." << std::endl;
    if (treatPatient->getHeadReference() != NULL) {
        Node* currentPatient = treatPatient->getHeadReference();
        while (currentPatient != NULL && confirmation == '1') {
            std::cout << "\nWould you like to continue to enter patient medical information now ? Enter [1] for yes or any other key for no :";
            std::cin >> confirmation;
            std::cin.ignore(256, '\n');
            if (confirmation == '1') {
                system("cls");
                modifyPatient(currentPatient->getPatient());
                currentPatient = currentPatient->getNextNode();
            }
            else
                continue;
        }
    }
    std::cout << "Redirecting you back to main menu..." << std::endl << std::endl;
    system("pause");
}

void Doctor::searchPatient()
{
    int searchMode=0;
    std::string cont;
    std::string searchReference;
    while (searchMode != 5)
    {
        system("cls");
        std::cout << "=== Doctor Searching Menu ===\n";
        std::cout << "1. Search by Patient ID \n2. Search by Patient First Name \n3. Search by Sickness Description" <<
            "\n4. Medicine Information \n5. Go back\n\nPlease select an option to search for the patients' profile: ";
        std::cin >> searchMode;
        if (searchMode < 1 || searchMode>5 || std::cin.fail())
        {
            std::cin.clear();
            std::cin.ignore(256, '\n');
            std::cout << "\n\n WARNING: The input you have entered is not supported by the system, please select from the menu.\n";
            std::cout << std::endl << std::endl;
            system("pause");
            continue;
        }
        std::cin.clear();
        std::cin.ignore(256, '\n');
        if (searchMode==5)
        {
            continue;
        }
        else
        {
            std::cout << "\nPlease enter the patient details to be searched with: ";
            getline(std::cin, searchReference);
            LinkedList* resultsFromPatientQueue = patientQueue->search(searchReference, searchMode);
            LinkedList* resultsFromHistoryList = historyList->search(searchReference, searchMode);
            if (resultsFromPatientQueue->getHeadReference() == NULL && resultsFromHistoryList->getHeadReference() == NULL) {
                std::cout << "\nPatient(s) not found!\n";
            }
            else {
                if (resultsFromPatientQueue->getHeadReference() != NULL)
                {
                    resultsFromPatientQueue->displayList();
                }
                if (resultsFromHistoryList->getHeadReference() != NULL) {
                    resultsFromHistoryList->displayList();
                }
            }
        }
        std::cout << std::endl << std::endl;
        system("pause");
    }
    return;
}

std::string Doctor::getName() {
    return name;
}

void Doctor::setName(std::string name) {
    this->name = name;
}




