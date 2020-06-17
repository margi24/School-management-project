# School-management-project

# School-management-project

The purpose of this application is to make an efficient management system for schools. The app contains 3 user types: 
  - student
  - teacher
  - admin
  
 ### Workflow
  
Admin enters in the system and add it's students and teachers. Only after adding then the teachers and the students will be able to sign up into the app. 

After a student logged in on the main page he can see it's account details. If he navigates to the homework tab, hw will be able to see his grades.
After a teacher logged in he will see 3 different rooms:
  - Students - see students
  - Homework - see homeworks and also can add a homework(description, deadline week and given start date)
  - Grades 
On the Grades page the teache can see all the grades from students. On the add page he can add a grade for a student. For every passed week from the deadline, the student grade will decrese with a penalty point. If the student has 3 penaties, he'll get 1 grade. If the student is motivated for the weeks that have passed from the deadline, he ll get the grade given by the teacher. The teacher can also see reports by homework, student name, homeworks and class name or date on the reports page, and also can genarate a pdf bazed by those reports.

For developing the application I used Java language. 
Functionalities: 
  - save data in xml files 
  - use paginated repository 
  - make real time filtering 
  - use bcrypt for saving passwords
  - generates reports

![](video/ezgifcom-video-to-gif1.gif)




## admin account:admin,password:admin
## teacheri account:gabi,password:gabi
