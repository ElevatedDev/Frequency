![imgur](https://i.imgur.com/FN2dqsq.png)

# Frequency

This is an open source anticheat mainly developed and maintained by Elevated and Gson, with help from Dawson and the additional mental support from frap

The main purposes of this anticheat is to provide some learning grounds for new-comers into anticheats. This project is not recommended for
commercial use into a production server as it still has not been properly tested, but it should function properly for the most part. We would appreciate contributing to the project since this would expedite release, even if you're a novice. We'll do our best to help you setup and get started. 

## Simple Base Explanation

**Frequency:** This is where every method actually executes. It uses an enum for the instance to prevent the unnecessary creation of an object. It contains
the executors we are using for the packet-handler injection and alert-looping, and also some basic managers to handle data easier. 

**PacketWrapper:** This is a caching system for reflection that is used to get and grab the field values from packets. The wrapper child classes are only created
once before being sent to the packet checks to prevent constant creation of unnecessary objects.

**PlayerData/PlayerDataManager:** This is where all the data is stored for our checks. A data class is put in a concurrent hash-map with the player's uuid upon
joining the server and destroyed when the player quits the server. The player-data-manager handles everything just mentioned.

**PacketHandler:** This is where all the packets are being listened through a channel duplex handler through injecting into the player's pipeline upon joining.

**ProcessorManager:** This is the manager that stores all the processors. It uses an immutable class map to instance map to make instance creating simplier and
prevent the creation of unnecessary objects. More over, it looks nice when getting one of the processors for calling.

**ExemptManager/ExemptTypes:** The exempt manager handles the possible situations that a player should be exempted from in a check.

**Check:** There's a single check abstract used for its basic functions. It's not supposed to be used in actual check making. The ones you should be using are 
the classes "PacketCheck", "RotationCheck", "PostCheck", "PositionCheck". Upon certain actions by the player, the data is directed to those checks through the
check manager which works similarly to the processor. This system helps to make the check classes a lot cleaner, and also make it a little easier to create
certain checks.

**AlertManager:** This is where the alert messages, bans and broadcasts are being handled in. Everything is formatted from a base string, and the alert loop is
handled in a different thread using an executor. The actual alerts/violations are handled via a list, to allow us to clear old alerts every 9000ms.

## Working on Frequency
Here we will lay out all the important information you need to know getting setup to work on Frequency. Always be sure to follow the structure of the project. We want everything to remain as readable and clean as possible so there's no future confusion or any conflicts. 

### Getting Setup
1) Fork Frequency
2) Get your git environment setup on your computer.
3) Load the directory into your project as Maven.
4) You will find that the spigot dependency is missing. You can run BuildTools with a script Dawson made (https://www.dropbox.com/s/oj3v6arxfpfcb96/BuildTools.zip?dl=0). We have to do this because of licensing issues. However, it is really simple to use and there are instructions inside the ZIP file.

### Important Git Conventions
Always make sure that your master is up to date with the Frequency one. **NEVER** work on master. When making a feature, format the branch based on master with "feature/{name}" or "bugfix/{name}". Then you can submit the branch in a pull request. We will not be merging master to master. The whole point of this is to help prevent merging conflicts and allow for a cleaner git history. 

### How to report bugs/issues
Always make sure to give an in-depth explaination of anything that happened when you first encountered the issue. If you can, give a possible theory to what could be wrong, and finally provide some proof of the bug happening in the exact situation.
