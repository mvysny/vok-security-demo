[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Build Status](https://travis-ci.org/mvysny/vok-security-demo-v10.svg?branch=master)](https://travis-ci.org/mvysny/vok-security-demo-v10)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Heroku](https://heroku-badge.herokuapp.com/?app=vok-security-demo-v10&style=flat&svg=1)](https://vok-security-demo-v10.herokuapp.com/)

# Vaadin-on-Kotlin Security Demo for Vaadin 10

Demonstrates the security aspect of the Vaadin-on-Kotlin framework. For a general information on
VoK Security please head to the [vok-security module documentation](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/README.md).

## Getting Started

To quickly start the app, make sure that you have Java 8 JDK installed. Then, just type this into your terminal:

```bash
git clone https://github.com/mvysny/vok-security-demo-v10
cd vok-security-demo-v10
./gradlew appRun
```

The app will be running on [http://localhost:8080/](http://localhost:8080/).

The app is running live on Heroku at [https://vok-security-demo-v10.herokuapp.com](https://vok-security-demo-v10.herokuapp.com).

## About the application

The application uses the username+password authorization, with users stored in an in-memory H2 SQL database
(the [User](src/main/kotlin/com/vaadin/securitydemo/User.kt) class). There are no
views that could be accessed publicly - the user must log in first, in order to see any part of the app.

There are two users pre-created by the [Bootstrap](src/main/kotlin/com/vaadin/securitydemo/Bootstrap.kt) class:

* The 'user' user with the password of 'user' and the role of `user`
* The 'admin' user with the password of 'admin' and two roles: `admin` and `user`

The [MainLayout](src/main/kotlin/com/vaadin/securitydemo/MainLayout.kt) is configured to show a full-screen
login form (provided for us by the Vaadin-on-Kotlin as [LoginForm](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-util-vaadin10/src/main/kotlin/com/github/vok/framework/flow/VokSecurity.kt) class).
The username and password are compared against the database. The `User` class takes advantage
of the [HasPassword](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/src/main/kotlin/com/github/vok/security/simple/HasPassword.kt)
mixin which makes sure to store the passwords in a hashed form.

If the login succeeds, the user is then stored into the session (or, rather, the `LoginManager` class
is stored in the session along with the currently logged-in user. This way, we can group all
login/logout functionality into single class). Then, the page is refreshed. This forces Vaadin
to create a new instance of the `MainLayout`. Since a non-null user is now in the session, the `MainLayout`
will not perform the reroute to the login view; instead it will show the application layout.

There are four views:

* The [WelcomeView](src/main/kotlin/com/vaadin/securitydemo/WelcomeView.kt) which is accessible by all logged-in users;
* The [UserView](src/main/kotlin/com/vaadin/securitydemo/UserView.kt) which is accessible by all users with roles `user` and `admin`
* The [AdminView](src/main/kotlin/com/vaadin/securitydemo/AdminView.kt) which is accessible by users with the `admin` role only
* The [UserProfileView](src/main/kotlin/com/vaadin/securitydemo/UserProfileView.kt) which shows info about the currently logged-in user and is therefore accessible by
  all logged-in users.

## Dissection of project files

Let's look at all files that this project is composed of, and what are the points where you'll add functionality:

| Files | Meaning
| ----- | -------
| [build.gradle](build.gradle) | [Gradle](https://gradle.org/) build tool configuration files. Gradle is used to compile your app, download all dependency jars and build a war file
| [gradlew](gradlew), [gradlew.bat](gradlew.bat), [gradle/](gradle) | Gradle runtime files, so that you can build your app from command-line simply by running `./gradlew`, without having to download and install Gradle distribution yourself.
| [.travis.yml](.travis.yml) | Configuration file for [Travis-CI](http://travis-ci.org/) which tells Travis how to build the app. Travis watches your repo; it automatically builds your app and runs all the tests after every commit.
| [Procfile](Procfile) | Configuration file for [Heroku](https://www.heroku.com/) which hosts the app. Heroku will wait for Travis to verify the build, then it will build a Tomcat bundle and run it inside of docker.
| [.gitignore](.gitignore) | Tells [Git](https://git-scm.com/) to ignore files that can be produced from your app's sources - be it files produced by Gradle, Intellij project files etc.
| [src/main/resources/](src/main/resources) | A bunch of static files not compiled by Kotlin in any way; see below for explanation.
| [logback.xml](src/main/resources/logback.xml) | We're using [Slf4j](https://www.slf4j.org/) for logging and this is the configuration file for Slf4j
| [db/migration/](src/main/resources/db/migration) | Database upgrade instructions for the [Flyway](https://flywaydb.org/) framework. Database is upgraded on every server boot, to ensure it's always up-to-date. See the [Migration Naming Guide](https://flywaydb.org/documentation/migrations#naming) for more details.
| [webapp/](src/main/webapp) | static files provided as-is to the browser. The project stylesheet is stored here, in the [styles.html](src/main/webapp/frontend/styles.html) file
| [src/main/kotlin/](src/main/kotlin) | The main Kotlin sources of your web app. You'll be mostly editing files located in this folder.
| [Bootstrap.kt](src/main/kotlin/com/vaadin/securitydemo/Bootstrap.kt) | When Servlet Container (such as Tomcat) starts your app, it will run the `Bootstrap.contextInitialized()` function before any calls to your app are made. We need to bootstrap the Vaadin-on-Kotlin framework, in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date. After that's done, your app is ready to be serving client browsers. Also creates two sample users, user/user and admin/admin
| [MainLayout.kt](src/main/kotlin/com/vaadin/securitydemo/MainLayout.kt) | The main UI of the app; typically contains a template UI code which guarantees unified look-and-feel of your app. You then typically provide a layout which will host the views as the user navigates througout the app. Shows the `LoginForm` if there is no user logged in.
| [WelcomeView.kt](src/main/kotlin/com/vaadin/securitydemo/WelcomeView.kt) | The view accessible by all logged-in users, shown when the user browses the root page.
| [UserView.kt](src/main/kotlin/com/vaadin/securitydemo/UserView.kt) | The view accessible by users with roles of `user` and `admin`
| [AdminView.kt](src/main/kotlin/com/vaadin/securitydemo/AdminView.kt) | The view accessible by users with roles of `admin` only.
| [User.kt](src/main/kotlin/com/vaadin/securitydemo/User.kt) | Contains the `User` entity which maps to the `User` database table. Also contains `LoginManager` - a stateful session-stored utility class which performs login/logout-related duties.
| [AccessDeniedView.kt](src/main/kotlin/com/vaadin/securitydemo/AccessDeniedView.kt) | Catches the `AccessRejectedException` and shows a security violation message.

# Development with Intellij IDEA Ultimate

The easiest way (and the recommended way) to develop Karibu-DSL-based web applications is to use Intellij IDEA Ultimate.
It includes support for launching your project in any servlet container (Tomcat is recommended)
and allows you to debug the code, modify the code and hot-redeploy the code into the running Tomcat
instance, without having to restart Tomcat.

1. First, download Tomcat and register it into your Intellij IDEA properly: https://www.jetbrains.com/help/idea/2017.1/defining-application-servers-in-intellij-idea.html
2. Then just open this project in Intellij, simply by selecting `File / Open...` and click on the
   `build.gradle` file. When asked, select "Open as Project".
2. You can then create a launch configuration which will launch the `web` module as `exploded` in Tomcat with Intellij: just
   scroll to the end of this tutorial: https://kotlinlang.org/docs/tutorials/httpservlets.html
3. Start your newly created launch configuration in Debug mode. This way, you can modify the code
   and press `Ctrl+F9` to hot-redeploy the code. This only redeploys java code though, to
   redeploy resources just press `Ctrl+F10` and select "Update classes and resources"
