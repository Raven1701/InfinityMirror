 /*                   Eryk Kruk SMProject
 *  
 */
#include <Adafruit_NeoPixel.h>

#define PIN 12
#define NUM_LEDS 55
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_LEDS, PIN, NEO_GRB + NEO_KHZ800);

String MESSAGE = "";
int BRIGHTNESS = 150;
int MODE = 0;
int TIME = 100;
boolean STATE = true;
int R,G,B;
boolean NOTRUN = false;
uint32_t COLOR = strip.Color(255, 255, 255);
uint32_t COLOR2 = strip.Color(0, 0 ,0);
uint32_t BLACK = strip.Color(0, 0, 0);
#define COLORWIPE 100
#define THEATERCHASE 101
#define SNAKE 104
#define THEATERCHASERAINBOW 103
#define RAINBOWCYCLE 102
#define COLORBLINK 105
#define RANDOMCOLOR 106
#define COMET 107
#define DOUBLECOMET 108
#define CHANINGCOLORS 109
#define CHANINGRANDOMCOLORS 110

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {; }
  strip.setBrightness(BRIGHTNESS);
   strip.begin();
   testColors();
  
  Serial.println("InfinityMirror v1.0.0 by Raven1701");
}

void loop(){
    STATE = true;
    readBLE();
    startLEDShow();
    
    }



