#include <Arduino.h>
#include <BluetoothSerial.h>

BluetoothSerial SerialBT;
uint8_t data[] = {0x44, 0x45, 0x46};
char buffer[1023];

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
}
