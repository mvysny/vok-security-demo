[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://github.com/mvysny/vok-security-demo/actions/workflows/gradle.yml/badge.svg)](https://github.com/mvysny/vok-security-demo/actions/workflows/gradle.yml)

# Vaadin-on-Kotlin Security Demo for Vaadin

Demonstrates the security aspect of the Vaadin-on-Kotlin framework. For a general information on
VoK Security please head to the [vok-security module documentation](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/README.md).

The project [online demo](https://v-herd.eu/vok-security-demo).

# Preparing Environment

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

# About the application

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
