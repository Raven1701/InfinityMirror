


void startLEDShow() {
  switch (MODE) {
    case COLORWIPE:
      colorWipe(COLOR, TIME);
      break;
    case THEATERCHASE:
      theaterChase(COLOR, TIME);
      break;
    case SNAKE:
      snake();
      break;
    case THEATERCHASERAINBOW:
      theaterChaseRainbow(TIME);
      break;
    case RAINBOWCYCLE:
      rainbowCycle(TIME);
    case COLORBLINK:
      colorBlink();
      break;
    case RANDOMCOLOR:
      randomColor();
      break;
    case COMET:
      comet();
      break;
    case DOUBLECOMET:
      doubleComet();
      break;
    case CHANINGCOLORS:
      changingColors();
      break;
    case CHANINGRANDOMCOLORS:
      changingRandomColors();
      break;
      default:
       showRandoms();
       break;

  }
}
void snake() {
  int redColor = R;
  int greenColor = G;
  int blueColor = B;
  int i = 0;
  while (STATE) {
    readBLE();
    delay(TIME);
    for (int j = 0; j < strip.numPixels(); j++) {
      strip.setPixelColor(j, COLOR);
    }//00000025510750020
    strip.setPixelColor(i % strip.numPixels(), strip.Color(R, G, B));
    strip.setPixelColor((i - 1) % strip.numPixels(), strip.Color(R, G, B));
    strip.setPixelColor((i - 2) % strip.numPixels(), strip.Color(R, G, B));
    strip.setPixelColor((i - 3) % strip.numPixels(), strip.Color(R / 2, G / 2, B / 2));
    strip.setPixelColor((i - 4) % strip.numPixels(), strip.Color(R / 2, G / 2, B / 2));
    strip.setPixelColor((i - 5) % strip.numPixels(), strip.Color(R / 2, G / 2, B / 2));
    strip.setPixelColor((i - 6) % strip.numPixels(), strip.Color(R / 2, G / 2, B / 2));
    strip.setPixelColor((i - 7) % strip.numPixels(), strip.Color(R / 4, G / 4, B / 4));
    strip.setPixelColor((i - 8) % strip.numPixels(), strip.Color(R / 4, G / 4, B / 4));
    strip.setPixelColor((i - 9) % strip.numPixels(), strip.Color(R / 4, G / 4, B / 4));
    strip.setPixelColor((i - 10) % strip.numPixels(), strip.Color(R / 4, G / 4, B / 4));
    strip.setPixelColor((i - 11) % strip.numPixels(), strip.Color(R / 4, G / 4, B / 4));
    strip.setPixelColor((i - 12) % strip.numPixels(), strip.Color(R / 6, G / 6, B / 6));
    strip.setPixelColor((i - 13) % strip.numPixels(), strip.Color(R / 6, G / 6, B / 6));
    strip.setPixelColor((i - 14) % strip.numPixels(), strip.Color(R / 6, G / 6, B / 6));
    strip.setPixelColor((i - 15) % strip.numPixels(), strip.Color(R / 6, G / 6, B / 6));
    strip.setPixelColor((i - 16) % strip.numPixels(), strip.Color(R / 6, G / 6, B / 6));
    strip.show();
    i++;
  }
}
void changingRandomColors() {
  uint32_t tempColor = getRandomColor();
  for (int j = 0; j < strip.numPixels(); j++) {
    strip.setPixelColor(j, tempColor);
  }
  strip.show();
  readBLE();
  delay(TIME);
}
void changingColors() {
  for (int j = 0; j < strip.numPixels(); j++) {
    strip.setPixelColor(j, COLOR);
  }
  strip.show();
  delay(TIME);
  for (int j = 0; j < strip.numPixels(); j++) {
    strip.setPixelColor(j, COLOR2);
  }
  strip.show();
  readBLE();
  delay(TIME);
}
void doubleComet() {
  int redColor = R;
  int greenColor = G;
  int blueColor = B;
  int i = 0;
  while (STATE) {
    readBLE();
    delay(TIME);
    for (int j = 0; j < strip.numPixels(); j++) {
      strip.setPixelColor(j, COLOR);
    }//00000025510750020
    strip.setPixelColor(i % strip.numPixels(), strip.Color(redColor, greenColor, blueColor));
    strip.setPixelColor((i - 1) % strip.numPixels(), strip.Color(redColor / 2, greenColor / 2, blueColor / 2));
    strip.setPixelColor((i - 2) % strip.numPixels(), strip.Color(redColor / 2, greenColor / 2, blueColor / 2));
    strip.setPixelColor((i - 3) % strip.numPixels(), strip.Color(redColor / 4, greenColor / 4, blueColor / 4));
    strip.setPixelColor((i - 4) % strip.numPixels(), strip.Color(redColor / 6, greenColor / 6, blueColor / 6));
    strip.setPixelColor((i + (strip.numPixels() / 2)) % strip.numPixels(), strip.Color(redColor, greenColor, blueColor));
    strip.setPixelColor((i - 1 + (strip.numPixels() / 2)) % strip.numPixels(), strip.Color(redColor / 2, greenColor / 2, blueColor / 2));
    strip.setPixelColor((i - 2 + (strip.numPixels() / 2)) % strip.numPixels(), strip.Color(redColor / 2, greenColor / 2, blueColor / 2));
    strip.setPixelColor((i - 3 + (strip.numPixels() / 2)) % strip.numPixels(), strip.Color(redColor / 4, greenColor / 4, blueColor / 4));
    strip.setPixelColor((i - 4 + (strip.numPixels() / 2)) % strip.numPixels(), strip.Color(redColor / 6, greenColor / 6, blueColor / 6));
    strip.show();
    i++;
  }
}
void comet() {
  int redColor = R;
  int greenColor = G;
  int blueColor = B;
  int i = 0;
  while (STATE) {
    readBLE();
    delay(TIME);
    for (int j = 0; j < strip.numPixels(); j++) {
      strip.setPixelColor(j, COLOR);
    }//00000025510750020
    strip.setPixelColor(i % strip.numPixels(), strip.Color(redColor, greenColor, blueColor));
    strip.setPixelColor((i - 1) % strip.numPixels(), strip.Color(redColor / 2, greenColor / 2, blueColor / 2));
    strip.setPixelColor((i - 2) % strip.numPixels(), strip.Color(redColor / 2, greenColor / 2, blueColor / 2));
    strip.setPixelColor((i - 3) % strip.numPixels(), strip.Color(redColor / 4, greenColor / 4, blueColor / 4));
    strip.setPixelColor((i - 4) % strip.numPixels(), strip.Color(redColor / 6, greenColor / 6, blueColor / 6));
    strip.show();
    i++;
  }
}
void randomColor() {
  if (!NOTRUN) {
    for (uint16_t i = 0; i < strip.numPixels(); i++) {
      strip.setPixelColor(i, getRandomColor());
      strip.show();
    }
    NOTRUN = true;
  }
}
void colorBlink() {
  while(STATE){
      for (uint16_t i = 0; i < strip.numPixels(); i++) {
        strip.setPixelColor(i, COLOR);
      } delay(TIME);
      readBLE();
      strip.show();
      for (uint16_t i = 0; i < strip.numPixels(); i++) {
        strip.setPixelColor(i, BLACK);
      }
      strip.show();
      delay(TIME);
      readBLE();
    }
}

void theaterChaseRainbow(uint8_t wait) {
  for (int j = 0; j < 256; j++) {
    if (STATE) {
      for (int q = 0; q < 3; q++) {
        for (int i = 0; i < strip.numPixels(); i = i + 3) {
          strip.setPixelColor(i + q, Wheel( (i + j) % 255)); //turn every third pixel on
        }
        strip.show();
         readBLE();
        delay(wait);
        for (int i = 0; i < strip.numPixels(); i = i + 3) {
          strip.setPixelColor(i + q, 0);      //turn every third pixel off
        }
      }
    }
    else {
      j = 255;
    }
  }
}
void rainbowCycle(uint8_t wait) {
  uint16_t i, j;
  for (j = 0; j < 256 * 5; j++) { // 5 cycles of all colors on wheel
    if (STATE) {
      for (i = 0; i < strip.numPixels(); i++) {
        strip.setPixelColor(i, Wheel(((i * 256 / strip.numPixels()) + j) & 255));
      }
      readBLE();
      strip.show();
      delay(wait);
    }
    else {
      j = 255;
    }
  }
}
void colorWipe(uint32_t c, uint8_t wait) {
  for (uint16_t i = 0; i < strip.numPixels(); i++) {
    strip.setPixelColor(i, c);
    strip.show();
    delay(wait);
  }
}
void showRandoms(){
  switch(random(0, 2)){
    case 0:
    showColor(random(0, 255),random(0, 255),random(0, 255));
   break;
    case 1:
     switch(random(0, 3)){
      case 3:
     showColor(255, 0,0);
   break;
     case 0:
       showColor(0,0,255);
     break;
     case 1:
       showColor(0, 255,0);
       break;
      case 2:
        showColor(255,255,255);
        break;}
        break;
       
   
}}
void testColors(){
  showColor(255, 0,0);
  showColor(0, 255,0);
  showColor(0,0,255);
  showColor(255,255,255);
}
void rainbow(uint8_t wait) {
  uint16_t i, j;
  for (j = 0; j < 256; j++) {
    if (STATE) {
      for (i = 0; i < strip.numPixels(); i++) {
        strip.setPixelColor(i, Wheel((i + j) & 255));
      }
      strip.show();
      delay(wait);
    }
    else {
      j = 255;
    }
  }
} void theaterChase(uint32_t c, uint8_t wait) {
  for (int j = 0; j < 10; j++) { //do 10 cycles of chasing
    for (int q = 0; q < 3; q++) {
      for (int i = 0; i < strip.numPixels(); i = i + 3) {
        strip.setPixelColor(i + q, c);  //turn every third pixel on
      }
      strip.show();

      delay(wait);

      for (int i = 0; i < strip.numPixels(); i = i + 3) {
        strip.setPixelColor(i + q, 0);      //turn every third pixel off
      }
    }
  }
}

