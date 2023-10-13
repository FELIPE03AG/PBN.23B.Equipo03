ORG $1
Et1: SWI
dos: EORA 5
    EORA @5
Tres: EORA %111
Pru: EQU @34
    EORA $6
    EORA 300
    EORA $FFF
    DS.B 4
    EORA #5
    EORA #@5
    EORA #1500
    EORA 1,X
Uno: EORA -255,X
    EORA 32768,X
    EORA 1,+SP
    EORA A,X
    EORA 254,X
    EORA 64444,X
    EORA [1,X]
    EORA [6444,X]
    EORA [D,X]
    BLT Uno
    CPD Dos
    ADDD #28455
    ADDD
    LBLT Uno
    IBNE A,Uno
    END