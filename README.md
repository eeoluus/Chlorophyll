# Chlorophyll
Chlorophyll is my first Android project, an offline scheduling app. :seedling: Still a work in progress, see [here](#future).


## About
Chlorophyll provides functionality to add recurring events and delete them. It checks daily for upcoming events; if there are any, it will show a notification to remind the user of them. 

All recurring events are displayed in a calendar widget, see the [demo video](#demo) below. To do this, I used the [Applandeo Material-Calendar-View](https://github.com/Applandeo/Material-Calendar-View) widget, the only non-Jetpack dependency of the project.

General dependencies include the following. See the Gradle build scripts for details.

- Android 11 (API level 30)
- Gradle 7.4

## Background
I would oftentimes forget to water my plants, hence the application. Apart from that, this is a hobby project for me to get started with Android and Kotlin in a fun way.

I mostly follow the [Android Basics in Kotlin](https://developer.android.com/courses/android-basics-kotlin/course) course and, for everything outside the scope of the course, [Android Developer Guides](https://developer.android.com/guide), resorting to Stack Overflow answers when needed. As for Kotlin, the official [docs](https://kotlinlang.org/docs/home.html) have been helpful.

### What I have learned so far

- [x] some Kotlin
- [x] OOP concepts
- [x] Android basics (activities, fragments, lifecycles, navigation)
- [x] how to show notifications
- [x] how to schedule alarms
- [x] how to do CRUD using an SQLite wrapper, Room

## Demo
https://user-images.githubusercontent.com/86976278/234864396-1ce2b071-6970-4b4b-bd6d-a3c3b7e3c03d.mp4

## Future

#### Planned features
 
- [ ] make alarm time customizable
- [ ] add a screen to display event specifics for a selected day

#### Fixed issues

- [x] system boot kills future alarms
- [ ] no support for horizontal views
- [ ] recurring events are only displayed  within a fixed period (30 occurrences each, starting from a fixed date)
