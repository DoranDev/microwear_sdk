import 'package:flutter/services.dart';
import 'package:microwear_sdk/microwear_device_control.dart';

class MicrowearSdk {
  final methodChannel = const MethodChannel('microwear_sdk');

  Future<String?> sendRequest(MicrowearDeviceControl microwearDeviceControl,
      {Map? data}) async {
    final version = await methodChannel.invokeMethod('sendRequest',
        {'microwearDeviceControl': microwearDeviceControl, 'data': data});
    return version;
  }

  final EventChannel _deviceDataReceivedChannel =
      const EventChannel('deviceDataReceived');
  Stream get deviceDataReceivedStream => _deviceDataReceivedChannel
      .receiveBroadcastStream(_deviceDataReceivedChannel.name)
      .cast();
}
