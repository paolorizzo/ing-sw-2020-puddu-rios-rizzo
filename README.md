# Prova finale di Ingegneria del Software 2020
## Gruppo GC04

- ### 887164 Francesco Puddu ([@francescopuddu](https://github.com/francescopuddu))
- ### 889117 Federico Rios ([@federico-rios](https://github.com/federico-rios))
- ### 890681 Paolo Rizzo ([@PaoloRizzo](https://github.com/PaoloRizzo))



## Implemented functionalities 

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Advanced Gods | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Undo | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |



## Test coverage 

| Package | Class | Method | Line |
|:-----------------------|:------------------------------------|:-----------------------|:------------------------------------:|
| Model | 100% | 97% | 95% |
| View | 22% | 20% | 13% |
| Controller | 100% | 100% | 96% |
| Exception | 100% | 100% | 100% |
| Observation | 100% | 65% | 55% |

(Total number of tests: 174)



## How to generate the JAR archives from terminal
1. Move to the project folder
2. Run the command 
```
mvn package 
```
3. Locate the generated JAR files in the /target folder



## How to run the application
The generated JAR ships all 3 applications, that can be accessed as follows:

### Server
Remember to specify the port number. 
It is recommended to run this as Administrator, to avoid permissions problems when writing log files for persistence. 
```
java -jar CG04-1.0-SNAPSHOT-jar-with-dependencies.jar -server 12345
```
### CLI
The WSL terminal in full screen mode is recommended for a proper use. 
```
java -jar CG04-1.0-SNAPSHOT-jar-with-dependencies.jar -cli
```
### GUI
Takes no arguments. 
If run in a non-Windows OS, it's recomended to install the "FORTE" font, available in the /deliveries folder.  
```
java -jar CG04-1.0-SNAPSHOT-jar-with-dependencies.jar 
```
