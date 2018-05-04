# imt3673-project
IMT3673 Project - App Ball Game (Android, Java)

# Members
Adam Jammary - 771962  
Magnus W. Enggrav - 470917  
Michael Bråten - 470912  
Martin Bjerknes - 757948  

# Report
See imt3673_Project__Gravity_Ball.pdf in repo for report.

## App ball game - Game(3D/2D) (lab 3 expansion)
* Code: lab3-expansion
* Category: game

## Game idea
Our game is about guiding a ball towards a goal on obstacle filled levels using gravity.
The completion time of a level is timed, the achieved time is the score.
There are 3 tiers of times to beat on each level, meant to challenge the player.
Scores are added to a local leaderboard for each level and an opt in online leader board.

## Code organization 
We have organized our classes into appropriately named packages.
The varius menu activities are in the menu package. The game activity is in the main package.
Bitmaps for the levels are in res/raw and textures are in res/drawable. 

## Login/server side (Google Play Games Services)

Only our team members can test this for now, please see the report for more details on Google Play Games Services.

If you want to test it, you need to, send an email to Adam Jammary with your:

* Gmail account, so you can be added as a tester on Google Play Console.
* The SHA-1 key you use to build the APK, the key for debug builds can be obtained with (on Windows):


    keytool -exportcert -keystore %USERPROFILE%\.android\debug.keystore -list

## Linter Warnings:

#### These are the warnings showing in the Linting sections when running code inspection, and explanaitions for why we left them in/haven't dealt with them:

* Ball.drag is left non-local because we want the ball physics variables in one place.

* Block.type and .rect are left protected because classes that inherit from Block should have access if they want to use them.

* All linter suggestions about making things package private are purposefully ignored.

* Some "unused" resources are not directly referenced in the code (eg. levels) so the linter think they are unused.

* Image defined in density-independent drawable folder: For simplicity's sake we kept all drawables in one folder, but could probably have moved these to size-specific folders.

* "Overdraw: Painting regions more than once": The error is due to the fact that we draw custom backgrounds in menus, while the theme draws its own. It is intentional, but we could perhaps have used a custom theme without a background in it. 

* Block.TYPE_VALUES could be SparseIntArray instead of HashMap. Linter says its because of performance, but apparently it is not always more performant to use SIA over HM(according to Stack Overflow). We left it as HashMap because we didn't feel that it was neccessary to make the switch.
