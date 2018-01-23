uint32_t Wheel(byte WheelPos) {
  readBLE();
  WheelPos = 255 - WheelPos;
  if(WheelPos < 85) {
    return strip.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  }
  if(WheelPos < 170) {
    WheelPos -= 85;
    return strip.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
  WheelPos -= 170;
  return strip.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
}

uint32_t getRandomColor(){
   return strip.Color(random(0, 255),random(0, 255),random(0, 255));
}


void showColor(int R, int G, int B){
  for(int j=10; j>1; j--){
   for (uint16_t i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, strip.Color(R/j, G/j, B/j));  
  }
   delay(50);
   strip.show();
  }
for(int j=1; j<10; j++){
   for (uint16_t i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, strip.Color(R/j, G/j, B/j));  
  }
   delay(50);
   strip.show();
  }
  for(uint16_t i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, strip.Color(0, 0,0));
  }}
