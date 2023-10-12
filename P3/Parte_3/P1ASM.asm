	ORG 	$4040
	EORA	#1500
	EORA 	1,+PC
tres:	EQU	%111
	EQU	$3
h	EQU	
F	equ	$#@
Uno	DS.B	3
	DS.B	1,5
	DS.W	5
	DS.L	4
	DS.b	#	
	DC.W	4,$3
	dc.b	@4,5
	DC,L	4
	DC.B	%1
	DC.W	#4
	DC.W	7
	DC.B	"ABC"
	DC.W	"A"
	DC.B	$FFFF
	DC.B	$FFFF,5
	DS.W	$5
	sss	efd
	END