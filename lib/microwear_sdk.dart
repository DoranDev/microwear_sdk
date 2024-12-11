import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:microwear_sdk/microwear_device_control.dart';
import 'package:microwear_sdk/microwear_gps_command.dart';
import 'package:microwear_sdk/microwear_weather_data.dart';

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
  Future getBondState(String macAddress) async {
    final response = await methodChannel.invokeMethod('getBondState', {
      'macAddress': macAddress,
    });
    return response;
  }

  /// getConnectStatus with the specified BLE device.
  Future getConnectStatus(String macAddress) async {
    final response = await methodChannel.invokeMethod('getConnectStatus', {
      'macAddress': macAddress,
    });
    return response;
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

  /// Synchronizes the weather forecast (max 7) on the device with [tempData] and [weatherType].
  /// Uses [MicrowearDeviceControl.weatherForecast].
  Future syncWeatherForecast(
      {required List<MicrowearWeatherData> listMicrowearWeatherData}) async {
    // Ensure the list contains at most 7 items
    if (listMicrowearWeatherData.length > 7) {
      throw ArgumentError('The list of weather data must not exceed 7 items.');
    }
    final response =
        await sendRequest(MicrowearDeviceControl.weatherForecast, data: {
      "weathers": listMicrowearWeatherData.map((e) => e.toMap()).toList(),
    });
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

  /// Push Contact Dial
  /// Uses [MicrowearDeviceControl.otaStart].
  Future startPushContactDial({required List listContact}) async =>
      await sendRequest(MicrowearDeviceControl.otaStart, data: {
        "otaType": "startPushContactDial",
        "listContact": listContact
      });

  /// Opens or closes the real-time ECG monitoring.
  /// [isOpen] specifies whether to open or close the ECG monitoring.
  /// Uses [MicrowearDeviceControl.ecgHr].
  Future syncRealTimeECG({required bool isOpen}) async =>
      await sendRequest(MicrowearDeviceControl.ecgHr, data: {"isOpen": isOpen});

  /// Uses [MicrowearDeviceControl.stock].
  Future sendStock(
      {required int count,
      required int id,
      required String code,
      required String companyName,
      required String currentPrice,
      required String changePercent}) async {
    final response = await sendRequest(MicrowearDeviceControl.stock, data: {
      "count": count,
      "id": id,
      "code": code,
      "companyName": companyName,
      "currentPrice": currentPrice,
      "changePercent": changePercent
    });
    return response;
  }

  /// Uses [MicrowearDeviceControl.gpsSport].
  ///
  ///
  /// Reply to the status of the APP when the watch starts.
  ///
  /// @param type
  /// GPS_CMD_GPS = 0 //Request GPS positioning rights: [MicrowearGpsCommand.requestPermission]
  /// GPS_CMD_APP_BUSY = 7 //APP is busy and cannot respond at this time [MicrowearGpsCommand.appBusy]
  ///
  /// @param status
  /// - 1: GPS open reply
  /// - 0: GPS closed reply
  ///
  ///
  Future startGPSStatus(
      {required MicrowearGpsCommand type, required int status}) async {
    final response = await sendRequest(MicrowearDeviceControl.gpsSport, data: {
      "func": "startGPSStatus",
      "type": type.value,
      "status": status,
    });
    return response;
  }

  /// Uses [MicrowearDeviceControl.gpsSport].
  ///
  ///
  /// Reply to the status of the APP when the watch starts.
  ///
  /// @param type
  /// GPS_CMD_PAUSE= 4, //Pause [MicrowearGpsCommand.pauseActivity]
  /// GPS_CMD_CONTINUE= 5, //Continue [MicrowearGpsCommand.resumeActivity]
  /// GPS_CMD_END= 6, //End [MicrowearGpsCommand.endActivity]
  ///
  /// @param sportId
  /// The ID of the current sport activity.
  ///
  ///
  Future sendGPSStatus(
      {required MicrowearGpsCommand type, required int sportId}) async {
    final response = await sendRequest(MicrowearDeviceControl.gpsSport, data: {
      "func": "sendGPSStatus",
      "type": type.value,
      "sportId": sportId,
    });
    return response;
  }

  /// Synchronize the sports data of the mobile phone.
  ///
  Future syncGPSData({
    required int sportHr,
    required int sportValid,
    required int sportType,
    required int sportTime,
    required int sportSteps,
    required int sportKcal,
    required int sportDistance,
    required int sportSpeed,
    required int sportCadence,
    required int sportStride,
  }) async {
    final response = await sendRequest(MicrowearDeviceControl.gpsSport, data: {
      "func": "syncGPSData",
      "sportHr": sportHr,
      "sportValid": sportValid,
      "sportType": sportType,
      "sportTime": sportTime,
      "sportSteps": sportSteps,
      "sportKcal": sportKcal,
      "sportDistance": sportDistance,
      "sportSpeed": sportSpeed,
      "sportCadence": sportCadence,
      "sportStride": sportStride
    });
    return response;
  }

  /// Stream for device data received events.
  final EventChannel _deviceDataReceivedChannel =
      const EventChannel('deviceDataReceived');
  Stream get deviceDataReceivedStream => _deviceDataReceivedChannel
      .receiveBroadcastStream(_deviceDataReceivedChannel.name)
      .cast();

  /// Stream for battery level events.
  final EventChannel _getBatteryLevelChannel =
      const EventChannel('getBatteryLevel');
  Stream get getBatteryLevelStream =>
      _getBatteryLevelChannel.receiveBroadcastStream().cast();

  /// Stream for syncing hourly steps data.
  final EventChannel _syncHourStepChannel = const EventChannel('syncHourStep');
  Stream get syncHourStepStream =>
      _syncHourStepChannel.receiveBroadcastStream().cast();

  /// Stream for syncing weekday sports data.
  final EventChannel _syncWeekDaySportsChannel =
      const EventChannel('syncWeekDaySports');
  Stream get syncWeekDaySportsStream =>
      _syncWeekDaySportsChannel.receiveBroadcastStream().cast();

  /// Stream for device configuration events.
  final EventChannel _deviceConfigChannel = const EventChannel('deviceConfig');
  Stream get deviceConfigStream =>
      _deviceConfigChannel.receiveBroadcastStream().cast();

  /// Stream for syncing sleep data.
  final EventChannel _syncSleepDataChannel =
      const EventChannel('syncSleepData');
  Stream get syncSleepDataStream =>
      _syncSleepDataChannel.receiveBroadcastStream().cast();

  /// Stream for syncing sport records.
  final EventChannel _syncSportRecordChannel =
      const EventChannel('syncSportRecord');
  Stream get syncSportRecordStream =>
      _syncSportRecordChannel.receiveBroadcastStream().cast();

  /// Stream for retrieving alarm clock information.
  final EventChannel _getAlarmClockInfoChannel =
      const EventChannel('getAlarmClockInfo');
  Stream get getAlarmClockInfoStream =>
      _getAlarmClockInfoChannel.receiveBroadcastStream().cast();

  /// Stream for syncing blood pressure data.
  final EventChannel _syncBloodPressureChannel =
      const EventChannel('syncBloodPressure');
  Stream get syncBloodPressureStream =>
      _syncBloodPressureChannel.receiveBroadcastStream().cast();

  /// Stream for syncing heart data.
  final EventChannel _syncHeartDataChannel =
      const EventChannel('syncHeartData');
  Stream get syncHeartDataStream =>
      _syncHeartDataChannel.receiveBroadcastStream().cast();

  /// Stream for syncing blood oxygen data.
  final EventChannel _syncOxDataChannel = const EventChannel('syncOxData');
  Stream get syncOxDataStream =>
      _syncOxDataChannel.receiveBroadcastStream().cast();

  /// Stream for syncing home data.
  final EventChannel _syncHomeDataChannel = const EventChannel('syncHomeData');
  Stream get syncHomeDataStream =>
      _syncHomeDataChannel.receiveBroadcastStream().cast();

  /// Stream for syncing real-time ECG data.
  final EventChannel _syncRealTimeECGChannel =
      const EventChannel('syncRealTimeECG');
  Stream get syncRealTimeECGStream =>
      _syncRealTimeECGChannel.receiveBroadcastStream().cast();

  /// Stream for device functionality events.
  final EventChannel _getDeviceFunChannel = const EventChannel('getDeviceFun');
  Stream get getDeviceFunStream =>
      _getDeviceFunChannel.receiveBroadcastStream().cast();

  /// Stream for retrieving additional device configuration information.
  final EventChannel _getDeviceConfig1Channel =
      const EventChannel('getDeviceConfig1');
  Stream get getDeviceConfig1Stream =>
      _getDeviceConfig1Channel.receiveBroadcastStream().cast();

  /// Stream for connection status callback events.
  final EventChannel _registerConnectStatuesCallBackChannel =
      const EventChannel('registerConnectStatuesCallBack');
  Stream get registerConnectStatuesCallBackStream =>
      _registerConnectStatuesCallBackChannel.receiveBroadcastStream().cast();

  /// Stream for somatosensory game callback events.
  final EventChannel _registerSomatosensoryGameCallbackChannel =
      const EventChannel('registerSomatosensoryGameCallback');
  Stream get registerSomatosensoryGameCallbackStream =>
      _registerSomatosensoryGameCallbackChannel.receiveBroadcastStream().cast();

  /// Stream for single heart and blood oxygen callback events.
  final EventChannel _registerSingleHeartOxBloodCallbackChannel =
      const EventChannel('registerSingleHeartOxBloodCallback');
  Stream get registerSingleHeartOxBloodCallbackStream =>
      _registerSingleHeartOxBloodCallbackChannel
          .receiveBroadcastStream()
          .cast();

  /// Stream for Mac3 callback events.
  final EventChannel _registerMac3CallBackChannel =
      const EventChannel('registerMac3CallBack');
  Stream get registerMac3CallBackStream =>
      _registerMac3CallBackChannel.receiveBroadcastStream().cast();

  /// Stream for GPS callback events.
  final EventChannel _registerGPSCallbackCallBackChannel =
      const EventChannel('registerGPSCallbackCallBack');
  Stream get registerGPSCallbackCallBackStream =>
      _registerGPSCallbackCallBackChannel.receiveBroadcastStream().cast();
}
