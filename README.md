# imt3673-project
IMT3673 Project - App Ball Game (Android, Java)

## App ball game - Game(3D/2D) (lab 3 expansion)
* Code: lab3-expansion
* Category: game

## Main idea

Create a game using lab 3's ball (using physics to move the ball with acceleration and collision).

Either OpenGL or native Android graphics libraries can be used.

* https://developer.android.com/training/graphics/opengl/environment.html

Requirements:
* There should be an objective within the game (if not it wouldn't be a game really, would it?) and something to interact with (maybe: levers, pressure plates, ramps, holes, walls etc).
* There should be a menu the app starts up on, where the user can select:
  * Play
  * Options
  * Exit
  * Possibly others?
* There should be a User Preferences screen:
  * The progress of a user should be able to be private/hidden.
  * Blocking of users/friends.
* The game should have Google Play integration.
* Scoreboard (local and public) and be able to, if they want to, save a score to it.
* Be able to add friends (via Google play) and see their progress with the game (which levels they are on/have completed, highscores) (IF Available).

## Optional:
* Local Multiplayer: Should display devices that's close and once connected can play together (possibly BlueTooth or LAN?).
* Global multiplayer: A user should be able to invite their friends to play with them (WiFi).
* Security on multiplayer: Could anyone tamper with the packets (overwrite data, events that happen and more)? Hijack a game session to join private parties/games they shouldn't have access too? Other vulnerabilities?




## Linter Warnings:

#### These are the warnings showing in the Linting sections when running code inspection, and explanaitions for why we left them in/haven't dealt with them:

* Ball.drag is left non-local because we want the ball physics variables in one place.

* Block.type, .rect and .corners are left protected because classes that inherit from Block should have access if they want to use them.

* LineSegment.b is left public because it should be publically available. LineSegment.a for example is used accessed outside LineSegment.

* All linter suggestions about making things package private are purposefully ignored.

* Some "unused" resources are not directly referenced in the code (eg. levels) so the linter think they are unused.

* ID "StartupMenu_googlePlayLeaderboard_btn" is missing from land layout because we have not kept this layout up-to-date due to the screen being locked to vertical mode on main menu.

* Image defined in density-independent drawable folder: For simplicity's sake we kept all drawables in one folder, but could probably have moved these to size-specific folders.

* "Overdraw: Painting regions more than once": The error is due to the fact that we draw custom backgrounds in menus, while the theme draws its own. It is intentional, but we could perhaps have used a custom theme without a background in it. 

* Block.TYPE_VALUES could be SparseIntArray instead of HashMap. Linter says its because of performance, but apparently it is not always more performant to use SIA over HM(according to Stack Overflow). We left it as HashMap because we didn't feel that it was neccessary to make the switch.


**I will place this into the README, as an explanation of the still-existing linter warnings (If anyone deals with any of these existing warnings, just remove the relevant bulletpoint from the README)**