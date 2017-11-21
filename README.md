1066-imitation
==============
Inspired by Preloaded's 1066 (https://preloaded.com/case-studies/1066/). Made for CIS 120 at UPenn.

Known Issues:
Still not finished, so there's some debugging print statements.
Preview of moves currently is repainting with black circles. Should be a trail of red/green.
If two units are moved to the same location, they will merge, with the troop count of the unit that is closest to the upper left corner (because of the iteration set up over the 2D array).
Some squares cannot be moved to. Not sure which or why.

Text below copy-pasted from README submitted for homework.

The TroopObj class:
Represents a unit of troops.
Datastructure use:
    Queue of KeyEvents - When KeyEvent dictates a valid move for a TroopObj,
    it is added (appended) to the queue. Iteration begins at the start of the
    queue, so that the TroopObj moves in the correct sequence determined by
    key presses.
Non-boring methods:
attackUnit - subtracts troops from another troop object.
draw - draws a circle (oval) that represents the unit.
    If the unit's troop count is low, a different color is drawn.


The StandardUnit class:
An extension of the TroopObj class with 'standard' stats
(steps, troops, attack, defense)


The GhostUnit class:
An extention of the TroopObj class. It is used to test where a TroopObj may
move on the 2D array. It has no stats except the steps, which are set equal to
the steps of the unit it is representing.


Changes to the GameCourt class:
Datastructure use:
    2D array of TroopObjs - Entries in battleField are either null or a
    TroopObj. This is the best way to represent a grid upon which the TroopObjs
    can move.
    ArrayList of TroopObjs - This is iterated over during various checks
    throughout the game. Sets should not be used, as an error with Comparable
    will be encountered. Also, ArrayList is superior to LinkedList in many
    ways, according to StackExchange.

Got rid of timers and GameObjects.

2D array battleField of TroopObjs. Entries in battleField are either
null or a TroopObj.

ArrayList "troopUnits" of TroopObjs.

GameCourt takes in two additional JLabels gCount & rCount. Modifies these
labels based on the number of red and green troops in troopUnits.

MouseListener and KeyListener. The MouseListener highlights the area
clicked on battleField. The KeyListener takes in KeyEvents and checks if they
represent valid moves for a selected unit (if the mouse clicked on a null in
the 2D array, nothing happens).

GameCourt represents the state of battleField by drawing circles for every
entry in battleField. battleField is redrawn as needed, during moving previews
and during/after TroopObjs move.


Changes to the Game class:
Added two JPanels red_count and green_count; their labels are gCount and rCount
(described above).

Added an "Execute" button. Moves each TroopObj in troopUnits.
Finally, it switches the side that has the turn in the instance
of GameCourt.

Added a display for the total troop count for either side.

Updated status display to show which side has the turn.

Added instructions.
