
# Real Time Systems with Java

##  Automated Passport Control System Simulation
##  Table of Contents

- [Overview](#overview)
	- [Scenario Formulation](#scenario-formulation)
	- [Implementation Difference](#implementation-difference)
- [Documentation & Screenshots](#documentation--screenshots)



## Overview
The main objective of this assignment is to investigate the implications of program design on program performance in real time systems. A test scenario is formulated and implemented in two different design patterns which are the **mediator** and **observer** design pattern while applying proper concurrent and real time system concepts in Java. 


### Scenario Formulation
![Scenario Formulation diagram](https://cheng-k.github.io/school-assignments/Real%20Time%20Systems/Scenario_Formulation.svg)

### Implementation Difference

| Mediator | Observer |
| --- | --- |
| All threads are controlled by a single main thread known as the mediator thread | Most threads can be observed by other threads for events triggered |
| *E.g.  Facial scanner and passport scanner threads need to wait for the instructions from the mediator thread to start operating, which is after the gate thread sends a closed entry gate event to the mediator thread* | *E.g. Facial scanner and passport scanner can directly observe the gate thread for closed entry gate event to start scanning for passport and facial data*


## Documentation & Screenshots

Full assignment report and methodology can be found starting [here](https://cheng-k.github.io/school-assignments/Real%20Time%20Systems/Assignment_Report.pdf#page=6) 

Benchmark results can be found starting [here](https://cheng-k.github.io/school-assignments/Real%20Time%20Systems/Assignment_Report.pdf#page=12)

