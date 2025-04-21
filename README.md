<p align="center">
  <img src="logo.png" alt="Logo" width="150">
</p>

# SimPraFy
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
  - Reference Length
  - Reference String
  - Frames

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
* **File Saving**: Users can save a file (PDF or PNG) showing the output of the algorithm chosen.

# Snapshots
* Lobby <br/><br/>![image](https://github.com/user-attachments/assets/b71cc52b-3c69-4c14-8d59-9f23fcfb9ac0)<br/><br/>
* Data Input Methods<br/><br/>![image](https://github.com/user-attachments/assets/01cd5346-36cc-47b5-9c9a-86811607f8d9)<br/><br/>![image](https://github.com/user-attachments/assets/2a8b0969-cfdc-4167-a7f0-810c8ad2416c)<br/><br/>![image](https://github.com/user-attachments/assets/dfe7605e-2087-415f-a12c-0ebdb32760e3)<br/><br/>
* Algorithm Selection Screen<br/><br/>![image](https://github.com/user-attachments/assets/127c95d8-6b22-41be-937b-25ccec3eaa01)<br/><br/>
* Sample (FIFO: First-In, First-Out) Screen<br/><br/>![image](https://github.com/user-attachments/assets/9e8a031b-00bb-4c10-9084-160080cc4012)<br/><br/>
* Sample (All Algorithms) Screen<br/><br/>![image](https://github.com/user-attachments/assets/e2b1f994-ce68-4663-b1d2-ad24dbf20a28)<br/><br/>![image](https://github.com/user-attachments/assets/f2f70a72-f5d1-47e5-b715-75336bcc7ba1)<br/><br/>












