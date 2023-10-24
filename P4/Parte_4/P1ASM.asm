	ORG	0
uno	DC.B	"abcd"
dos	DC.B	%1010,@12,"a",$a
	DC.B	"a","b","c"
	DC.W	$b
	END