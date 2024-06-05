package com.example.magicarcade.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5RetainHandling;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;

public class MqttService extends Service {

    private static final String TAG = "MqttService";
    private Mqtt5AsyncClient client;

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
            client.toBlocking().disconnect();
        }
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
                    .automaticReconnectWithDefaultConfig()
                    .buildAsync();

            try {
                Log.d(TAG, "Connecting to MQTT broker...");
                Mqtt5ConnAck connAck = client.toBlocking().connectWith()
                        .cleanStart(false)
                        .sessionExpiryInterval(30)
                        .restrictions()
                        .receiveMaximum(10)
                        .sendMaximum(10)
                        .maximumPacketSize(10_240)
                        .sendMaximumPacketSize(10_240)
                        .topicAliasMaximum(0)
                        .sendTopicAliasMaximum(8)
                        .applyRestrictions()
                        .willPublish()
                        .topic("demo/topic/will")
                        .qos(MqttQos.EXACTLY_ONCE)
                        .payload("rip".getBytes())
                        .contentType("text/plain")
                        .messageExpiryInterval(120)
                        .delayInterval(30)
                        .applyWillPublish()
                        .send();

                Log.d(TAG, "Connected: " + connAck);

                Log.d(TAG, "Subscribing to topic...");
                client.subscribeWith()
                        .topicFilter("demo/topic/a")
                        .noLocal(true)
                        .retainHandling(Mqtt5RetainHandling.DO_NOT_SEND)
                        .retainAsPublished(true)
                        .callback(this::onMessageReceived)
                        .send().join();

                Log.d(TAG, "Subscribed");

                Log.d(TAG, "Publishing message...");
                client.toBlocking().publishWith()
                        .topic("demo/topic/a")
                        .qos(MqttQos.EXACTLY_ONCE)
                        .payload("payload".getBytes())
                        .retain(true)
                        .contentType("text/plain")
                        .messageExpiryInterval(120)
                        .userProperties()
                        .add("sender", "demo-sender-1")
                        .add("receiver", "you")
                        .applyUserProperties()
                        .send();

                Log.d(TAG, "Message published");

            } catch (Mqtt5ConnAckException e) {
                Log.e(TAG, "Connection failed: " + e.getMqttMessage(), e);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing MQTT client", e);
            }

        }).start();
    }

    private void onMessageReceived(Mqtt5Publish publish) {
        Log.d(TAG, "Received message: " + new String(publish.getPayloadAsBytes()));
    }
}
