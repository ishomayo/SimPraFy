<p align="center">
  <img src="icon.png" alt="Logo" width="150">
</p>

# AlgoriSim
This is a repository for the implementation of a Page Replacement Algorithm Simulator for a requirement in the course CMSC 125 - M.

# Description
SimPraFy is a Java-based page replacement algorithms simulator with a graphical user interface (GUI). It allows users to visualize and simulate various CPU scheduling algorithms, including:

* **FIFO**: First-In, First-Out
* **LRU**: Least Recently Used
* **OPT**: Optimal Page Replacement Algorithm
* **Second Chance Algorithm**
* **Enhanced Second Chance Algorithm**
* **LFU**: Least Frequently Used
* **MFU**: Most Frequently Used

# System Requirements
* Java Development Kit (JDK): JDK 21 or later
* Operating System: Windows, macOS, or Linux
* IDE (Optional): Visual Studio Code or any Java-compatible IDE

# Installation & Setup
## 1. Clone the Repository
If using Git, clone the repository:<br/>
```
git clone https://github.com/ishomayo/SimPraFy.git
```
Alternatively, download the source code and extract it to your preferred directory.
## 2. Navigate to the Project Directory (Your Git Projects Directory)
```
cd Users\yourUser\Desktop\Git\SimPraFy
```
## 3. Open a terminal or command prompt in the project directory and run:
```
java -jar SimPraFy.jar
```

# Formatting the File Input 
## (For User-defined Input from a Text File)
The application expects input files to be formatted as follows:
* Each line contains three space-separated integers in the following order:
  - Arrival Time
  - Burst Time
  - Priority

### Example File Format
```
10
5 6 17 6 20 9 10 13 20 11
8
```
This will read as: <br/>
```
Reference Length: 10
Reference String: 5 6 17 6 20 9 10 13 20 11
Frames: 8
```
_**Note: A sample .txt file is given as example named "fileInput_1.txt" and "fileInput_2.txt"**_
# Features
* **Graphical Interface:** Intuitive UI using Java Swing.
* **Scheduling Algorithms:** Simulates FIFO, LRU, OPT, Second Chance Algorithm, Enhanced Second Chance Algorithm, LFU, and MFU
* **Interactive Selection:** Users can navigate between different screens to choose and simulate algorithms.

# Snapshots
* Lobby 




