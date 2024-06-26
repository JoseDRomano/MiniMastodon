# MiniMastodon
In this project, we created a distributed system that could work like Mastodon's. We used both REST and SOAP procotols to build it - even tough we had some trouble ruuning the SOAP version bc of an error of Casts and we its still giving some headaches. In order to use the tester that was provided for us, read the README.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------
Make sure you have maven installed, java 17 and your docker is running.

To run the tester:

-mvn clean compile assembly:single docker:build

Linux / MacOS
sh test-sd-tp1.sh -image sd2223-trab1-59241-60055

Windows
test-sd-tp1.bat -image sd2223-trab1-59241-60055

Other Tester Options

Tester has some options that can be specified when running it that modify its behavior.

-test <num> : Allows to omit the execution of some tests. For example, if you pass the value -test 2b, the tests will start on test 2b. This option is useful when you have already verified that your work works correctly up to a given test and you are fixing errors in a specific test.

-sleep <seconds> : Allows you to decrease the waiting time between launching containers with your work and starting the tests. You can adjust this value depending on the capacity of your computer and the operations you are doing in the initialization phase.

-log OFF|ALL|FINE|FINEST : Allows you to control the level of messages generated by the program. For example, by using the -log FINE option, the program will indicate all the operations it is doing to your system, indicating the received and expected messages, as shown in the following image:

-textsize <len> : Allows you to indicate the maximum text size of the messages created.
