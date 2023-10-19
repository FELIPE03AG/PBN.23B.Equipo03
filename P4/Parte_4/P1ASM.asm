	ORG	$1010
	NOP
	INX	
Et1:	ADCA	#3
Tres:	EQU	%111
Uno:	DS.B	3
	TBNE	A,$30
	TBNE	A,uno
	LBLT	$fff
	LBLT	uno
	BVS	$56
	BVS	uno
	END