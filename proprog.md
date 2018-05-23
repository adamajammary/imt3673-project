# IMT3602 Professional Programming

A list of the name of the students in the team

* Magnus Enggrav (470917) (magneng@stud.ntnu.no)
* Adam Jammary   (771962) (adamaj@stud.ntnu.no)

## A list of links to any other repos connected to the project

https://github.com/adamajammary/imt3673-project

## Group discussion

### Strengths and weaknesses of languages we used in the project

#### Java for Android

We wrote everything in Java using Android Studio as this is the official language for native Android development. We could have used Kotlin, or even used C++ with NDK. Maybe if we had used OpenGL for rendering graphics, we may have used C++ with NDK and run everything in fullscreen, but then it would have been very cumbersome to make menu items and other GUI widgets. None of had any experience with Kotlin, so we ended up using Java since we already had experience with it. And since our game was entirely in 2D, and utilized menus and other UI components, going with native Java for Android was in our opinion the best choice.

#### Room Persistence Library VS SQLite

“Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.” When we looked for a way to store highscores locally on the android phone, the Room Persistence Library was one of the first things that popped up when googling “local database on android” It was also the one recommended on the https://developer.android.com/training/data-storage/room/ page, instead of directly using SQLite, Alot of the problems of using SQLite direcly is that you need to use alot of boilderplate code to convert between SQL queries and java data object. but when using the Room Persistence Library you can use annotations to reduce the boilerplate code. There are also the fact that with SQLite, if you database changes you have to update the affected SQL queries manually, this can be time time consuming and one could easily get errors. The Room persistence library takes care of and it was also easy to set up and use.

Reasearch links:

https://medium.com/mindorks/sqlite-made-easy-room-persistence-library-ecd1a5bb0a2c
https://medium.com/@anujguptawork/note-making-application-using-sqlite-vs-room-part-2-using-room-becf92672e29 

### How we controlled communication systems during development

We had all our meetings and discussions via Discord, it worked well. We already had experience with using Discord for meetings and discussions in previous courses, so it worked out well in this project as well.

### Use of version control systems, ticket tracking and branching

We used GitHub to host our GIT repository, and a combination of SourceTree and command line GIT tools for managing the GIT versioning.

GitHub has a perfectly fine issue tracking system that we used to create and track all our issues including bugs and features. GitHub even had their own Trello-alternative for managing project boards, and the issue was perfectly integrated with the project board, so we could just create an issue in Issues and then a card was available in the board.

We protected the master branch, and everyone had to make their own feature branches that they worked on. And when the feature was complete, they made a Pull Request, and someone else on the team had to review the code, and test to make sure everything worked as expected before merging the feature branch in to the master branch.

### Link to programming style guide including how to comment code.

**Google Java Style Guide**

https://google.github.io/styleguide/javaguide.html

We tried to follow the coding standards we saw in official Android tutorials. We made sure to run the “Analyze - Inspect Code” tool in Android Studio as often as possible, because it would have very good tips on how to improve the code.

We made sure to comment all public classes and methods as a minimum, since someone else will use the code we make, it’s important that the API (the public interface) is clearly documented. But we also tried to comment the private methods and other more complicated code logic that needed some additional explaining.

### Use of libraries and integration of libraries

#### Google Play Game Services

You can read more details about our implementation of the Google Play Games Services in Chapter 4.1 and 5.1 in the report “imt3673_Project__Gravity_Ball.pdf”, it’s located in the root of the GIT repo. We used Google Play Services for authentication using Google accounts, and Google Play Games Services for online leaderboards.

#### Room Database

We used The Room database to store highscores locally and display them in the LevelChooser.java activity.

https://developer.android.com/topic/libraries/architecture/room

### Professionalism in our approach to software development

While experimenting, we might to some extent have hacked together code to get something working. But before we committed the code, we always tried to make sure the code was as  organized and easy to read as possible.

We tried using variable and method names that were self-documenting, which means they explained what their purpose was. We also made sure all public methods were commented, so it would be easy for anyone else on the team to use the classes that someone else made.

We also tried to make the code as modular as possible, so it would be easy for everyone to integrate their individual code with someone else's code, and also to avoid too many merge conflicts by having everyone working on separate modules that didn’t affect the rest of the program too much.

We also used “smart commits” as supported by GitHub, we tried to label each commit with a “#ISSUE_NR” so that it would be easy to follow commits, issues and project board items, as they would all be linked by the issue number.

In addition to having informative commit descriptions, we also made sure to make detailed test instructions with screenshots when creating the pull request, this made it easy for the code reviewers to make sure the pull request worked as expected.

### Use of Code Review

Everyone had to make their own feature branches that they worked on, someone made a new branch with a name of the issue they worked on, others had a branch with their name that they continually kept up-to-date with the master branch, but in any case the feature branch was always separate from the master branch, and the master branch was protected from anyone committing code directly in without permission.

When a team member had finished working on the feature, they updated the branch with all changes from the master branch. If they had any merge conflicts, they had to first resolve all of them before requesting a Pull Request. After a Pull Request was made, someone else on the team reviewed the code, and tested to make sure everything worked as expected before merging the feature branch in to the master branch.

Most of the Pull Requests were accepted without much problems, but sometimes the solution didn’t work as expected on someone else's machine, even though it had worked on the developers machine. A good example of this was Pull Request #35:

https://github.com/adamajammary/imt3673-project/pull/35

## Individual discussion - Magnus

### What is good?

**StartupMenu.java**

https://github.com/adamajammary/imt3673-project/blob/master/app/src/main/java/com/imt3673/project/menu/StartupMenu.java

#### Quality of code written
Although the code is not very complicated it is organized well and uses self explanatory method and variable names. The methods themselves are short and easy to read, this avoids clutter and large complicated methods.

All string that are displayed are located in the /res/values/strings.xml this makes it easier if one were to implement I18n. It also keeps all strings in the same place, this makes it easier to locate and change the strings you want, it also gives the opportunity to share strings between different layouts if needed.

I did run a lint scan to check the structure of the code, and other than a simple typo in one of the variable names it did not display any warnings.

#### Quality of comments and coding style

The class is well documented using the JavaDoc style of commenting over different methods except for inherited methods that use “@Override”. The code also have more specific comments like “//” over code that does not have any specific method/variable names that gives a short explanation to why this sniped of code is implemented and to make it more readable for other group members.

#### Quality and relevance of commit comments in version control

Each commit message contains the issue number and a short explanation of what has been implemented. 
Commit message example:
https://github.com/adamajammary/imt3673-project/commit/a433fa684c9ec9915ad56fc8717a1deb11b3529e

```
 #4 Menu - Play, Options, Exit etc.
* Added Startup menu. with functional Play, Option and Exit buttons
* Added background image.
* Added logo.
* Added Option menu.
```

The pull request gives a more detailed description on how the code works. together with all the commits for this specific issue, this makes it is easier for other members of the team, to test ang look through the new code when they do a code review. 
ex:
https://github.com/adamajammary/imt3673-project/pull/16 

### What is bad?

**MainPage.java - private void displayWinScreen()**

https://github.com/adamajammary/imt3673-project/blob/5a22e801184e596295fb0de76216aa4b695a1959/app/src/main/java/com/imt3673/project/main/MainActivity.java

#### Quality of code written
The method is big and can be hard to read at first sight, and is not very organized. 

#### Quality of comments and coding style
Some parts of the method is commented and understandable. But some of the variable names can be better, to make it clearer what they are.

#### Refactoring

Gathered the code that has to do with displaying and animate of stars into a new method called animateSmallStars(), this make it easier to read and it is easier to do changes, when the code that belonges together is gathered in one method instead of being spread out in one big method with other pieces of code.

Did the same with the code that has do with displaying and changing color of times the player has beaten. It is called displayLevelTimes(). for the same reason as the description above.

I also moved the textview that displays the time when the player reaches goal in to the animateBigStar() method, since the time  is displayed inside the big animated star in the win screen layout and they kind of belong together..

Added final int GOLD, SILVER and BRONZE to use ase parameters in the two new methods to avoid using “magic “ numbers.

Organized the code a little so it is easier to read the "if" checks and added some small comments so it easier to read when glancing over the code.

**Commit:**

https://github.com/adamajammary/imt3673-project/commit/6169adf85bf4bd0c598fc3d43eb4e0c38d7ec8be

**Old File:**

https://github.com/adamajammary/imt3673-project/blob/5a22e801184e596295fb0de76216aa4b695a1959/app/src/main/java/com/imt3673/project/main/MainActivity.java

**New File:**

https://github.com/adamajammary/imt3673-project/blob/622c32d58a98bc9de7c91f3b307e859a70034192/app/src/main/java/com/imt3673/project/main/MainActivity.java

### A personal reflection about professionalism in programming

I research whenever I start to plan a project, to find the best way to do something, like coding styles that is most used in the industry for this type of project and what is the recommended technologies, libraries, language etc. to use for this type of project. I try to follow this to the best of my abilities, in case there is a chance that other people also will be working on it.

I always try to write the best code I can, by commenting well, write self-explaining variable/method names and making it as organized and easy to read as possible, for others and myself, if I were to look at the code again later. I do my best to learn from my mistakes either if I spot the mistakes myself, or others tell/comment on my code during a code review. I always try to absorb knowledge from people around me, either through discussions or looking at others code. I do my best to communicate well and be honest in a nice way, with fellow team members, to avoid conflicts either between team members but also to avoid merge conflicts if possible. 

I always try to write easy to understand commit messages and linking each commit to the issue I am working on, by adding “#issueNr”.  I also write good descriptions of what is implemented in pull requests so it is easier to test and review for other members on the team.


## Individual discussion - Adam

### What is good?

**GooglePlayService.java**

https://github.com/adamajammary/imt3673-project/blob/dc8b67e6e5d56238d0a88d79eba909edd194c49f/app/src/main/java/com/imt3673/project/services/GooglePlayService.java

#### Quality of code written

The class is written in a way that it will be easy for others to use it. It is modular, all management is handled within the class, and it has a clearly documented API (public interface). The methods are not too long and cluttered, all functionality exists in their own encapsulated method with a self-documenting method name and input parameters.

The code does not use “magic numbers”, but instead uses constants when referencing special numeric identifiers like 100 which by itself would not tell the reader anything of its purpose, but the constant name GOOGLE_SIGNIN_RESULT tells the reader all they need to know without having to care why the number 100 was chosen.

Resources that are used throughout the code are stored as class members, and resources that are only used locally or that needs to be reinitialized before use are only stored in the local context or scope of where it needs to be declared. This makes the code as optimized and organized as possible.

#### Quality of comments and coding style

All public methods are commented and documented using Javadoc style commenting, comments that start with /** and end with */. The parameters and return values are described, all though the method and parameter names are themselves self-documenting.

The flow of the code is also commented sometimes, this makes it easier when quickly glancing over the code to know what the different parts of the method does, without having to read the code itself. This is not absolutely necessary, but means that both programmers and non-programmers can read the code and understand its purpose.

#### Quality and relevance of commit comments in version control

Most commits are rather self-explaining:

https://github.com/adamajammary/imt3673-project/commits/master/app/src/main/java/com/imt3673/project/services/GooglePlayService.java

The below commit example contains the issue nr, the issue title and commit description:

_**#5 User Preferences screen - The progress of a user should be able to be private/hidden**_
_**- Linked options with the service, now it only uploads if the box is checked in options**_

### What is bad?

**MainActivity.java - protected void onCreate(Bundle savedInstanceState)**

https://github.com/adamajammary/imt3673-project/blob/fd6219d948705f1866a4192fc29f0dc1112580fc/app/src/main/java/com/imt3673/project/main/MainActivity.java

#### Quality of code written

The onCreate method should contain code that initializes the activity, and the current onCreate method does exactly that. But the method ended up very cluttered, as we added new initialization statements one after the other without organizing them very well.

#### Quality of comments and coding style

There are are some comments, but all statements initialize something, so the variable names and assignment operators are pretty self-explaining.

#### What would be a better implementation?

We should try to group initialize statements together based on what they initialize, and move them out to separate methods.

#### Refactored code

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.initMain();
		this.initGraphics();
		this.initManagers();
		this.loadResources();
		this.initCanvas();
	}

**Commit:**

https://github.com/adamajammary/imt3673-project/commit/5a22e801184e596295fb0de76216aa4b695a1959

**Old file:**

https://github.com/adamajammary/imt3673-project/blob/fd6219d948705f1866a4192fc29f0dc1112580fc/app/src/main/java/com/imt3673/project/main/MainActivity.java

**New file:**

https://github.com/adamajammary/imt3673-project/blob/5a22e801184e596295fb0de76216aa4b695a1959/app/src/main/java/com/imt3673/project/main/MainActivity.java

### A personal reflection about professionalism in programming

This has pretty much been summed up in the group discussion above, but I can sum up my own individual reflections below:

* I always tried to make sure the code was as organized and easy to read as possible.
* I tried using variable and method names that were self-documenting.
* I made sure all public methods were commented.
* I tried to make the code as modular as possible.
* I tried to label each commit with a “#ISSUE_NR” so that GitHub could link the commit with issues, pull requests and project boards.
* I also tried to add as detailed test instructions as possible to the pull requests.

Basically, I tried to program with the mindset that other people would read my code, and hopefully could understand my code so they finally could use my code. So whenever I found that my code was not self-explaining, I knew that other people would definitely have problems reading it. Sometimes it’s difficult to be completely objective and look at your own code from an external perspective, but I at least tried as much as possible to do so.
