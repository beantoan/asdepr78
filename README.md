### SETUP
1. Create a MySQL database
1. Modify datasource information at `application-development.properties`  
1. Define environment variable or create file `setenv.sh` (linux) / `setenv.bat` (windows) in the folder bin of tomcat with the following content:
    >-Dspring.profiles.active=development  
  
### RUN
1. Build .war file to deploy on Tomcat as a web application  
2. Or run class `it.unical.asde.pr7.Pr7Application` as an usual Java Application