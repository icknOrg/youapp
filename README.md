YouMeIBD
========

About
-----

YouMeIBD is a place for the IBD community to connect. The ultimate goal of YouMeIBD is to enrich and improve the life of people living with IBD. We connect users based on their interest, location and expertise. By doing so, we want to build up a vivid and intelligent swarm of patients to collectively find the right answers to questions that people share.

The Code is Open Source and all of it can be found in this repository. The Code is maintained by the ICKN Team (http://www.ickn.org) and Pull Requests are welcome. The project dates back to 2010/2011 when an idea for a platform integrated into Facebook was described. The original paper can be found here: http://www.ickn.org/documents/COINs11_YouApp.pdf. In the context of an international university course called the "COIN Seminar", the idea was implemented by various teams. At one point the name was changed to YouMeIBD, since there existed already an App called YouApp.

The application is currently under construction. A working demonstration can be found on the GalaxyAdvisors server: http://91.250.82.106/youapp

Screenshots
-----------

Homepage:

![Homepage](http://gigr.ch/youmeibd/homepage.PNG)

Timeline:

![Homepage](http://gigr.ch/youmeibd/timeline.PNG)

Soulmates:

![Homepage](http://gigr.ch/youmeibd/soulmates.PNG)

Profile:

![Homepage](http://gigr.ch/youmeibd/profile.PNG)

Installation
------------

1. Install Java JRE and JDK.

JRE Path: C:\Program Files\Java\jre1.8.0_45
JDK Path: C:\Program Files\Java\jdk1.8.0_45

2. Set the JAVA_HOME System Variable. To do that search for System Environment and pick Edit the System Environment Variables. Then click Environment Vaiables... and under user variables click new and enter JAVA_HOME and the JDK Path from Step 1.

3. Download and install Eclipse for Java EE developers.

4. Add the JDK to Eclipse by clicking Windows -> Preferences -> Java -> Installed JREs and search for the JDK Path from step 1. Make sure the newly added entry is checked.

5. Import the project downloaded from this repository by clicking File -> Import

6. Download a Tomcat server.

Tomcat Path: C:\Program Files\apache-tomcat-8.0.21

7. Add the Tomcat server to Eclipse: File -> New -> Other -> Server -> Next -> Apache -> Tomcat 8 -> Enter localhost as hostname and Tomcat as the server name -> Next -> Open the Tomcat Path -> Choose JDK 7 JRE -> Finish.

(8. Add a MySQL Connector to the Tomcat server. Add the mysql-connector.jar to Tomcat Path/lib.)

URL: http://www.mysql.com/downloads/connector/j/

9. Install XAMPP, make sure MySQL is installed.

XAMPP Path: C:\xampp

10. Open XAMPP and Start Apache and MySQL. Once both are running go to http://localhost/phpmyadmin -> Import -> Choose File -> Select dump.sql from this repository -> Go.

11. Open the file youapp/src/main/config/dev/datasource.properties and change the username and password according to your XAMPP installation (username: root, password: "").

12. To start the project right-click on the project -> Run As -> Run on Server.

YouApp Path: http://localhost:8080/youapp

13. Additionally, an Apache SOLR server can be installed.