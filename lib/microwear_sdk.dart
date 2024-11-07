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

  final EventChannel _getBatteryLevelChannel =
      const EventChannel('getBatteryLevel');
  Stream get getBatteryLevelStream =>
      _getBatteryLevelChannel.receiveBroadcastStream().cast();

  final EventChannel _syncHourStepChannel = const EventChannel('syncHourStep');
  Stream get syncHourStepStream =>
      _syncHourStepChannel.receiveBroadcastStream().cast();

  final EventChannel _syncWeekDaySportsChannel =
      const EventChannel('syncWeekDaySports');
  Stream get syncWeekDaySportsStream =>
      _syncWeekDaySportsChannel.receiveBroadcastStream().cast();

  final EventChannel _deviceConfigChannel = const EventChannel('deviceConfig');
  Stream get deviceConfigStream =>
      _deviceConfigChannel.receiveBroadcastStream().cast();

  final EventChannel _getAlarmClockInfoChannel =
      const EventChannel('getAlarmClockInfo');
  Stream get getAlarmClockInfoStream =>
      _getAlarmClockInfoChannel.receiveBroadcastStream().cast();

  final EventChannel _syncBloodPressureChannel =
      const EventChannel('syncBloodPressure');
  Stream get syncBloodPressureStream =>
      _syncBloodPressureChannel.receiveBroadcastStream().cast();

  final EventChannel _syncHeartDataChannel =
      const EventChannel('syncHeartData');
  Stream get syncHeartDataStream =>
      _syncHeartDataChannel.receiveBroadcastStream().cast();

  final EventChannel _syncOxDataChannel = const EventChannel('syncOxData');
  Stream get syncOxDataStream =>
      _syncOxDataChannel.receiveBroadcastStream().cast();

  final EventChannel _syncHomeDataChannel = const EventChannel('syncHomeData');
  Stream get syncHomeDataStream =>
      _syncHomeDataChannel.receiveBroadcastStream().cast();

  final EventChannel _syncRealTimeECGChannel =
      const EventChannel('syncRealTimeECG');
  Stream get syncRealTimeECGStream =>
      _syncRealTimeECGChannel.receiveBroadcastStream().cast();

  final EventChannel _getDeviceFunChannel = const EventChannel('getDeviceFun');
  Stream get getDeviceFunStream =>
      _getDeviceFunChannel.receiveBroadcastStream().cast();

  final EventChannel _getDeviceConfig1Channel =
      const EventChannel('getDeviceConfig1');
  Stream get getDeviceConfig1Stream =>
      _getDeviceConfig1Channel.receiveBroadcastStream().cast();

  final EventChannel _registerConnectStatuesCallBackChannel =
      const EventChannel('registerConnectStatuesCallBack');
  Stream get registerConnectStatuesCallBackStream =>
      _registerConnectStatuesCallBackChannel.receiveBroadcastStream().cast();

  final EventChannel _registerSomatosensoryGameCallbackChannel =
      const EventChannel('registerSomatosensoryGameCallback');
  Stream get registerSomatosensoryGameCallbackStream =>
      _registerSomatosensoryGameCallbackChannel.receiveBroadcastStream().cast();

  final EventChannel _registerSingleHeartOxBloodCallbackChannel =
      const EventChannel('registerSingleHeartOxBloodCallback');
  Stream get registerSingleHeartOxBloodCallbackStream =>
      _registerSingleHeartOxBloodCallbackChannel
          .receiveBroadcastStream()
          .cast();
}
