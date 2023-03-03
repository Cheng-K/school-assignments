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
        std::cout << "4. Logout" << std::endl;
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
        else if (choice == '4')
            break;
        else {
            std::cout << "\n\nWARNING: The input you have entered is not supported by the system, please select from the menu\n\n";
            system("pause");
        }
        system("cls");
    }
}

void Doctor::viewInfo()
{
    int choice = 0;

    while (choice != 9)
    {
        system("cls");
        std::cout << "=== Doctor Viewing Menu ===\n";
        std::cout << "1. View entire patient waiting list\n";
        std::cout << "2. View patient waiting list in page by page mode\n";
        std::cout << "3. View entire patient visit history list (Descending order by visit time)\n";
        std::cout << "4. View patient visit history list in page by page mode\n";
        std::cout << "5. View sorted history list based on patient first name\n";
        std::cout << "6. View sorted waiting list based on patient first name\n";
        std::cout << "7. View sorted history list based on sickeness description\n";
        std::cout << "8. View sorted waiting list based on sickeness description\n";
        std::cout << "9. Go Back to previous menu\n\n";
        std::cout << "Enter a number above: ";


        std::cin >> choice;
        if (std::cin.fail())
        {
            std::cout << "\n\nWARNING: The input you have entered is not supported by the system, please select from the menu\n\n";
            system("pause");
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
            if (historyList->getHeadReference() != NULL)
                Utility::mergeSort(historyList, 1)->displayList();
            else
                std::cout << "There are 0 patients in the history list." << std::endl;
            break;

        case 6:
            if (patientQueue->getHeadReference() != NULL)
                Utility::mergeSort(patientQueue, 1)->displayList();
            else
                std::cout << "There are 0 patients in the history list." << std::endl;
            break;

        case 7:
            if (patientQueue->getHeadReference() != NULL)
                Utility::mergeSort(historyList, 2)->displayList();
            else
                std::cout << "There are 0 patients in the history list." << std::endl;
            break;

        case 8:
            if (patientQueue->getHeadReference() != NULL)
                Utility::mergeSort(patientQueue, 2)->displayList();
            else
                std::cout << "There are 0 patients in the history list." << std::endl;
            break;

        case 9:
            break;

        default:
            std::cout << "\n\nWARNING: The input you have entered is not supported by the system, please select from the menu\n";
        }
        std::cout << std::endl << std::endl;
        system("pause");
    }
    return;
}

void Doctor::modifyMedicineInformation(Patient* patient)
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

    std::cout << "There are " << treatPatient->getSize() << " patients that need your attention to enter their medicine information." << std::endl;
    if (treatPatient->getHeadReference() != NULL) {
        Node* currentPatient = treatPatient->getHeadReference();
        while (currentPatient != NULL && confirmation == '1') {
            std::cout << "\nWould you like to continue to enter patient medicine information now ? Enter [1] for yes or any other key for no :";
            std::cin >> confirmation;
            std::cin.ignore(256, '\n');
            if (confirmation == '1') {
                system("cls");
                modifyMedicineInformation(currentPatient->getPatient());
                currentPatient = currentPatient->getNextNode();
            }
            else
                continue;
        }
    }
    std::cout << "\nRedirecting you back to main menu..." << std::endl << std::endl;
    system("pause");
}

void Doctor::searchPatient()
{
    int searchMode = 0;
    std::string cont;
    std::string searchReference;
    while (searchMode != 5)
    {
        system("cls");
        std::cout << "=== Doctor Searching Menu ===\n";
        std::cout << "1. Search by Patient ID \n2. Search by Patient First Name \n3. Search by Sickness Description" <<
            "\n4. Search by Medicine Information \n5. Go back\n\nPlease select an option to search for the patients' profile: ";
        std::cin >> searchMode;
        if (searchMode < 1 || searchMode>5 || std::cin.fail())
        {
            std::cin.clear();
            std::cin.ignore(256, '\n');
            std::cout << "\n\nWARNING: The input you have entered is not supported by the system, please select from the menu.\n";
            std::cout << std::endl << std::endl;
            system("pause");
            continue;
        }
        std::cin.clear();
        std::cin.ignore(256, '\n');
        if (searchMode == 5)
        {
            continue;
        }
        else
        {
            std::cout << "\nPlease enter the patient details to be searched with: ";
            getline(std::cin, searchReference);
            LinkedList* resultsFromPatientQueue = patientQueue->search(searchReference, searchMode);
            LinkedList* resultsFromHistoryList = historyList->search(searchReference, searchMode);
            LinkedList* finalResults = LinkedList::concatLists(resultsFromPatientQueue, resultsFromHistoryList);
            if (finalResults->getHeadReference() == NULL) {
                std::cout << "\nPatient(s) not found!\n";
            }
            else {
                char modifyPatient = ' ';
                finalResults->displayList();
                std::cout << "\nWould you like to edit the information of any patient(s) displayed above ? " << std::endl;
                std::cout << "Enter [1] for yes or any other key for no : ";
                std::cin >> modifyPatient;
                std::cin.ignore(256, '\n');
                if (modifyPatient == '1') {
                    patientEditor(finalResults);
                }
            }
            std::cout << std::endl << std::endl;
            system("pause");
        }
        return;
    }
}

void Doctor::patientEditor(LinkedList* patientList) {
    char attribute = ' ';
    char traverseDir = ' ';
    Node* currentNode = patientList->getHeadReference();
    while (currentNode != NULL) {
        system("cls");
        std::cout << "----------- Patient Editor -----------" << std::endl << std::endl;
        std::cout << currentNode->getPatient()->toString() << std::endl;
        std::cout << "Would you like to modify this patient's record ? ";
        std::cout << "Press : " << std::endl;
        if (currentNode->getPreviousNode() != NULL)
            std::cout << "[0] to move to the previous patient" << std::endl;
        if (currentNode->getNextNode() != NULL)
            std::cout << "[1] to move to the next patient" << std::endl;
        std::cout << "[2] to stay at current patient and modify other attributes " << std::endl;
        std::cout << "[3] to exit the patient editor" << std::endl;
        std::cin >> traverseDir;
        std::cin.ignore(256, '\n');
        if (traverseDir == '0' && currentNode->getPreviousNode() != NULL) {
            currentNode = currentNode->getPreviousNode();
        }
        else if (traverseDir == '1' && currentNode->getNextNode() != NULL) {
            currentNode = currentNode->getNextNode();
        }
        else if (traverseDir == '3')
            return;
        else if (traverseDir == '2') {
            std::cout << "Press : " << std::endl;
            std::cout << "[0] to change patient's first name" << std::endl;
            std::cout << "[1] to change patient's last name" << std::endl;
            std::cout << "[2] to change patient's age " << std::endl;
            std::cout << "[3] to change patient's gender" << std::endl;
            std::cout << "[4] to change patient's phone number" << std::endl;
            std::cout << "[5] to change patient's address" << std::endl;
            std::cout << "[6] to change patient's sickness description" << std::endl;
            std::cout << "[7] to change patient's disability option" << std::endl;
            std::cout << "[8] to change patient's doctor name" << std::endl;
            std::cout << "[9] to change patient's medicine information" << std::endl;
            std::cout << "Any other key to go back" << std::endl;

            std::cin >> attribute;
            std::cin.ignore(256, '\n');
            if (attribute == '0' || attribute == '1' || attribute == '2' || attribute == '3' || attribute == '4' || attribute == '5'
                || attribute == '6' || attribute == '7' || attribute == '8' || attribute == '9') {
                currentNode->getPatient()->modifyRecord(attribute);
                std::cout << "All modifications are saved. Changes will be reflected on the next screen." << std::endl << std::endl;
            }
            system("pause");
        }
        else {
            std::cout << "Invalid operation. Please try again.";
            system("pause");
        }

    }
    std::cout << "There are no remaining patients that can be modified...";

}

std::string Doctor::getName() {
    return name;
}

void Doctor::setName(std::string name) {
    this->name = name;
}




