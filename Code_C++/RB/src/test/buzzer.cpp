#include <Arduino.h>

//#include <RB_Animation_V4.h>

void setup() {
    // initialise les broches
    pinMode(9, OUTPUT);
    Serial.begin(115200);
 }
 
 void loop() {
    tone (11, 600); // allume le buzzer actif arduino
    delay(500);
    tone(11, 900); // allume le buzzer actif arduino
    delay(500);
    noTone(11);  // désactiver le buzzer actif arduino
    delay(500);
    Serial.println("test buzzer");
 }