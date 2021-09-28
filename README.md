# Data Analysis App

## What does this application do?
This is a data analysis system that monitors all files inside path `${home}/data/in`. Every time it finds a new file with extension `.dat` that has a limited size of 500MB it reads its content and generates a report inside `${home}/data/out/{source_file_name}.done.dat`.

There are 3 kinds of data inside those source files. For each kind of data there is a different layout.
```
 Salesman data has the format id 001 and the line will have the following
    -> format: • 001çCPFçNameçSalary
Customer data has the format id 002 and the line will have the following
    -> format: • 002çCNPJçNameçBusiness Area
Sales data has the format id 003. Inside the sales row, there is the list of items, which is wrapped
by square brackets []. The line will have the following format:
    -> format: • 003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name
```
This application will process any type of data that follows these patterns, just make sure that the chunks of data are seperated by a new line or a white space. 

The output file summarize the following data:
```
* Amount of clients in the input file 
* Amount of salesman in the input file 
* ID of the most expensive sale 
* The worst salesman ever
```
## How to run this application
This application was built with Java 11, therefore it needs a JDK 11 installation in your local machine in order to run. You can have more information on how to download and configure a JDK 11 [here](https://www.oracle.com/java/technologies/downloads).
1. Download this repository and save it on a desired folder.
2. Open a terminal window and navigate to that folder.
3. Run the command `sudo chmod +x run.sh` in order to make `run.sh` script runnable.
4. Execute the script with `./run.sh`.

Note: This application was built to run in an infinite loop. If you want to kill it press `CTRL+C` in the terminal window that is running this application.
