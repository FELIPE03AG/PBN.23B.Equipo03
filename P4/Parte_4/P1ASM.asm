	ORG	$0100
uno	equ	$30
dos	equ	$505
tres	equ	$120
	ldx	uno
	ADCA	dos
	JSR	tres
	LDAA	$120
	LDAA	$30
	end
	
	