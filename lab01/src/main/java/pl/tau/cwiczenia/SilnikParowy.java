package pl.tau.cwiczenia;

/**
 * Hello world!
 *
 */

 public class SilnikParowy {
     
     private int _objetoscPracy;
     private int _iloscPary = 0;
     private boolean _tlokWysuniety = false;

     public SilnikParowy(int objetoscPracy) {
         this._objetoscPracy = objetoscPracy;
     }

     public void pobierzPare(int iloscPary) {
         if((this._iloscPary + iloscPary < this._objetoscPracy)) {
            this._iloscPary += iloscPary;
         }
        else {
            this._iloscPary = 0;
            this._tlokWysuniety = true;
        }
     }
     
     public boolean tlokWysuniety() {
    	 return _tlokWysuniety;
     }
     
     public int iloscPary() {
    	 return _iloscPary;
     }
 
 }
