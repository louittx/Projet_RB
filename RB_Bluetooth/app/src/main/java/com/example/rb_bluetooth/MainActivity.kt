package com.example.rb_bluetooth

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlin.concurrent.thread
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    lateinit var  sharedPreferences: SharedPreferences
    private lateinit var service: BluethoothService
    private var bound = false
    private lateinit var heart1: Heart
    private lateinit var heart2: Heart
    private lateinit var heart3: Heart
    private lateinit var body: Body

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

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        Intent(this, BluethoothService::class.java).also { intent ->
            startService(intent)
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        // Start the code
        val rb_v2 = findViewById<ImageView>(R.id.rb_v2)
        // Initialisation du corp
        body = Body(
            findViewById(R.id.robot_avantbras_droit),
            findViewById(R.id.robot_avantbras_gauche),
            findViewById(R.id.robot_bras_droit),
            findViewById(R.id.robot_bras_gauche),
            findViewById(R.id.robot_mains_droit),
            findViewById(R.id.robot_mains_gauche),
            findViewById(R.id.robot_oreille_droit),
            findViewById(R.id.robot_oreille_gauche),
            findViewById(R.id.robot_tors_droit),
            findViewById(R.id.robot_tors_gauche),
            findViewById(R.id.robot_tete),
            findViewById(R.id.robot_tors_2),
            findViewById(R.id.robot_tors_3),
            findViewById(R.id.robot_tors)
        )
        // Initialisation des cœurs
        heart1 = Heart(
            findViewById(R.id.heart_on_fire),
            findViewById(R.id.broken_heart),
            findViewById(R.id.mending_heart),
            findViewById(R.id.heart_suit)
        )

        heart2 = Heart(
            findViewById(R.id.heart_on_fire2),
            findViewById(R.id.broken_heart2),
            findViewById(R.id.mending_heart2),
            findViewById(R.id.heart_suit2)
        )

        heart3 = Heart(
            findViewById(R.id.heart_on_fire3),
            findViewById(R.id.broken_heart3),
            findViewById(R.id.mending_heart3),
            findViewById(R.id.heart_suit3)
        )

        val TextConnection = findViewById<TextView>(R.id.TextConnection)
        val Button_1 = findViewById<Button>(R.id.button)
        val Button_2 = findViewById<Button>(R.id.button2)
        val ButtonConnexion = findViewById<Button>(R.id.ButtonConnexion)

        // value Hearts
        /*  0 -> no display
            1 -> Heart broken
            2 -> Heart suit
            3 -> Heart (simple)
            4 -> Heart fire
         */
        heart1.updateState(3)
        heart2.updateState(3)
        heart3.updateState(3)

        sharedPreferences = this.getSharedPreferences("Caracter", MODE_PRIVATE)

        var StoreValueHeart = sharedPreferences.getInt("ValueHeart", 0)
        var StoreValueBody = sharedPreferences.getInt("ValueBody", 0)
        var StartRB = false
        var ValueBody = 0b11111111111111
        //var BufferValueBody = ByteBuffer.allocate(2)
        body.updateBody(ValueBody)
        Handler(Looper.getMainLooper()).postDelayed({ // delay a task
            if (service != null) {
                if (service.ConectedApareil == true){
                    //TextConnection.text = "Connecter"
                    StartRB = true
                    Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show()
                }
                else{
                    //TextConnection.text = "Non Connecter"
                    StartRB = false
                }
            }
        }, 1000)
        thread {
            Thread.sleep(1200)

            val bufferValueBody = ByteBuffer.allocate(2) // Alloué une seule fois

            while (true) {
                if (this::service.isInitialized && service.ConectedApareil) {
                    bufferValueBody.clear() // Reset du curseur
                    bufferValueBody.putShort(ValueBody.toShort()) // 2 octets
                    val sendValueBody = bufferValueBody.array()

                    service.connectedThread?.write(sendValueBody, sendValueBody.size)

                    // Toast sur le thread principal
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
                    }
                }
                Thread.sleep(50)
            }
        }

        /*Button_1.setOnClickListener {
            datax = datax+1
            val editor = sharedPreferences.edit()
            editor.putInt("datax", datax)
            editor.apply()
            Toast.makeText(this,"${datax}",Toast.LENGTH_SHORT).show()
        }
        Button_2.setOnClickListener {
            datay = datay+1
            val editor = sharedPreferences.edit()
            editor.putInt("datay", datay)
            editor.apply()
            Toast.makeText(this,"${datay}",Toast.LENGTH_SHORT).show()
        }*/
        ButtonConnexion.setOnClickListener {
            val SearchBtLeaf = Intent(this, MainActivity2::class.java)
            startActivity(SearchBtLeaf)
        }
    }
}