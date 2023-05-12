package pdl.backend;

import ch.qos.logback.core.joran.conditional.ElseAction;

public class Statut {

    private int idStatut = -1;
    private String nameStatut;

    public Statut(int idStatut, String nameStatut) {
        this.idStatut = idStatut;
        this.nameStatut = nameStatut;
    }

    public int getIdStatut() {
        return idStatut;
    }

  

    public String getNameStatut() {
        return nameStatut;
    }

    public void setNameStatut(String nameStatut) {
        this.nameStatut = nameStatut;
    }

    public static String getNameWithIdStatut( int idStatut) 
    {
        if(idStatut == 0)
        {
            return "private";
        }
        else
        if(idStatut == 1)
        {
            return "public";
        }
        else
        if(idStatut == 2)
        {
            return "delete";
        }
        else
        {
            return "Erreur NameStatut false car ID false";
        }
    }

    
        
}