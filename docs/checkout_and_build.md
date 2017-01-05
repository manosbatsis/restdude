---
layout: default
title: Home
description: Simple documentation template for Github pages
isHome: true
---
## Overview

To build Calipso you need a Java Development Kit version (1.)7, Git and Apache Maven. Eclipse (Java or JEE) IDE users need EGit and m2eclipse plugins, those are included in Eclipse Java/JEE Luna and above. Read bellow for instructions to checkout the source using eclipse or the command line.

## Contents

- [Checkout using Eclipse](#checkout-using-eclipse)
- [Checkout using the Command Line](#checkout-using-the-command-line)
- [Maven build](#maven-build)
- [Browse Calipso](#browse-calipso)
- [Browse the Database Console](#browse-the-database-console)
- [Use a custom database](#use-a-custom-database)
    

### Checkout using Eclipse

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/010_import_dialog.png)

Right-click in Eclipse's Package/Project Explorer and click Import, then choose Git > Projects from Git

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/011_clone_uri.png)

Select the "Clone URI" option, then click "Next"

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/012_import_from_git.png)

Enter https://github.com/abissgr/calipso-hub.git as the clone URI

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/014_local_dest.png)

Select your local files location (independent to your Eclipse workspace)

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/015_wizard.png)

Select the "Import as general project" wizard

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/016_project_name.png)

Use "calipso-hub" as the project name

![alt tag]({{ site.baseurl }}/assets/images/checkout_and_build/017_import_existing_maven.png)

Right click on the "calipso-hub" then click on "Import". Choose "Existing Maven projects". Browse to the project folder for eclipse to discover the maven submodules, then click "Finish".


### Checkout using the Command Line

See https://help.github.com/articles/importing-a-git-repository-using-the-command-line/
 
### Maven build 

[![Build Status](https://travis-ci.org/abissgr/calipso-hub.png?branch=master)](https://travis-ci.org/abissgr/calipso-hub)

To build Calipso you need a Java Development Kit(JDK7 ) and Apache Maven installed. 

1) Change to the root project directory:

    $ cd calipso-hub

2) Rename HOWTO.txt to dev.properties and (optionally) edit it: 

    $ mv HOWTO.txt dev.properties

3) Build the project 

    $ mvn clean install

4) Change to webapp module directory 

    $ cd calipso-hub-webapp

5) Start the Jetty server 

    $ mvn clean install jetty:run
    
Note: If you get a <code>[ERROR] No plugin found for prefix 'jetty' in the current project and in the plugin groups </code> you probably forgot step 4 above.

    
## Browse Calipso

After you complete the steps above, you can access Calipso using your web browser: 

    http://localhost:8080/calipso
    
## Browse the Database Console 

By default, Calipso uses H2, an in-memory database that works great for development.
A fresh copy is created with test data each time you start Jetty using mvn:run. 
The database console is available at: 

    http://localhost:8080/calipso/console/database/
    

## Use a custom database 

You can use a database like MySQL by commenting out the H2 database section in your dev.properties, 
then uncommenting the MySQL section. Other databases can be also be used.  
    
    
## Add a Service

See [How to implement CRUD, View and Search for a new Entity Model](scrud_howto.md)