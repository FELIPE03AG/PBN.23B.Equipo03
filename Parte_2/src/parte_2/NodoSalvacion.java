package parte_2;


public class NodoSalvacion {
    public String CODOP;
    public String Operando;
    public String AddrMode;
    public String SourceForm;
    public String byteCalcular;
    public String byteTotal;
    public NodoSalvacion siguiente;

    public NodoSalvacion(String CODOP, String Operando, String AddrMode, String SourceForm, String byteCalcular, String byteTotal, NodoSalvacion siguiente) {
        this.CODOP = CODOP;
        this.Operando = Operando;
        this.AddrMode = AddrMode;
        this.SourceForm = SourceForm;
        this.byteCalcular = byteCalcular;
        this.byteTotal = byteTotal;
        this.siguiente = siguiente;
    }
 
}
