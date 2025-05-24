#ifndef RB_Animation_V4_h
#define RB_Animation_V4_h

#include "Arduino.h"
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

int eye_angry(int i, int pos); // pos = 1 also left or pos = 1 also rigth
int eye_bad(int i, int pos);
int eye_close(int i, int pos);
int eye_happy(int i, int pos);
int eye_incredible(int i, int pos);
int eye_sad(int i, int pos);
int eye_semi_happy(int i, int pos);
int eye_sleep(int i, int pos);

int mouth_Hengry(int i);
int mouth_Hengry_hot(int i);
int mouth_Mischievous(int i);
int mouth_Surprised(int i);

extern Adafruit_SSD1306 display;

#endif