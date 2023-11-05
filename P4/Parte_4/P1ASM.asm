	ORG	0
lbl1:	DC.B	"ABCDE"
	DC.B	0,"A"
	DC.B	"A","B","C","D"
	DC.B	%1010,@12,1,$A
	DC.B	%1010
	DC.B	"F"
	DC.W	"A"
	DC.W	@12
	DC.W	%1010,"A"
cnt:	DS.B	2
	DS.w	3
	END
	