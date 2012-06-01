Team 4069 LoEllen Robotics 2012 Robot
=====================================

Robot code is final. No further changes is required.

Robot code released under GPLv3.

Features
--------

Some things:

 1. Low-pass filter for drive train
 2. Precision mode for drive train
 3. Software correcting a mechanical issue causing the drive train 
    not to be straight.
 4. PID controller for shooter.
 5. Automatic ball detection/shooter.
 6. Autonomous mode includes shooting 2 balls from the key area at set RPM.

Known issue:

 1. PID integral term does not reset if a ball is shot and the RPM dips.
    This causes longer settling time for the next shot. However, if the
    button is released, the PID error term resets itself.

Build instructions
------------------

If you checked out the code here and import it into eclipse with the WPI
plugin installed. You will **not** be able to build. The reason is that
my WPI plugin installation broke somehow and I had to deploy/run via `ant`

Once you check out this repo, go into build.xml and remove the 25th and 27th
line. This 2 lines is geared towards my computer and will fail if you try
to deploy and run. 

You also need to create a file, under `src/frc/t4069/robots`. The name of 
the file is `Version.java`. Here's the content of this file (You can just
use this or modify it if you wish):

    package frc.t4069.robots;
    public final class Version {
      public static final String author = "Shuhao";
      public static final String version = "final";
    }

The reason for this file is that it's to differentiate which version and
which developer worked on this robot. SmartDashboard will print out the
version and the author, so there's no confusion.

Once that's done, save and hit FRC Deploy.

Alternatively, you could change those 2 lines to your respective sunspot
and wpilibj directories, and use `ant deploy run` to deploy and run the
code on the robot. There's a couple other capabilities as well such as 
`ant deploy` or `ant run` (I believe). Note, if you're under Linux, you
may need to do `sudo ant ....` instead of just `ant ...` if you encounter
any permission related issues.

Organization
------------

Code is organized in the package `frc.t4069.xxx`. Under `.utils` there 
are some simple utilities. Most of these are unused, with the exception
of `GameController` and `Logger`. `networking` was unused completely and 
it's not a stable version. So don't use that. That was created in 
conjuction with mediator, which could provide control of the robot without
FRC software (possibility of controlling with smartphone, but that requires
a lot more code that I didn't write).

Under `.robots` it's very simple. `RobotMap` maps all the signal ports. 
`The2012Robot`is an extension to the iterative robot. `.subsystems.` has
all the systems on the robot as its individual objects.

Additional Note
---------------

The code base consists of only about 500 lines of code. I know. It's very
short, especially after the reorganization of the code. 

I *wasn't* kidding when I say it's just a line. 

In fact, most of the time I was netting negative lines.
