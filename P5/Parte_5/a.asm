	ORG	$4000
	LDAA	#$15
INI	LDAA	15
	BNE	INI
	LBNE	INI
	IBNE	A,FIN
	IBNE	A,INI
	LDAA	4,Y
FIN	END	
	
	