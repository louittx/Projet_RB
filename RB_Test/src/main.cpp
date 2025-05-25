#include <Arduino.h>
#include <BluetoothSerial.h>

BluetoothSerial SerialBT;
uint8_t data[];
char buffer[1023];

uint8_t peur;
uint8_t malade;
uint8_t hummer;
uint8_t fatigue;
uint16_t vie; 
uint8_t AdresseEmotion = 0x7;
uint8_t AdresseJeux = 0x3;

void sendEmotion(){
    data[0] = ((AdresseEmotion << 4)| peur)&0xff;
    data[1] = ((hummer << 4) | fatigue) & 0xff;
    data[2] = ((malade << 4) | ((vie>>8)&0xf))& 0xff;
    data[3] = vie & 0xff;

}

void setup()
{
    Serial.begin(115200);
    Serial.println("Bluetooth Serial Test");
    SerialBT.begin("RBTest"); // Nom de l'appareil Bluetooth
    Serial.println("Bluetooth device is ready to pair");
}


void loop()
{
    if (SerialBT.hasClient())
    {
        SerialBT.write(AdresseEmotion,1);
    while(SerialBT.hasClient()){
    }
}

/*
void loop()
{
    if (SerialBT.hasClient())
    {
        Serial.println("Client connected");
        SerialBT.write(data, sizeof(data));
        size_t bytesRead = SerialBT.readBytes((uint8_t*)buffer, 1023);
        Serial.print("Reçu ");
        Serial.print(bytesRead);
        Serial.println(" octets :");
        for (size_t i = 0; i < bytesRead; i++)
        {
            Serial.print(buffer[i], HEX);
            Serial.print(" ");
        }
        Serial.println();

        delay(1000);
    }
    else
    {
        Serial.println("No client connected");
        delay(1000);
    }
}*/
