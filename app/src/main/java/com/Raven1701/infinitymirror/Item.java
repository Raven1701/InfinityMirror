package com.Raven1701.infinitymirror;

/**
 * Created by Raven1701 on 18.01.2018.
 */

public class Item {
    public boolean colorPallete;
    public boolean colorPallete2;
    public boolean brightness;
    public boolean delay;
    public String tittle;
    public int mode;
Item(boolean T, boolean C1, boolean C2, boolean B, int mod, String text){
    colorPallete = C1;
    colorPallete2 = C2;
    delay = T;
    brightness = B;
    mode = mod;
    tittle = text;
}


}
