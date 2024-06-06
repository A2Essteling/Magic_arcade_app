package com.example.magicarcade.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.magicarcade.activities.LoginActivity;
import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.objects.Profile;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5RetainHandling;
import com.hivemq.client.mqtt.mqtt5.message.disconnect.Mqtt5DisconnectReasonCode;

public class MqttService extends Service {

    private static final String TAG = "MqttService";
    private static final String baseTopic = "magic";
    private static Mqtt5AsyncClient client;
    private static boolean isConnected = false;



    public static boolean getState() {
        return isConnected;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service onCreate");
        super.onCreate();
        initMqttClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service onDestroy");
        if (client != null) {
            client.toBlocking().disconnectWith()
                    .reasonCode(Mqtt5DisconnectReasonCode.NORMAL_DISCONNECTION)
                    .send();
        }
        isConnected = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initMqttClient() {
        Log.d(TAG, "initMqttClient");
        new Thread(() -> {
            client = Mqtt5Client.builder()
                    .serverHost("broker.hivemq.com")
                    .serverPort(1883)
                    .automaticReconnectWithDefaultConfig()
                    .buildAsync();

            try {
                Log.d(TAG, "Connecting to MQTT broker...");
                Mqtt5ConnAck connAck = client.toBlocking().connectWith()
                        .cleanStart(false)
                        .sessionExpiryInterval(30)
                        .send();

                Log.d(TAG, "Connected: " + connAck);
                isConnected = true;

                Log.d(TAG, "Subscribing to topic...");
                client.subscribeWith()
                        .topicFilter(baseTopic + "/#")
                        .noLocal(true)
                        .retainHandling(Mqtt5RetainHandling.DO_NOT_SEND)
                        .retainAsPublished(true)
                        .callback(this::onMessageReceived)
                        .send().join();

                Log.d(TAG, "Subscribed");

            } catch (Mqtt5ConnAckException e) {
                Log.e(TAG, "Connection failed: " + e.getMqttMessage(), e);
                isConnected = false;
            } catch (Exception e) {
                Log.e(TAG, "Error initializing MQTT client", e);
                isConnected = false;
            }

        }).start();
    }

    private void onMessageReceived(Mqtt5Publish publish) {
        Log.d(TAG, "Received message: " + new String(publish.getPayloadAsBytes()));
        Log.d(TAG, "Received topic: " + publish.getTopic().toString());
        if (publish.getTopic().toString().equals(baseTopic + "/" + Profile.getController().getID() + "/button1")) {
            Controller controller = new Controller();
            controller.getID();
        }
    }
    public static void publishMsg(String topic, String msg) {
        Log.d(TAG, "Publishing message...");
        client.toBlocking().publishWith()
                .topic(baseTopic + "/" + topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(msg.getBytes())
                .retain(true)
                .contentType("text/plain")
                .send();

        Log.d(TAG, "Message published");
    }
}
