Kevin Vu (kkv263)
CS314 Data Structures
Octoberr 2015

Instructions to run file.

javac libtest.java
java libtest

Answers to Questions
7. How does BigO() of this sort compare to BigO() of
   destructive mergesort in class? What is the BigO() 
   of the garbage that is produced by this sort function?

A: The BigO() of both are nlog(n). The BigO() of the
   garbage produced by this sort function is 1 because
   it doesn't make any new lists to throw away and
   goes through the list recursively.
