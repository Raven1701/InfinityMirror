//*** READ STRINGS ***//
/* Arduino reads string #########***@@@$$$! where:
 *  ######### -> 9 signs, 3 per color in RGB Value 0-255
 *  ***  -> 3 signs Mode, for example 100 is colorWipe, 102 -> Rainbow
 *  @@@ -> 3 signs Brightness, Value 0-255
 *  $$$ -> time, for function
 *  ! -> if(1 -> first color, 2 -> second color)
 */

void readBLE(){
    while (Serial.available()) {
     MESSAGE = Serial.readString();
     Serial.println("Received: "+MESSAGE);
     if(MESSAGE[0]=='X'){
      COLOR = COLOR2 = BLACK;
      MODE = 100;
     }
     else if(MESSAGE[0]=='4'){
      
     }
     else{
     decodeMessage();}
     STATE = NOTRUN = false;
    } 
  }
void decodeMessage(){
  getRGB();
  getMode();
  getBrightnes();
  getTime();
}
void getTime(){
  String myTime = MESSAGE.substring(15,18);
  TIME = myTime.toInt();
  Serial.println("    Time:"+String(TIME)+"0 [ms]");
}
void getRGB(){
    String redColor = MESSAGE.substring(0,3);
    String greenColor =  MESSAGE.substring(3,6);
    String blueColor =  MESSAGE.substring(6,9);
    R = redColor.toInt();
    G = greenColor.toInt();
    B = blueColor.toInt();
        if(MESSAGE[18]=='1'){
    COLOR = strip.Color(R, G, B);
    Serial.print("Colors:FIRST R:"+redColor+" G:"+greenColor+" B:"+blueColor);
        }
        else if(MESSAGE[18]=='2'){
    COLOR2 = strip.Color(R, G, B);
    Serial.print("  SECOND: R:"+redColor+" G:"+greenColor+" B:"+blueColor);}
}
void getBrightnes(){
   String bright = MESSAGE.substring(12,15);
   BRIGHTNESS = bright.toInt();
   Serial.print("    Brithness :"+bright);
   
}
void getMode(){
  String mode = MESSAGE.substring(9,12);
  MODE = mode.toInt();
  Serial.print("    Mode: "+String(MODE));
}
