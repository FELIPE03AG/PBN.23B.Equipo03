; comentario número 1 =Programación=
	ORG 	$4040
Et1:    SWI
dos:    EORA 	5
	EORA 	@5
Tres:	EORA 	%111
	EORA	$6
	EORA	300
	EORA	$FFF
	EORA	#5
	EORA    #@5
	EORA	#1500
	EORA	1,X
Uno:	EORA	255,X
	EORA	32768,X
	EORA 	1,+PC
	EORA	A,X
	EORA 	254,X
	EORA 	64444,X
	EORA	[1,X]
	EORA	[6444,X]
	EORA	[D,X]
	BLT	$12
	LBLT	Uno
	IBNE	A,Uno
; comentario número 2 ¡Bajo!
;comentario número 3 -Nivel-
	END