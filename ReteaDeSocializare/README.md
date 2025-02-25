Am dezvoltat o aplicație Java utilizând JavaFX pentru interfața grafică și o arhitectură stratificată (UI, Service, Repository) pentru claritate și modularitate.
Aplicația implementează operații CRUD cu persistența datelor într-o bază de date. Am integrat Observer/Observable pentru actualizarea automată a interfeței și
paginare pentru optimizarea afișării datelor. De asemenea, am folosit Java 8 Features precum Lambda Expressions, Streams API și Optional pentru un cod mai eficient
și modern. Pentru a vedea cum arata proiectul si mai multe detalii despre acesta, a se vedea: Documentatie proiect

Aplicația permite utilizatorilor să se autentifice sau să își creeze un cont nou. După conectare, aceștia au acces la o interfață cu mai multe tab-uri:

"Home" – Afișează poza de profil și numele utilizatorului conectat.

"Friends" – Listează prietenii utilizatorului cu paginare. Selectarea unui prieten afișează detaliile acestuia în partea dreaptă, iar opțiunea "Delete" permite eliminarea lui din listă.

"Friend Requests" – Conține cererile de prietenie primite, iar lista se actualizează automat în timp real dacă o cerere este acceptată sau respinsă.
                    Aplicația notifică utilizatorii în timp real când primesc o nouă cerere de prietenie.

"Make Friends" – Permite trimiterea cererilor de prietenie către alți utilizatori, care pot verifica statusul acestora în secțiunea "Friend Requests".

"Chat" – Oferă posibilitatea de a selecta un prieten și de a conversa în timp real cu acesta.

"Reply" – Permite selectarea unui prieten și a unui mesaj la care utilizatorul dorește să răspundă ("Reply").


------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Developed a Java application using JavaFX for the graphical interface and a layered architecture (UI, Service, Repository) for clarity and modularity.
The application implements CRUD operations with data persistence in a database. Integrated the Observer/Observable pattern for automatic UI updates and
pagination for optimized data display. Additionally, utilized Java 8 features such as Lambda Expressions, Streams API, and Optional for more efficient
and modern code.

The application allows users to log in or register. After logging in, they have access to an interface with multiple tabs:

"Home" – Displays the profile picture and name of the logged-in user.

"Friends" – Lists the user's friends with pagination. Selecting a friend shows their details on the right side, and the "Delete" option removes them from the list.

"Friend Requests" – Displays received friend requests, updating in real time when a request is accepted or declined.

"Make Friends" – Allows sending friend requests to other users, who can check the request status in the "Friend Requests" section.

"Chat" – Enables selecting a friend and having real-time conversations.

"Reply" – Allows selecting a friend and replying to a specific message.

The application notifies users in real time when they receive a new friend request.
