TestClient
==========

Test client for Account Service application. Used JDK 1.8.

Client generates random amounts for random id (for addAmount method) or random id (for getAmount method) in one or several threads.
Parameters (count of readers, count of writers, id range, service ip and port) are set through the console or from file config.txt.
Simultaneously can run multiple clients on one or more computers.

Service commands:
"help" - show commands of client;
"conf" - show configuration of client;
"cnct" - connect to server;
"run" - create threads;
"dscnct" - stop threads and disconnect client;
"setr" - set count of readers;
"setw" - set count of writers;
"setrng" - set range of idList;
"setip" - set IP of service;
"setport" - set port of service;
"stadd" - get statistic for getAmount() method;
"stget" - get statistic for addAmount() method;
"rstst" - reset statistic of service;
"exit" - close client.

Have fun.
