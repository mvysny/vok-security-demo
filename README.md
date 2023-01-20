[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://github.com/mvysny/vok-security-demo/actions/workflows/gradle.yml/badge.svg)](https://github.com/mvysny/vok-security-demo/actions/workflows/gradle.yml)

# Vaadin-on-Kotlin Security Demo for Vaadin

Demonstrates the security aspect of the Vaadin-on-Kotlin framework. For general information on
VoK Security please head to the [vok-security module documentation](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/README.md).

The project [online demo](https://v-herd.eu/vok-security-demo).

# Preparing Environment

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

# About the application

The application uses the username+password authorization, with users stored in an in-memory H2 SQL database
(the [User](src/main/kotlin/com/vaadin/securitydemo/security/User.kt) class). There are no
views that could be accessed publicly - the user must log in first, in order to see any part of the app.

There are two users pre-created by the [Bootstrap](src/main/kotlin/com/vaadin/securitydemo/Bootstrap.kt) class:

* The 'user' user with the password of 'user' and the role of `ROLE_USER`
* The 'admin' user with the password of 'admin' and two roles: `ROLE_ADMIN` and `ROLE_USER`

The [AppServiceInitListener](src/main/kotlin/com/vaadin/securitydemo/AppServiceInitListener.kt) configures
Vaadin to check authorization and redirects to the Login route if there's no user logged in.
The username and password are compared against the database. The `User` class takes advantage
of the [HasPassword](https://github.com/mvysny/vaadin-on-kotlin/blob/master/vok-security/src/main/kotlin/com/github/vok/security/simple/HasPassword.kt)
mixin which makes sure to store the passwords in a hashed form.

If the login succeeds, the user is then stored into the session (or, rather, the `LoginService` class
is stored in the session along with the currently logged-in user. This way, we can group all
login/logout functionality into single class). Then, the page is refreshed. This forces Vaadin
to create a new instance of the `MainLayout`. Since a non-null user is now in the session, the `MainLayout`
will not perform the re-route to the login view; instead it will show the application layout.

There are four views:

* The [WelcomeRoute](src/main/kotlin/com/vaadin/securitydemo/welcome/WelcomeRoute.kt) which is accessible by all logged-in users;
* The [UserRoute](src/main/kotlin/com/vaadin/securitydemo/user/UserRoute.kt) which is accessible by all users with roles `ROLE_USER` and `ROLE_ADMIN`
* The [AdminRoute](src/main/kotlin/com/vaadin/securitydemo/admin/AdminRoute.kt) which is accessible by users with the `ROLE_ADMIN` role only
