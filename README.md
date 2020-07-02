# ing-sw-2020-Paolini-Pellini-Sani

This software is designed after the game "Santorini".
All the copyrights are owned by the real authors of this game, this is just a simplified version made for learning purposes.
Do not use it for any non-learning purpose!

## Installation

Clone the project from this repository.

Once you have all the files you can access to the server settings in the Resources folder (ing-sw-2020-Paolini-Pellini-Sani\src\main\Resources). In the server-settings.txt 
edit you parameters:
    
    Server IP: YOUR_SERVER_IP
    Server Port: YOUR_SERVER_PORT
   
After that you can generate the JARs, which will be put in the Deliverables folder, by simply calling Maven package.
The generated JARs are one for the server and one for the client.

NOTE: You can't modify your server parameters after you generate the JARs, so be sure to enter a valid IP and PORT or run the generation once again after you have correctly set them.

Default parameters are:
    
    Server IP: 127.0.0.1
    Server PORT: 12345

Now you are ready to play: simply access the Deliverables folder from your terminals and run
    
    java -jar server.jar
For the server, and

    java -jar client.jar 
    
For the client. 

## Usage

In the first stage, choose A to play with GUI or B to play with the CLI:
![image](https://github.com/PaoloSani/ing-sw-2020-Paolini-Pellini-Sani/blob/master/Deliverables/README_pics/Initial.PNG)
  
After that, enter you nickname and choose: 
1) **CREATE A NEW MATCH** / **NEW GAME** (CLI-GUI) If you want to play with your friends. The server will give you a game ID, give it to the other players.
2) **PLAY WITH YOUR FRIENDS** / **PLAY WITH FRIENDS** (CLI-GUI) To play an existing match. Simply enter the game ID you have been given.
3) **PLAY WITH STRANGERS** / **RANDOM GAME** (CLI-GUI) To play with other unknown players.

CLI show:
![image](https://github.com/PaoloSani/ing-sw-2020-Paolini-Pellini-Sani/blob/master/Deliverables/README_pics/CLI.png)

GUI show: 
![image](https://github.com/PaoloSani/ing-sw-2020-Paolini-Pellini-Sani/blob/master/Deliverables/README_pics/GUI.png)

## OBIETTIVI REALIZZATI
- Regole complete
- Implementazione socket
- CLI e GUI
- 2 FA: **Divinità avanzate** (Zeus, Charon, Triton, Poseidon e Hypnus) e **Partite Multiple**

## COVERAGE REPORT

## AUTHORS
_Giuseppe Paolini_  
_Riccardo Pellini_  
_Paolo Sani_  