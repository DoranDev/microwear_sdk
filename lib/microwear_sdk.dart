import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:microwear_sdk/microwear_device_control.dart';

/// A class to interact with the Microwear SDK using method channels.
class MicrowearSdk {
  final methodChannel = const MethodChannel('microwear_sdk');

  /// Connects to the device with the specified [macAddress].
  Future<void> connect({required String macAddress}) async {
    final response =
        await methodChannel.invokeMethod('connect', {'macAddress': macAddress});
    return response;
  }

  /// Creates a bond with the specified BLE device.
  Future<void> createBond(String macAddress) async {
    await methodChannel.invokeMethod('creteBond', {
      'macAddress': macAddress,
    });
  }

  /// getBondState with the specified BLE device.
  Future<void> getBondState(String macAddress) async {
    await methodChannel.invokeMethod('getBondState', {
      'macAddress': macAddress,
    });
  }

  /// getConnectStatus with the specified BLE device.
  Future<void> getConnectStatus(String macAddress) async {
    await methodChannel.invokeMethod('getConnectStatus', {
      'macAddress': macAddress,
    });
  }

  /// Registers to listen for connection status of the specified BLE device.
  Future<void> registerConnectStatus(String macAddress) async {
    await methodChannel.invokeMethod('registerConnectStatue', {
      'macAddress': macAddress,
    });
  }

  /// Unregisters the connection status listener.
  Future<void> unregisterConnectStatus() async {
    await methodChannel.invokeMethod('unregisterConnectStatue');
  }

  /// Disconnects from the device.
  Future<void> disconnect() async {
    final response = await methodChannel.invokeMethod('disconnect');
    return response;
  }

  /// Sends a request to the device using the specified [microwearDeviceControl] command
  /// and optional [data] map.
  Future sendRequest(MicrowearDeviceControl microwearDeviceControl,
      {Map? data}) async {
    if (kDebugMode) {
      log("${microwearDeviceControl.name}: $data",
          name: "MicroWear sendRequest");
    }
    final response = await methodChannel.invokeMethod('sendRequest',
        {'microwearDeviceControl': microwearDeviceControl.value, 'data': data});
    return response;
  }

  /// Sets the unit format on the device. [isMetricSystem] specifies whether to use the metric system.
  /// Uses [MicrowearDeviceControl.unitSystem].
  Future setUnitFormat({required bool isMetricSystem}) async {
    final response = await sendRequest(MicrowearDeviceControl.unitSystem,
        data: {"isMetricSystem": isMetricSystem});
    return response;
  }

  /// Sets the time format on the device. [is24] specifies whether to use 24-hour format.
  /// Uses [MicrowearDeviceControl.timeMode].
  Future setTimeFormat({required bool is24}) async {
    final response = await sendRequest(MicrowearDeviceControl.timeMode,
        data: {"is24": is24});
    return response;
  }

  /// Sets the temperature unit on the device. [isCen] specifies whether to use Celsius.
  /// Uses [MicrowearDeviceControl.tempUnit].
  Future setTempUnit({required bool isCen}) async {
    final response = await sendRequest(MicrowearDeviceControl.tempUnit,
        data: {"isCen": isCen});
    return response;
  }

  /// Sets the target step count on the device. [step] is the target number of steps.
  /// Uses [MicrowearDeviceControl.targetStep].
  Future setTargetStep({required int step}) async {
    final response = await sendRequest(MicrowearDeviceControl.targetStep,
        data: {"step": step});
    return response;
  }

  /// Sets the display time on the device. [time] is the duration in seconds.
  /// Uses [MicrowearDeviceControl.displayTime].
  Future setDisplayTime({required int time}) async {
    final response = await sendRequest(MicrowearDeviceControl.displayTime,
        data: {"time": time});
    return response;
  }

  /// Synchronizes the weather type data on the device with [tempData] and [weatherType].
  /// Uses [MicrowearDeviceControl.realTimeWeather].
  Future syncWeatherTypeData(
      {required int tempData, required int weatherType}) async {
    final response = await sendRequest(MicrowearDeviceControl.realTimeWeather,
        data: {"tempData": tempData, "weatherType": weatherType});
    return response;
  }

  /// Updates the wrist screen settings on the device with the provided parameters.
  /// Uses [MicrowearDeviceControl.raiseWrist].
  Future upHandleScreenOn(
      {required int wristScreenStatus,
      required int wristScreenBeginTime,
      required int wristScreenEndTime}) async {
    final response =
        await sendRequest(MicrowearDeviceControl.raiseWrist, data: {
      "wristScreenStatus": wristScreenStatus,
      "wristScreenBeginTime": wristScreenBeginTime,
      "wristScreenEndTime": wristScreenEndTime
    });
    return response;
  }

  /// Synchronizes the no-disturb settings on the device with the provided parameters.
  /// Uses [MicrowearDeviceControl.disturb].
  Future syncNoDisturbSet(
      {required int disturbStatus,
      required int disturbInterval,
      required int disturbBeginTime,
      required int disturbEndTime}) async {
    final response = await sendRequest(MicrowearDeviceControl.disturb, data: {
      "disturbStatus": disturbStatus,
      "disturbInterval": disturbInterval,
      "disturbBeginTime": disturbBeginTime,
      "disturbEndTime": disturbEndTime
    });
    return response;
  }

  /// Synchronizes the water notification settings on the device with the provided parameters.
  /// Uses [MicrowearDeviceControl.drinkWater].
  Future syncWaterNotify(
      {required int drinkWaterStatus,
      required int drinkWaterInterval,
      required int drinkWaterBeginTime,
      required int drinkWaterEndTime}) async {
    final response =
        await sendRequest(MicrowearDeviceControl.drinkWater, data: {
      "drinkWaterStatus": drinkWaterStatus,
      "drinkWaterInterval": drinkWaterInterval,
      "drinkWaterBeginTime": drinkWaterBeginTime,
      "drinkWaterEndTime": drinkWaterEndTime
    });
    return response;
  }

  /// Synchronizes the wash notification settings on the device with the provided parameters.
  /// Uses [MicrowearDeviceControl.washHand].
  Future syncWashNotify(
      {required int washWaterStatus,
      required int washWaterInterval,
      required int washWaterBeginTime,
      required int washWaterEndTime}) async {
    final response = await sendRequest(MicrowearDeviceControl.washHand, data: {
      "washWaterStatus": washWaterStatus,
      "washWaterInterval": washWaterInterval,
      "washWaterBeginTime": washWaterBeginTime,
      "washWaterEndTime": washWaterEndTime
    });
    return response;
  }

  /// Synchronizes the long sit notification settings on the device with the provided parameters.
  /// Uses [MicrowearDeviceControl.longSit].
  Future syncLongSitNotify(
      {required int longSitStatus,
      required int longSitInterval,
      required int longSitBeginTime,
      required int longSitEndTime}) async {
    final response = await sendRequest(MicrowearDeviceControl.longSit, data: {
      "longSitStatus": longSitStatus,
      "longSitInterval": longSitInterval,
      "longSitBeginTime": longSitBeginTime,
      "longSitEndTime": longSitEndTime
    });
    return response;
  }

  /// Retrieves the alarm clock information from the device.
  /// Uses [MicrowearDeviceControl.alarm].
  Future getAlarmClockInfo() async {
    final response = await sendRequest(MicrowearDeviceControl.alarm,
        data: {"method": "GET"});
    return response;
  }

  /// Synchronizes the alarm clock information on the device with the provided parameters.
  /// Uses [MicrowearDeviceControl.alarm].
  Future syncAlarmClockInfo(
      {required int index,
      required int mEnabled,
      String? mRepeat,
      required int mHour,
      required int mMinute,
      required List<String> listRepeat}) async {
    final response = await sendRequest(MicrowearDeviceControl.alarm, data: {
      "method": "POST",
      "index": index,
      "mEnabled": mEnabled,
      "mRepeat": mRepeat,
      "mHour": mHour,
      "mMinute": mMinute,
      "listRepeat": listRepeat
    });
    return response;
  }

  /// Opens or closes the photo-taking feature on the device. [isOpen] specifies whether to open the camera.
  /// Uses [MicrowearDeviceControl.takePhoto].
  Future openTakePhotoCamera({required bool isOpen}) async {
    final response = await sendRequest(MicrowearDeviceControl.takePhoto,
        data: {"isOpen": isOpen});
    return response;
  }

  /// Sets a notification on the device with the specified [messageId], [title], and [value].
  /// Uses [MicrowearDeviceControl.alertMsg].
  Future setNotify(
      {required int messageId,
      required String title,
      required String value}) async {
    final response = await sendRequest(MicrowearDeviceControl.alertMsg,
        data: {"messageId": messageId, "title": title, "value": value});
    return response;
  }

  /// Pushes a QR code to the device with the specified [type] and [content].
  /// Uses [MicrowearDeviceControl.addFriend].
  Future pushQRCode({required int type, required String content}) async =>
      await sendRequest(MicrowearDeviceControl.addFriend,
          data: {"type": type, "content": content});

  /// Pushes a QR code to the device with the specified [type] and [content].
  /// Uses [MicrowearDeviceControl.receiptCode].
  Future pushPayCode({required int type, required String content}) async =>
      await sendRequest(MicrowearDeviceControl.receiptCode,
          data: {"type": type, "content": content});

  /// Controls the phone with hand up or down.
  /// [type] specifies the action. 0 for hand down, 1 for hand up.
  /// Uses [MicrowearDeviceControl.androidPhoneCtrl].
  Future handUpPhone({required int type}) async =>
      await sendRequest(MicrowearDeviceControl.androidPhoneCtrl,
          data: {"type": type});

  /// Opens or closes the real-time ECG monitoring.
  /// [isOpen] specifies whether to open or close the ECG monitoring.
  /// Uses [MicrowearDeviceControl.ecgHr].
  Future syncRealTimeECG({required bool isOpen}) async =>
      await sendRequest(MicrowearDeviceControl.ecgHr, data: {"isOpen": isOpen});

  final EventChannel _deviceDataReceivedChannel =
      const EventChannel('deviceDataReceived');

  /// Stream for device data received events.
  Stream get deviceDataReceivedStream => _deviceDataReceivedChannel
      .receiveBroadcastStream(_deviceDataReceivedChannel.name)
      .cast();

  final EventChannel _getBatteryLevelChannel =
      const EventChannel('getBatteryLevel');

  /// Stream for battery level events.
  Stream get getBatteryLevelStream =>
      _getBatteryLevelChannel.receiveBroadcastStream().cast();

  final EventChannel _syncHourStepChannel = const EventChannel('syncHourStep');

  /// Stream for syncing hourly steps data.
  Stream get syncHourStepStream =>
      _syncHourStepChannel.receiveBroadcastStream().cast();

  final EventChannel _syncWeekDaySportsChannel =
      const EventChannel('syncWeekDaySports');

  /// Stream for syncing weekday sports data.
  Stream get syncWeekDaySportsStream =>
      _syncWeekDaySportsChannel.receiveBroadcastStream().cast();

  final EventChannel _deviceConfigChannel = const EventChannel('deviceConfig');

  /// Stream for device configuration events.
  Stream get deviceConfigStream =>
      _deviceConfigChannel.receiveBroadcastStream().cast();

  final EventChannel _getAlarmClockInfoChannel =
      const EventChannel('getAlarmClockInfo');

  /// Stream for retrieving alarm clock information.
  Stream get getAlarmClockInfoStream =>
      _getAlarmClockInfoChannel.receiveBroadcastStream().cast();

  final EventChannel _syncBloodPressureChannel =
      const EventChannel('syncBloodPressure');

  /// Stream for syncing blood pressure data.
  Stream get syncBloodPressureStream =>
      _syncBloodPressureChannel.receiveBroadcastStream().cast();

  final EventChannel _syncHeartDataChannel =
      const EventChannel('syncHeartData');

  /// Stream for syncing heart data.
  Stream get syncHeartDataStream =>
      _syncHeartDataChannel.receiveBroadcastStream().cast();

  final EventChannel _syncOxDataChannel = const EventChannel('syncOxData');

  /// Stream for syncing blood oxygen data.
  Stream get syncOxDataStream =>
      _syncOxDataChannel.receiveBroadcastStream().cast();

  final EventChannel _syncHomeDataChannel = const EventChannel('syncHomeData');

  /// Stream for syncing home data.
  Stream get syncHomeDataStream =>
      _syncHomeDataChannel.receiveBroadcastStream().cast();

  final EventChannel _syncRealTimeECGChannel =
      const EventChannel('syncRealTimeECG');

  /// Stream for syncing real-time ECG data.
  Stream get syncRealTimeECGStream =>
      _syncRealTimeECGChannel.receiveBroadcastStream().cast();

  final EventChannel _getDeviceFunChannel = const EventChannel('getDeviceFun');

  /// Stream for device functionality events.
  Stream get getDeviceFunStream =>
      _getDeviceFunChannel.receiveBroadcastStream().cast();

  final EventChannel _getDeviceConfig1Channel =
      const EventChannel('getDeviceConfig1');

  /// Stream for retrieving additional device configuration information.
  Stream get getDeviceConfig1Stream =>
      _getDeviceConfig1Channel.receiveBroadcastStream().cast();

  final EventChannel _registerConnectStatuesCallBackChannel =
      const EventChannel('registerConnectStatuesCallBack');

  /// Stream for connection status callback events.
  Stream get registerConnectStatuesCallBackStream =>
      _registerConnectStatuesCallBackChannel.receiveBroadcastStream().cast();

  final EventChannel _registerSomatosensoryGameCallbackChannel =
      const EventChannel('registerSomatosensoryGameCallback');

  /// Stream for somatosensory game callback events.
  Stream get registerSomatosensoryGameCallbackStream =>
      _registerSomatosensoryGameCallbackChannel.receiveBroadcastStream().cast();

  final EventChannel _registerSingleHeartOxBloodCallbackChannel =
      const EventChannel('registerSingleHeartOxBloodCallback');

  /// Stream for single heart and blood oxygen callback events.
  Stream get registerSingleHeartOxBloodCallbackStream =>
      _registerSingleHeartOxBloodCallbackChannel
          .receiveBroadcastStream()
          .cast();
}
