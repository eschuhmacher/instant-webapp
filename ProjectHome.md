## Disclaimer! ##
Update 23. August 2013:
  * Please note that the Project is currently not actively maintained while it indeed needs some maintenance here and there (upgrade of used libraries etc.)
  * I still leave the project online and hope to find the time to do the required maintenance sooner or later


<br />

## Introduction ##
Instant Web App (IWA) allows you to quickly create web applications that run in both Google App Engine (GAE) **and** in an traditional servlet container (like Apache Tomcat) in a very simple way.<br />
It provides the following functionality:
  * [Module Concept](http://code.google.com/p/instant-webapp/wiki/ModuleConcept) with Views/Presenters and Navigation Menu
  * Authentication (Login, including "lost password" emailing)
  * [Authorization](http://code.google.com/p/instant-webapp/wiki/AuthorizationConcept) (Users, Roles and Rights)
  * "Stand Alone Full Screen" Views
  * Internationalization (Multi Language via UI or URL, just append for example "?lang=en" to the URL)
  * JPA Persistence (altough we face some limitations here)
  * Email sending
  * Logging
  * and even if it is aimed to be a "use it as it is" framework, you can [customize](http://code.google.com/p/instant-webapp/wiki/FAQ) it's look

It is based on [Vaadin](http://vaadin.com) and [appfoundation](http://code.google.com/p/vaadin-appfoundation).

The very useful Appfoundation framework is an "implementation of frequently used tools in Vaadin applications" and follows a very flexible design ("The project consist of individual modules which are designed to be used separately or in combination with other modules"). **IWA** on the other hand is aimed to support these typical 80% percent of things one usually needs in a simple web application in a rather **pre-defined way** where one can start adding business logic right away.



See the **GettingStarted** page on how to use it, or check out the [live demo](http://iwa-sample-application.appspot.com/) (User "r", Password "r") of the sample app you can [download](http://code.google.com/p/instant-webapp/downloads/list) and use as your own starting point



<br />


<br />
## Limitations ##
Please be aware that IWA does have certain limitations, the most obvious is that is not very flexible by nature.

There are other [current issues](https://code.google.com/p/instant-webapp/issues/list) you are invited to contribute to overcome them.


<br />
## Screenshots ##

The Login Screen:

![http://instant-webapp.googlecode.com/svn/wiki/screenshots/Login.png](http://instant-webapp.googlecode.com/svn/wiki/screenshots/Login.png)

User Administration Screen:

![http://instant-webapp.googlecode.com/svn/wiki/screenshots/Users.png](http://instant-webapp.googlecode.com/svn/wiki/screenshots/Users.png)


