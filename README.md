# Lab 4: Multithreaded Movie Recommender
Due: November 9, 2018, 11:59pm

This lab builds on top of lab 3.  In this lab, you will make your movie recommender multithreaded.

You will practice the following:
- Writing classes that implement Runnable interface
- Using java.util.concurrent package to create a pool of threads and add your Runnable tasks to the corresponding work queue. 
- Writing synchronized methods that take care of situations where multiple threads access shared data
- Debugging multithreaded program

Start by clicking on the link with the starter code on Canvas (under assignment 4).

The starter code contains an input folder that contains two subfolders: smallSet and largeSet. Each of these subfolders contains:
- a file with movies (movies.csv for smallSet and movies.dat for largeSet - please check the format of movies.dat, it is slightly different than in lab 3).
- multiple ratings files in csv format (same as in lab 3, but there are now multiple files that contain ratings)
- queries.csv file that contains one query per line, and each query contains
   inputFolder, outputFolder, userId, numRecommendations

Your Driver class for this lab should take three command line arguments:
inputDirectory outputDirectory numThreads
where inputDirectory is directory that contains movie file, queries file and ratings files. Output directory is where your program should write recommendation files,
and numThreads is the number of threads you should use in your program.

expectedResults folder contains expected results for smallSet and largeSet.

## Requirements
You are allowed to use only the following methods from java's concurrent package:
- Executors.newFixedThreadPool
- shutdown from ExecutorService
- awaitTermination from ExecutorService

When you create a pool of threads using java.util.concurrent package, please use the newFixedThreadPool method:
Executors.newFixedThreadPool(numThreads);

Loading data:
Your program should create a runnable Worker for each ratings file in the given input folder. So each file is processed in parallel. 
To avoid excessive blocking, each worker should add ratings info to its own data structure. After it is done reading the file 
and the data has been added to the local data structure, the worker should merge it with the "main" data structure that stores ratings.
Note: this is where you need to take care of synchronization, otherwise you will have a situation with multiple threads reading and writing to the same data structure.

Processing queries:
Each line in the quieries file should be processed concurrently. So as your program reads each line from this file, create a Runnable worker 
that would compute recommendations for this particular query and write them to a file.

## Policies
I expect to see at least 5 meaningful commits (not counting my commits) for this lab, made over the course of at least 3 days.

Late submissions are not accepted. 

The lab must be completed individually, no collaboration is allowed.

You should be able to complete the lab without using the web. Instead, please use lecture notes, 
examples provided by the instructor, as well as instructor's and TA's office hours.

Each person might be asked to come in for a code review of the lab. Not being able to answer questions about the code or change it per requirements of the instructor,
will result in a 0 for the assignment.

