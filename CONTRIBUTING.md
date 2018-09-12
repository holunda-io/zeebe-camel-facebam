# Contributing


This project relies on two projects that are not yet finally published:

* https://github.com/zeebe-io/spring-zeebe
* https://github.com/holunda-io/camel-zeebe

its best to clone those projects and build/publish to the local maven repo.


    # spring-zeebe: 
    mvn clean install
    
    # camel-zeebe: 
    gradle clean build publishToMavenLocal

## Run

The application.yaml-configuration paths for in/outbox depend on the environment variable
`ZEEBE_CAMEL_FACEBAM_HOME` pointing to the project root.


WARNING: I didn't get .env with run dashbord running.
So currently, in each run dashboard configuration add an `Override Parameter`
* ZEEBE_CAMEL_FACEBAM_HOME | <PATH_TO_PROJECT_ROOT>

~~_I found that it's easiest to achieve when you have IDEA with the [.env-plugin](https://plugins.jetbrains.com/plugin/7861-env-file) 
 installed and just put `ZEEBE_CAMEL_FACEBAM_HOME=/home/galinskij/IdeaProjects/holisticon/zeebe-camel-facebam` in `<PROJECT_ROOT>/.env`
 You can use the `.env.tmpl` provided, just copy and replace the path_.
 _If you are not using IDEA+env, make sure the HOME variable is set on system properties._~~


## Links

* [Banner Generator](http://patorjk.com/software/taag/#p=display&f=Sub-Zero&t=%20FaceBam%0A%20%20thumb%0A%20%20%20nailer)

* camel-file: 
  * http://camel.apache.org/file2.html
  * http://www.giuseppeurso.eu/en/consuming-files-from-folders-with-apache-camel/
  * http://fabian-kostadinov.github.io/2016/01/10/reading-from-and-writing-to-files-in-apache-camel/
* camel typeconverters
  * http://camel.apache.org/type-converter.html
* camel json:
  * http://camel.apache.org/json.html
* camel base64: 
  * http://camel.apache.org/base64.html
* camel spring boot: 
  * http://camel.apache.org/spring-boot.html

* spring boot with kotlin: 
  * https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-kotlin.html
  
  
* camel twitter:
  * http://camel.apache.org/twitter.html


* java image processing:
  * https://www.geeksforgeeks.org/image-processing-java-set-10-watermarking-image/