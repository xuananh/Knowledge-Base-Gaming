#program base.
#const width=31.
#const height=10.


w(0..width-1).
h(0..height-1).
start(1).
end(21).
startX(2,height-5).
goalX(width-2,height-1).
width(width).
height(height).

block(X,Y,1) :- w(X), X<=(width/2 -2), X>=(width/2 -4), Y =height-3.
block(X,Y,1) :- w(X), X<=(width/2 -8), X>=(width/2 -9), Y =height-4.

block(X,height,1) :- w(X).
block(width-1,Y,1) :- h(Y).
block(0,Y,1) :- h(Y).

#show block/3.
#show width/1.
#show height/1.
#show startX/2.
#show goalX/2.


%%%%%%%%%%%%%%%%%
%	End Boss	%
%%%%%%%%%%%%%%%%%