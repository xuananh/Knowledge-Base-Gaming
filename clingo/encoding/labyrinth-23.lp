% Konstruktion eine (vereinfachten) horiontalen Labyrinths. Es wird davon ausgegangen, dass die Etagen vorhanden sind und dann Zwischenwaende und Deckenluken neu eingefuegt werden.


start(1,1). 
goal(1,2). % Test: goal(8,8).
field(1..width, 1..height).
% Hinweis: Punkt (1,1) ist unten links


% aperture(X,Y): untere �ffnung einer Etage - 2 pro Etage.
% wall(X,Y): dieses Feld besteht aus einer Mauer - 2 pro Etage


#const width=20.
#const height=20.

width(width).
height(height * 2).

w(1..width).
h(1..height).

h1(2..height). % in der untersten Etage gibt es weder �ffnungen noch Mauern

% jeweils zwei �ffnungen und W�nde pro Etage
2{ aperture(X,Y) : w(X) }2 	:- h1(Y).
2{ wall(X,Y) : w(X) } 2	:- h1(Y). 



% m�gliche Schritte (d.h. nicht durch Mauern, Etagenwechsel nur bei L�cke)
go(X,Y,X+1,Y) :- field(X,Y), field(X+1,Y), not wall(X,Y), not wall(X+1,Y). 
go(X,Y,X-1,Y) :- field(X,Y), field(X-1,Y), not wall(X,Y), not wall(X-1,Y).
go(X,Y,X,Y+1) :- field(X,Y), field(X,Y+1), aperture(X,Y+1).
go(X,Y,X,Y-1) :- field(X,Y), field(X,Y-1), aperture(X,Y).

%go(X,Y) :- field(X,Y), not wall(X,Y).


reachable(X,Y) :- start(X,Y).
reachable(X,Y) :- go(A,B,X,Y), reachable(A,B).


% minimale Schritte um X,Y zu erreichen
#const max_length = 100.
#const min_length = 100.

reachable_in(X,Y,1) :- start(X,Y).
reachable_in(X,Y,T + 1) :- reachable_in(A,B,T), go(A,B,X,Y), T < max_length.
reachable_in(T) :- goal(X,Y), T =  #min{ TT : reachable_in(X,Y,TT) }.

%#maximize { T : reachable_in(T) }.
:- reachable_in(T), T < min_length.


% jedes Feld muss erreichbar sein
%:- go(X,Y), not reachable(X,Y).

% Ziel muss erreichbar sein:
:- goal(X,Y), not reachable(X,Y).

% Unterhalb und oberhalb einer �ffnung darf keine Mauer sein:
:- aperture(X,Y), wall(X,Y).
:- aperture(X,Y), wall(X,Y-1).

% Start und Ziel d�rfen nicht in einer Mauer sein:
:- wall(X,Y), goal(X,Y).
:- wall(X,Y), start(X,Y).

% keine 2 W�nde nebeneinander:
:- wall(X,Y), wall(X+1,Y).

% keine 2 �ffnungen nebeneinander:
:- aperture(X,Y), aperture(X+1,Y).

% keine 2 �ffnungen untereinander (m�ssen versetzt sein, um springen zu k�nnen)
:- aperture(X,Y), aperture(X,Y+1).

% Start und Ziel d�rfen nicht direkt durch eine �ffnung miteinander verbunden sein:
:- aperture(X,Y), goal(X,Y). % wenn Start und Ziel �bereinander sind.

% Zweite Etage: Die L�cke zur 1. Etage darf erst nach dem 1. Block der 2. Etage vorhanden sein
%% first_a(X) :- aperture(X1, 2), aperture(X, 2), X < X1.
%% first_w(X) :- wall(X1, 2), wall(X, 2), X < X1.
%% :- first_w(XW), first_a(XA), XA < XW.


% test
%:- aperture(2,2).



%#show aperture/2.
%#show wall/2. 



%%%%%%%%%%%%%%%%%%%%%%%%%%%% finding a path %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% #const timemax = 30.
%% time(1..timemax).

%% % Schritte
%% 0{ at(X,Y,T) : field(X,Y) }1 :- time(T).
%% at(X,Y,1) :- start(X,Y).
%% atex(T) :- at(_,_,T).

%% :- at(X,Y,T), at(A,B,T+1), not go(X,Y,A,B).

%% finished :- goal(A,B), at(A,B,_).
%% :- not finished.
%% :- atex(T), T>1, not atex(T-1).


%% #minimize { T : atex(T) }.

%% % der minimierte Weg muss mehr als N Schritte enthalten -> funktioniert nicht
%% %:- N{ atex(_) }N , N<6, time(N).

%% #show at/3.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% creating blocks %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Hinweis: Punkt (1,1) ist bei blocks(X,Y,T) nun oben links (wird umgerechnet)

max(1..height*2).

ungerade(1). 
ungerade(X+2) :- ungerade(X),max(X+2).
gerade(2). 
gerade(X+2) :- gerade(X), max(X+2).

startX(X - 1, height * 2 - Y * 2) :- start(X,Y).
goalX(X - 1, height * 2 - Y * 2)  :- goal(X,Y).

wallX(X,height*2-Y*2+1)  :- wall(X,Y).
apertureX(X,height*2-Y*2+2) :- aperture(X,Y), field(X,Y).

block(X-1,Y-1,1) :- gerade(Y), w(X), not apertureX(X,Y).
block(X-1,Y-1,0) :- apertureX(X,Y).
%:- block(X,Y,1), aperture(X,Y).

block(X-1,Y-1,0) :- ungerade(Y), w(X), not wallX(X,Y).
block(X-1,Y-1,1) :- wallX(X,Y).
%:- block(X,Y,0), wall(X,Y).

block(X, Y, 10) :- startX(X,Y).
block(X, Y, 11) :- goalX(X,Y).



#show block/3. 
#show width/1.
#show height/1.
#show startX/2.
#show goalX/2.
#show reachable_in/1.
