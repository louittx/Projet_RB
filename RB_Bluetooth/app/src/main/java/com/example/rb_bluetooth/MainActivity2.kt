package com.example.rb_bluetooth
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread

class MainActivity2 : AppCompatActivity() {
    private lateinit var service: BluethoothService
    private var bound = false
    var NameBtConnet = ""
    var isThreadRunning = true

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, serviceBinder: IBinder) {
            val binder = serviceBinder as BluethoothService.LocalBinder
            service = binder.getService()
            bound = true
            // applle les fonction pour la premier fosi si non nous devos
            // attentre le temps du action comme button ect
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Intent(this, BluethoothService::class.java).also { intent ->
            startService(intent)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        val list_bt = findViewById<ListView>(R.id.list_BT)
        val RefeshButton = findViewById<Button>(R.id.ButtonRefersh)
        val DecoButton = findViewById<Button>(R.id.ButtonDeco)

        var deviceNames = BtInit("Name")
        var deviceAddress = BtInit("adresse")
        var BtNames = ArrayList<PostData>()
        var Etat = true

        for(name in deviceNames ) {
            BtNames.add(PostData(name, false)) // permet de metre la liste sans la barre verte
        }



        var adapter = BtAdapteur(this, android.R.layout.simple_list_item_1, BtNames)
        list_bt.adapter = adapter



        list_bt.setOnItemClickListener { _, _, position, _ -> // click sur la barre
            val name = BtNames[position]
            val address = deviceAddress[position]
            Toast.makeText(this, "$name ($address)", Toast.LENGTH_SHORT).show()

            for (item in BtNames) { // masi le trcu de connexion a 0
                item.isVisible = false
            }
            name.isVisible = true // mais celuis ou nous avosn cliquer a 0
            adapter.notifyDataSetChanged() // reactualiser la liste view

            // ce connect au bluethooth
            val device = bluetoothAdapter.getRemoteDevice(address)

            if (device != null) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {

                    if (bluetoothAdapter.isDiscovering) {
                        Log.d("MonTag", "Discovery en cours, on annule")
                        bluetoothAdapter.cancelDiscovery()
                    }
                    val connectThread = service.ConnectThread(device)
                    connectThread.start()
                }
            }
        }

        //permte de d'affiche l'appareil connecter meme apres le désavigage de la page
        Handler(Looper.getMainLooper()).postDelayed({ // permet de faire un tache en decalage
            if (service != null && service.ConectedApareil == true) { // si le servie et bien aplace et que nous somme connecter a un appareille
                NameBtConnet = service.NameBtConnet // stokque le name de l'aparre connecter

                val index = BtNames.indexOfFirst { it.name == NameBtConnet } // cannais la psotion de notre appareil conneted

                if (index != -1) {
                    BtNames[index] = BtNames[index].copy(isVisible = true) // metre notre apppareil en visible
                    adapter.notifyDataSetChanged() // actualiser la liste
                }
            }
        }, 1)


        RefeshButton.setOnClickListener {
            deviceNames = BtInit("Name")
            deviceAddress = BtInit("adresse")
            adapter = BtAdapteur(this, android.R.layout.simple_list_item_1, BtNames)
            list_bt.adapter = adapter
            Toast.makeText(this,"Refresh", Toast.LENGTH_SHORT).show()
            if (service != null && service.ConectedApareil == true) { // si le servie et bien aplace et que nous somme connecter a un appareille
                NameBtConnet = service.NameBtConnet // stokque le name de l'aparre connecter

                val index = BtNames.indexOfFirst { it.name == NameBtConnet } // cannais la psotion de notre appareil conneted

                if (index != -1) {
                    BtNames[index] = BtNames[index].copy(isVisible = true) // metre notre apppareil en visible
                    adapter.notifyDataSetChanged() // actualiser la liste
                }
            }
            else {
                for (item in BtNames) {
                    item.isVisible = false
                }
            }
        }


        DecoButton.setOnClickListener {
            if (service.ConectedApareil == true){
                service.connectedThread?.cancel()
                for (item in BtNames) {
                    item.isVisible = false
                }
                adapter.notifyDataSetChanged()
                NameBtConnet = ""
            }
            else {
                Toast.makeText(this,"Not Conected to bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun BtInit(connection: String): List<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissions = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }

            if (permissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)
                return emptyList()
            }
        }

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        val deviceNames = mutableListOf<String>()
        val deviceAddresses = mutableListOf<String>()

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Le téléphone ne prend pas en charge le Bluetooth", Toast.LENGTH_SHORT).show()
            return emptyList()
        } else if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
                return emptyList()
            }
        }

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices

        if (pairedDevices != null && pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                deviceNames.add(device.name ?: "Appareil inconnu")
                deviceAddresses.add(device.address)
            }
        } else {
            Toast.makeText(this, "Aucun appareil Bluetooth appairé", Toast.LENGTH_SHORT).show()
        }

        return if (connection.lowercase() == "adresse") {
            deviceAddresses
        } else {
            deviceNames
        }
    }
}