
# Concurrent Programming with Java Assignment

##  Airport Traffic Control Tower Scenario Simulation
## Table of Contents

- [Overview](#overview)
- [Problem Statement](#problem-statement)
- [Scenario Modelling](#scenario-modelling)
- [Documentation & Screenshots](#documentation--screenshots-of-program)


## Overview

This main objective of this assignment is to develop a concurrent Java application that simulates the given case study. The main intention of this assignment is not to merely evaluate the simulation program but requires the student to design and implement a concurrent program with best practices and free from any potential concurrency issues (e.g. deadlock, synchronization problems)

## Problem Statement

The management of an airport requires a simulation of the airport to evaluate their management efficiency. The simulation will simply run with text output describing the events as they occur in the airport and collect a minimal amount of statistical data. The Air Tower Controller should be automated in the simulation to direct the airplanes with some requirements :

-  All 10 airplanes are requesting for landing together but there are only one runway and four docks.
- Only 1 airplane can occupy the runway and one dock to prevent collision
- Each airplane should depart from the airport once successfully refill supplies at the airport after docking

## Scenario Modelling

Each airplane is thought to have dedicated threads to perform actions (e.g. landing, departing). These actions (threads) requires resources in order to execute (e.g. docking spot, runway) which is then managed and allocated by the air traffic controller (thread) and land traffic controller (thread). These two controller works simultaneously to direct the flow of airplane from landing to docking to departing. 


## Documentation & Report

Full assignment report and explanation can be found [here](https://cheng-k.github.io/school-assignments/Concurrent%20Programming/Assignment_Report.pdf#page=3). 


