# cs441-program2
This application is a game-like approach to the concepts introduced in Professor Madden's tutorial videos involving basic animation and physics. The player automatically moves left and right across the screen and bounces off of the walls upon collision. The player has three simple controls: jump, increase velocity, and decrease velocity. There is an enemy that moves similarly to the player and damages the player upon collision. The goal is to avoid the enemy for as long as possible using the controls provided. 

I started by implementing the code within the video tutorials provided by Professor Madden. Once I had a grasp on the basic concepts, I began going off-script to implement my own ideas.

I tweaked the physics code to my liking: altering the velocity and gravity values, reparing buggy collisions, and restricting the player to one jump at a time. Once I dialed in the physics, I converted the anywhere-on-screen touch input for jumping to a button located in the bottom left corner of the screen. This laid the groundwork for the control interface, which I expanded to include increase and decrease velocity buttons located in the bottom right corner of the screen. I linked each button to a text label display in the top center of the screen so the player can monitor their velocity at any given moment. I was able to learn how to implement a skin into the application to give the control interface an arcade-like feel.

Despite the application being focused around learning animation and physics, I wanted it to be as game-like as possible, so I rendered a player health bar underneath the velocity text display. I also placed a text label display for the player's health underneath the health bar so the player can observe their numerical health value instead of relying on the health bar itself. In order to dynamically animate the health bar, I altered the wall collision code to reduce the player's health upon collision and update the health display accordingly; however, this lacks practicality as a game-like application since it is impossible to avoid colliding with the wall.

I took the stock player image and inverted its colors to create an "enemy." Implementing another animated object with physics would have made the code extremely clunky, so I created a class called GameObject to encapsulate the attributes and actions of a game object, such as the player. Once the game object infrastructure was built, it was easy to generate an enemy object by instantiating a new GameObject with the enemy's values. 

Once I had an actual enemy moving on-screen, I was able to remove the damage upon wall collision. I wrote a basic rectangular collision method to trigger damage if the player and the enemy collide. I gave the game a purpose by adding a current score and high score display. The score is tied to a timer within the render method to add 1 score every second. I then wrote a reset game method that is called when the player's health reaches zero, which updates the high score if necessary, resets all object positions, refills the player's health, and resets the score counter to zero. This was the final piece in producing a simple, yet fluent survivability game where the goal is to avoid the enemy for as long as possible. To make the game more interesting, I added some basic random jumping to the enemy's movement. 

I also made an important reconfiguration of the GameObject class by reducing it to a generic class that acts as a parent to a Player and Enemy class.  

The ideas for this project largely stemmed from attempting to create a game out of a simple concept, like a box bouncing around the screen. Each addition to the game added more interest and variation to the scenario until the project evolved into a game with a goal and a high score to beat.

# Development schedule
June 8th  - Initialized project

June 9th  - Implemented animation and physics with a working jump button

June 10th - Created increase and decrease velocity controls

June 11th - Generated health display and triggered player damage upon wall collision

June 12th - Encapsulated game object attributes and actions within a GameObject class and spawned a moving enemy

June 13th - Altered GameObject and evaluated player/enemy collision

June 14th - Expanded GameObject into a Player class and an Enemy class, added score counter and high score tracking, made the game reset when the player reaches zero health, and added randomized enemy movements
