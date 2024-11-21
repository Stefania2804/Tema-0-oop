Am creat urmatoarele clase : Player, Board, Game, Card care se extinde in alte doua clase Minion si Hero, Initials, Constants si JsonOutput.

Clasa Player contine campuri si metode legate de jucator. Campurile  clasei sunt urmatoarele: index, deck, cardsInHand, hero, mana, cardsUsedThisTour, frozenCards, stillFrozenCards, heroUsed, playerWins.
index - indexul jucatorului (1 sau 2)
deck - pachetul ales la inceputul fiecarui joc
cardsInHand - lista de carti pe care o are jucatorul in mana
hero - eroul jucatorului
mana - mana jucatorului 
cardsUsedThisTour - lista de carti folosita tura curenta
frozenCards - lista de carti inghetate din dreptul jucatorului
stillFrozenCards - lista de carti de pe masa din dreptul jucatorului care vor ramane inghetate si tura urmatoare
heroUsed - un camp care retine daca s-a folosit eroul tura respectiva sau nu
playerWins-numarul de jocuri castigate de jucator

Ca metode in clasa Player am gettere si settere pentru campuri care sunt private, metode de adaugare si scoatere de carti din listele corespunzatoare si o metoda prin care caut o carte in lista de carti inghetate aferenta jucatorului si returnez 1 daca este inghetata sau 0 in caz contrar.

Clasa Card contine urmatoarele campuri : mana, health, description, colors, name.
Ca metode contine gettere si settere pentru campuri care sunt private.
Clasa Card suprascrie constructorul default primind o carte de tip CardInput ca parametru si transformand-o intr-o carte de tip Card.
Clasa Card se extinde in 2 subclase: Minion si Hero. Cum cartile de tip Hero nu au attackDamage, acesta este un camp suplimentar in clasa Minion. Constructorul clasei Minion apeleaza super pentru o carte de tip CardInput si in plus asociaza si campul attackDamage cu campul attackDamage aferent cartii primita ca parametru. Clasa Hero apeleaza super in constructor pentru a transforma eroul primit in input de tip CardInput in erou de tip Hero. El in plus seteaza viata eroului la 30.

Clasa Board contine un singur camp private, o lista de carti de tip Minion. Am un getter pentru a returna intreaga tabla de joc. Restul metodelor au ca scop functionalitatea jocului. Mare parte din el se desfasoara pe masa de joc(se plaseaza carti pe masa, se ataca minioni, se ataca eroi, se ingheata carti etc.). Am creat o metoda pentru fiecare actiune si am facut verificarile necesare ca aceste actiuni sa se desfasoare conform regulilor. Spre exemplu pentru metoda placeOnBoard eu am primit ca parametru o carte de tip Minion si playerul care trebuie sa plaseze aceasta carte pe masa. Acest player va fi player -ul curent. Am verificat daca are suficienta mana, dupa care am verificat tipul minionului avand in vedere ca fiecare trebuie asezat pe un anumit rand din dreptul fiecarui jucator.
La metoda cardAttack am verificat intai posibilele erori dupa care am facut schimbarile necesare pentru cartea atacata. Daca ea a ramas fara viata am scos-o de pe tabla si am mutat toate cartile de pe randul ei cu o pozitie spre stanga. 

In clasa Game am urmatoarele campuri: gamesPlayed, round, status, turnOne, turnTwo, playerTurn.
gamesPlayed - numarul de jocuri jucate
round - runda curenta
status - statusul jocului(daca e terminat sau nu)
turnOne - tura jucatorul 1
turnTwo - tura jucatorului 2
playerTurn - indicele jucatorului curent

turnOne si turnTwo ma ajuta sa imi dau seama cand trec la urmatoarea runda si cand se extrage o noua carte din pachet

Ca metode am gettere si settere pentru fiecare camp. Constructorul primeste ca parametru numarul total de jocuri jucate deoarece nu vreau sa se reseteze la fiecare joc.

Clasa Initials are trei campuri private si gettere si settere pentru fiecare dintre ele: gamesPlayed, playerOneWins, playerTwoWins.
Le retin deoarece nu vreau sa le resetez la fiecare inceput de joc nou.

In clasa principala Main am extras datele de input. Am parcurs jocurile si pentru fiecare joc am parcurs actiunile. Am verificat cu switch care este comanda si am apelat metoda aferenta fiecarei comenzi. Aceste metode se afla in main si au ca principal scop afisarea in format JSON. 

Clasa Constants este o clasa de tip Enum; am folosit-o pentru a evita numerele magice. Am tribuit fiecarui rand si fiecarei erori valoarea corespunzatoare, plus valoarea maxima a manei si viata initiala a eroului.

Clasa JsonOutput are un constructor public, nu are campuri, singurul ei scop fiind sa faca afisarea in format Json.
Metodele ei sunt reprezentative pentru fiecare comanda a jocului. Spre exemplu, in prima metoda getPlayerDeck se primesc ca parametrii playerul, indexul acestuia, objectMapper si outputul si afiseaza in format JSON intreg pachetul de carti folosindu-se de crearea unui ArrayNode unde adauga pe rand fiecare carte din pachet, transformand-o dintr-un obiect Java intr-un nod Json.
