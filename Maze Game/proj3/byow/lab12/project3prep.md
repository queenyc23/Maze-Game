# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:

The TA's solution was a lot more organised. He made an entire new class to take care of Points, and put his code in chunks. He also had a very long-term mindset, meaning he was able to think of complications that might come up in the future if our code is too specific or not abstracted enough. Don't be afraid to use helper method and distribute the work!

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:

It's like Minecraft. We have to connect blocks together to make a world. The hexagons are the rooms and the tesselation represents the hallways.

**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:

Draw 1 hexagon first, so draw a room and a hallway first. Then figure out how to connect them together.

**What distinguishes a hallway from a room? How are they similar?**

Answer:

Room: mainly rectangular, but other shapes work too
Hallway: straight long, 
Similar: connected, walls that are diff from floors, no GAPS in the floor between adjacent rooms/hallways
