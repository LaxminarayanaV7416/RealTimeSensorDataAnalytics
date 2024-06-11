package com.psd.RealTimeSensorDataAnalyticsBackend.controllers;

import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.CredentialsConfBean;
import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.MqttBrokerCallBacksAutoBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.exceptions.ExceptionMessages;
import com.psd.RealTimeSensorDataAnalyticsBackend.exceptions.MqttException;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.MqttPublishModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.MqttSubscriberModel;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    public CredentialsConfBean credentialsConf;

    @PostMapping("publish")
    public void publishMessage(@RequestBody @Valid MqttPublishModel messagePublishModel,
            BindingResult bindingResult) throws org.eclipse.paho.client.mqttv3.MqttException {
        if (bindingResult.hasErrors()) {
            throw new MqttException(ExceptionMessages.SOME_PARAMETERS_INVALID);
        }

        MqttMessage mqttMessage = new MqttMessage(messagePublishModel.getMessage().getBytes());
        mqttMessage.setQos(messagePublishModel.getQos());
        mqttMessage.setRetained(messagePublishModel.getRetained());

        MqttBrokerCallBacksAutoBeans.getInstance(credentialsConf.getMqttServerURL(), credentialsConf.getServerID(),
                credentialsConf.getUsername(), credentialsConf.getPassword())
                .publish(messagePublishModel.getTopic(), mqttMessage);
    }

    @GetMapping("subscribe")
    public List<MqttSubscriberModel> subscribeChannel(@RequestParam(value = "topic") String topic,
            @RequestParam(value = "wait_millis") Integer waitMillis)
            throws InterruptedException, org.eclipse.paho.client.mqttv3.MqttException {
        List<MqttSubscriberModel> messages = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        MqttBrokerCallBacksAutoBeans.getInstance(credentialsConf.getMqttServerURL(), credentialsConf.getServerID(),
                credentialsConf.getUsername(), credentialsConf.getPassword())
                .subscribeWithResponse(topic, (s, mqttMessage) -> {
                    MqttSubscriberModel mqttSubscribeModel = new MqttSubscriberModel();
                    mqttSubscribeModel.setId(mqttMessage.getId());
                    mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
                    mqttSubscribeModel.setQos(mqttMessage.getQos());
                    messages.add(mqttSubscribeModel);
                    countDownLatch.countDown();
                });

        countDownLatch.await(waitMillis, TimeUnit.MILLISECONDS);

        return messages;
    }

}
