# Frequency

This is an open source anticheat mainly developed and maintained by Elevated and Gson, with help from Dawson. 

The main purposes of this anticheat is to provide some learning grounds for new-comers into anticheats. This project is not recommended for
commercial use into a production server since this is in very early stages. We would appreciate contributing to the project since this would expedite release, even if you're a novice. We'll do our best to help you setup and get started. 

Simple Base Explaination: 

FrequencyAPI: This is where every method actually executes. It uses an enum for the instance to prevent the unnecessary creation of an object. It contains
the executors we are using for the packet-handler injection and alert-looping, and also some basic managers to handle data easier. 

PacketWrapper: This is a caching system for reflection that is used to get and grab the field values from packets. The wrapper child classes are only created
once before being sent to the packet checks to prevent constant creation of unnecessary objects.

PlayerData/PlayerDataManager: This is where all the data is stored for our checks. A data class is put in a concurrent hash-map with the player's uuid upon
joining the server and destroyed when the player quits the server. The player-data-manager handles everything just mentioned.

PacketHandler: This is where all the packets are being listened through a channel duplex handler through injecting into the player's pipeline upon joining.

ProcessorManager: This is the manager that stores all the processors. It uses an immutable class map to instance map to make instance creating simplier and
prevent the creation of unnecessary objects. More over, it looks nice when getting one of the processors for calling.

ExemptManager/ExemptTypes: The exempt manager handles the possible situations that a player should be exempted from in a check.

Check: There's a single check abstract used for its basic functions. It's not supposed to be used in actual check making. The ones you should be using are 
the classes "PacketCheck", "RotationCheck", "PostCheck", "PositionCheck". Upon certain actions by the player, the data is directed to those checks through the
check manager which works similarly to the processor. This system helps to make the check classes a lot cleaner, and also make it a little easier to create
certain checks.

AlertManager: This is where the alert messages, bans and broadcasts are being handled in. Everything is formatted from a base string, and the alert loop is
handled in a different thread using an executor. The actual alerts/violations are handled via a list, to allow us to clear old alerts every 9000ms.
