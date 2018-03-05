package pl.tau.cwiczenia;

/**
 * Hello world!
 *
 */

 public class SilnikParowy {
     
     private int objetoscPracy;
     private int iloscPary = 0;
     private boolean tlokWysuniety = false;

     public SilnikParowy(int objetoscPracy) {
         this.objetoscPracy = objetoscPracy;
     }

     public pobierzPare(int iloscPary) {
         if((this.iloscPary < this.objetoscPracy))
            this.iloscPary += iloscPary;
        else {
            this.iloscPary = 0;
            this.tlokWysuniety = true;
        }
     }

 }
