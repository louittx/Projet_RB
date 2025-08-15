#include <Arduino.h>

#include <RB_Animation_V4.h>

/// Ecran oled

#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
#define OLED_RESET -1
#define SCREEN_ADDRESS 0x3C

void setup()
{
  display.begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS);
  display.clearDisplay();
}
int z = 0;
void loop()
{
  display.clearDisplay();
  int v = eye_happy(z, 0);
  v = eye_angry(z, 1);
  int y = mouth_Hengry(z);
  if (v == 0)
  {
    z = 0;
  }
  else
  {
    z = z + 1;
  }
  display.display();
  delay(50);
}
