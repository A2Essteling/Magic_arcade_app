package com.example.magicarcade.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.magicarcade.fragments.ScoreboardFragment;
import com.example.magicarcade.objects.Controller;
import com.example.magicarcade.objects.Profile;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.disconnect.Mqtt5DisconnectReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5RetainHandling;

public class MqttService extends Service {

    private static final String TAG = "MqttService";
    private static final String baseTopic = "magic";
    private static Mqtt5AsyncClient client;
    private final IBinder binder = new LocalBinder();

    private static Handler scoreUpdateHandler;

    public class LocalBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }
        public void setScoreUpdateHandler(Handler handler) {
            scoreUpdateHandler = handler;
        }
    }

    public static void publishMsg(String topic, String msg) {
        if (getState()) {
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

    public static void publishMsgID(String topic, String msg) {
        if(getState()) {
            Log.d(TAG, "Publishing message...");
            client.toBlocking().publishWith()
                    .topic(baseTopic + "/" + Profile.getController().getID().getValue() + "/" + topic)
                    .qos(MqttQos.EXACTLY_ONCE)
                    .payload(msg.getBytes())
                    .retain(true)
                    .contentType("text/plain")
                    .send();

            Log.d(TAG, "Message published");
        }
    }

    public static boolean getState() {
        return client != null && client.getState().isConnected();
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
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
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


            } catch (Mqtt5ConnAckException e) {
                Log.e(TAG, "Connection failed: " + e.getMqttMessage(), e);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing MQTT client", e);
            }

        }).start();
    }

    private static void onMessageReceived(Mqtt5Publish publish) {
        Log.d(TAG, "Received message: " + new String(publish.getPayloadAsBytes()));
        Log.d(TAG, "Received topic: " + publish.getTopic().toString());
        Controller controller = Profile.getController();
        String payload = new String(publish.getPayloadAsBytes());
        String topic = publish.getTopic().toString();
        if (topic.equals(baseTopic + "/" + Profile.getController().getID().getValue() + "/button1")) {
            controller.setButton1(payload.equals("1"));
        } else if (topic.equals(baseTopic + "/" + Profile.getController().getID().getValue() + "/button2")) {
            controller.setButton2(payload.equals("1"));
        } else if (topic.equals(baseTopic + "/" + Profile.getController().getID().getValue() + "/joystickButton")) {
            controller.setButtonJoy(payload.equals("1"));
        } else if (topic.equals(baseTopic + "/" + Profile.getController().getID().getValue() + "/joystickX")) {
            controller.setJoyX(Math.round((Double.parseDouble(payload) / 4095 * 200) - 100));
        } else if (topic.equals(baseTopic + "/" + Profile.getController().getID().getValue() + "/joystickY")) {
            controller.setJoyY(Math.round((Double.parseDouble(payload) / 4095 * 200) - 100));
        } else if (topic.substring(0,topic.lastIndexOf("/")).equals(baseTopic+"/highscore")) {
            if (scoreUpdateHandler != null) {
                Message message = scoreUpdateHandler.obtainMessage(1);
                Bundle bundle = new Bundle();
                bundle.putString("name", topic.substring(topic.lastIndexOf("/")+1)); // Change this as needed
                bundle.putInt("score", Integer.parseInt(payload));
                message.setData(bundle);
                scoreUpdateHandler.sendMessage(message);
            }
        }
    }

    public static void subscribe(String topic) {
        Log.d(TAG, "Subscribing to topic..." + baseTopic + "/" + topic);
        client.subscribeWith()
                .topicFilter(baseTopic + "/" + topic)
                .noLocal(true)
                .retainHandling(Mqtt5RetainHandling.DO_NOT_SEND)
                .retainAsPublished(true)
                .callback(publish -> onMessageReceived(publish))
                .send().join();

        Log.d(TAG, "Subscribed");
    }
}
