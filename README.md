# StudentGradesManagementSystem
Application manages students' grades. The app can be used by teachers and students as well. 

<h1>Description</h1>
This system is built with Java and JavaFx. For its development I used:
 <ul>
  <li> 
    Apache Derby - a relational database management system (RDBMS) that can be embedded in Java programs.
  </li>
  <li>
    Java Mail API - a platform-independent and protocol-independent framework to build mail and messaging applications. 
  </li>
  <li>
    Jfoenix - an open source java library, that implements Google Material Design using java components
  </li>
  <li>
    iText 5.5.8 - to convert HTML code in PDF format
  </li>
  </ul>
  
  
<h1>Usege</h1>
Three types of users can log in to the system:
- admin
- teacher
- student
A user logs into the system based on their username and password.

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/login.PNG)


For this application, there is only one administrator. He can do the following:
<br>
Add, delete or edit information about a student

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/edit.PNG)

Add, delete or edit information about a teacher

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/add.PNG)

See all the accounts (An account is created automatically when a student or a teacher is added)

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/users.PNG)

The main purpose of the system is to give grades to students. This task can be accomplished by user logged as teacher.

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/grades.PNG)

A teacher can also do the following:
<br>
Add, delete, edit details about homework

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/teme.PNG)

View statistics

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/statistics.PNG)

Generate reports

![Screenshot](https://github.com/teofanaenachioiu/StudentGradesManagementSystem/blob/master/screenshots/raport.PNG)


<h1>Libraries</h1>
* activation.jar 
* mail.jar
* derby.jar
* derbyclient.jar
* derbyrun.jar
* derbytools.jar
* itextpdf-5.5.8
* jfoenix-8.0.8
* controlsfx-8.0.1

<h1>Useful links</h1>
* https://db.apache.org/derby/integrate/plugin_help/derby_app.html
* http://faculty.sdmiramar.edu/jcouture/2014sp/cisc190/webct/manual/tutorial-derby-intellij.asp
* https://www.tutorialspoint.com/java/java_sending_email.htm
* https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
* https://www.youtube.com/watch?v=yvI9HTP54gI
* https://jar-download.com/artifacts/com.itextpdf/html2pdf/2.0.0/source-code
* https://www.flaticon.com/
* https://www.youtube.com/watch?v=yvI9HTP54gI
* https://github.com/jfoenixadmin/JFoenix
* https://developers.itextpdf.com/content/itext-7-jump-start-tutorial/installing-itext-7
* http://incepttechnologies.blogspot.com/p/javafx-tableview-with-pagination-and.html
