package id.doran.microwear_sdk

import android.app.Activity
import android.content.Context
import android.util.Log
import com.njj.njjsdk.callback.*
import com.njj.njjsdk.library.Code
import com.njj.njjsdk.manger.NJJOtaManage
import com.njj.njjsdk.manger.NjjProtocolHelper
import com.njj.njjsdk.protocol.cmd.ruiyu.*
import com.njj.njjsdk.protocol.entity.*
import com.njj.njjsdk.utils.ApplicationProxy
import com.njj.njjsdk.utils.LogUtil
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.Date

/** MicrowearSdkPlugin */
class MicrowearSdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  private lateinit var mContext : Context
  private lateinit var mActivity: Activity


  private var deviceDataReceivedChannel: EventChannel? = null
  private var deviceDataReceivedSink : EventChannel.EventSink? = null
  private val deviceDataReceivedHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      deviceDataReceivedSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var getBatteryLevelChannel: EventChannel? = null
  private var getBatteryLevelSink : EventChannel.EventSink? = null
  private val getBatteryLevelHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      getBatteryLevelSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncHourStepChannel: EventChannel? = null
  private var syncHourStepSink : EventChannel.EventSink? = null
  private val syncHourStepHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncHourStepSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncWeekDaySportsChannel: EventChannel? = null
  private var syncWeekDaySportsSink : EventChannel.EventSink? = null
  private val syncWeekDaySportsHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncWeekDaySportsSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var deviceConfigChannel: EventChannel? = null
  private var deviceConfigSink : EventChannel.EventSink? = null
  private val deviceConfigHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      deviceConfigSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var getAlarmClockInfoChannel: EventChannel? = null
  private var getAlarmClockInfoSink : EventChannel.EventSink? = null
  private val getAlarmClockInfoHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      getAlarmClockInfoSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncBloodPressureChannel: EventChannel? = null
  private var syncBloodPressureSink : EventChannel.EventSink? = null
  private val syncBloodPressureHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncBloodPressureSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncHeartDataChannel: EventChannel? = null
  private var syncHeartDataSink : EventChannel.EventSink? = null
  private val syncHeartDataHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncHeartDataSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncOxDataChannel: EventChannel? = null
  private var syncOxDataSink : EventChannel.EventSink? = null
  private val syncOxDataHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncOxDataSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncHomeDataChannel: EventChannel? = null
  private var syncHomeDataSink : EventChannel.EventSink? = null
  private val syncHomeDataHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncHomeDataSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncRealTimeECGChannel: EventChannel? = null
  private var syncRealTimeECGSink : EventChannel.EventSink? = null
  private val syncRealTimeECGHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncRealTimeECGSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var getDeviceFunChannel: EventChannel? = null
  private var getDeviceFunSink : EventChannel.EventSink? = null
  private val getDeviceFunHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      getDeviceFunSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var getDeviceConfig1Channel: EventChannel? = null
  private var getDeviceConfig1Sink : EventChannel.EventSink? = null
  private val getDeviceConfig1Handler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      getDeviceConfig1Sink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    Log.d("MicrowearSdkPlugin","onAttachedToEngine")
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "microwear_sdk")
    channel.setMethodCallHandler(this)
    mContext = flutterPluginBinding.applicationContext

    deviceDataReceivedChannel = EventChannel(flutterPluginBinding.binaryMessenger, "deviceDataReceived")
    deviceDataReceivedChannel!!.setStreamHandler(deviceDataReceivedHandler)

    getBatteryLevelChannel = EventChannel(flutterPluginBinding.binaryMessenger, "getBatteryLevel")
    getBatteryLevelChannel!!.setStreamHandler(getBatteryLevelHandler)

    syncHourStepChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncHourStep")
    syncHourStepChannel!!.setStreamHandler(syncHourStepHandler)

    syncWeekDaySportsChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncWeekDaySports")
    syncWeekDaySportsChannel!!.setStreamHandler(syncWeekDaySportsHandler)

    deviceConfigChannel = EventChannel(flutterPluginBinding.binaryMessenger, "deviceConfig")
    deviceConfigChannel!!.setStreamHandler(deviceConfigHandler)

    getAlarmClockInfoChannel = EventChannel(flutterPluginBinding.binaryMessenger, "getAlarmClockInfo")
    getAlarmClockInfoChannel!!.setStreamHandler(getAlarmClockInfoHandler)

    syncBloodPressureChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncBloodPressure")
    syncBloodPressureChannel!!.setStreamHandler(syncBloodPressureHandler)

    syncHeartDataChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncHeartData")
    syncHeartDataChannel!!.setStreamHandler(syncHeartDataHandler)

    syncOxDataChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncOxData")
    syncOxDataChannel!!.setStreamHandler(syncOxDataHandler)

    syncHomeDataChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncHomeData")
    syncHomeDataChannel!!.setStreamHandler(syncHomeDataHandler)

    syncRealTimeECGChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncRealTimeECG")
    syncRealTimeECGChannel!!.setStreamHandler(syncRealTimeECGHandler)

    getDeviceFunChannel = EventChannel(flutterPluginBinding.binaryMessenger, "getDeviceFun")
    getDeviceFunChannel!!.setStreamHandler(getDeviceFunHandler)

    getDeviceConfig1Channel = EventChannel(flutterPluginBinding.binaryMessenger, "getDeviceConfig1")
    getDeviceConfig1Channel!!.setStreamHandler(getDeviceConfig1Handler)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
      "sendRequest" -> {
        val microwearDeviceControl: Int? = call.argument<Int>("microwearDeviceControl")
        val data: Map<String, Any>? = call.argument<Map<String, Any>>("data")
        if (microwearDeviceControl != null) {
          when (microwearDeviceControl) {
            //Watch face push
            -1 -> {

            }

            EVT_TYPE_ALERT_FIND_WATCH -> {
              NjjProtocolHelper.getInstance().findMe(object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Find watch successful")
                  //"Find watch successful"
                }

                override fun onWriteFail() {
                  LogUtil.e("Find watch failed")
                  //"Find watch failed"
                }
              })
            }

            EVT_TYPE_TP_VER -> {
              NjjProtocolHelper.getInstance().getTPDeviceInfo()
            }

            EVT_TYPE_FIRMWARE_VER -> {
              NjjProtocolHelper.getInstance().getDeviceInfo(object : NjjFirmwareCallback {
                override fun onFirmwareSuccess(firmware: String) {
                  LogUtil.e("Firmware version: $firmware")
                  //"Firmware version: $firmware"
                }

                override fun onFirmwareFail() {
                  LogUtil.e("Get failed")
                  //"Get device info failed"
                }
              })
            }

            EVT_TYPE_UI_VER -> {
              NjjProtocolHelper.getInstance().getUIDeviceInfo()
            }

            EVT_TYPE_BAT -> {
              NjjProtocolHelper.getInstance().getBatteryLevel(object : NjjBatteryCallBack {
                override fun onSuccess(batteryLevel: Int) {
                  LogUtil.e("Battery level: $batteryLevel")
                  //"Battery level: $batteryLevel"
                }

                override fun onFail() {
                  LogUtil.e("Get battery level failed")

                }
              })
            }

            EVT_TYPE_HOUR_STEP -> {
              NjjProtocolHelper.getInstance().syncHourStep().subscribe {
                LogUtil.e("Get hourly steps successful")
                var stringBuffer = StringBuffer()
                it.forEach {
                  stringBuffer.append("Steps: ${it.stepNum}  Calories: ${it.calData}")
                }
                //stringBuffer.toString()
              }
            }

            EVT_TYPE_HISTORY_SPORT_DATA -> {
              NjjProtocolHelper.getInstance().syncWeekDaySports().subscribe {
                LogUtil.e("Get seven days data successful")
                //"Get seven days data successful. Count: ${it.size}"
              }
            }

            EVT_TYPE_BAND_CONFIG -> {
              NjjProtocolHelper.getInstance().deviceConfig.subscribe {
                LogUtil.e("Band info: $it")
                //it.toString()
              }
            }

            EVT_TYPE_SLEEP_DATA -> {
              NjjProtocolHelper.getInstance().syncSleepData().subscribe {
                LogUtil.e("Sleep data callback successful")
                var sleepDetailList = it.sleepDetailList
                var sleepTime = it.sleepTime
              }
            }

            EVT_TYPE_SPORT_RECORD -> {
              NjjProtocolHelper.getInstance().syncSportRecord().subscribe {
                LogUtil.e("Exercise data")
                //"Sleep data callback successful Date: ${it.date} Steps: ${it.stepNum}"
              }
            }

            EVT_TYPE_UNIT_SYSTEM -> {
              val pos = 2
              when (pos) {
                2 -> NjjProtocolHelper.getInstance().setUnitFormat(true, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Set unit successful"
                  }

                  override fun onWriteFail() {
                    //"Set unit failed"
                  }
                })

                3 -> NjjProtocolHelper.getInstance()
                  .setUnitFormat(false, object : NjjWriteCallback {
                    override fun onWriteSuccess() {
                      LogUtil.e("Command sent successfully")
                      //"Set unit successful"
                    }

                    override fun onWriteFail() {
                      //"Set unit failed"
                    }
                  })
              }
            }

            EVT_TYPE_TIME_MODE -> {
              val pos = 5
              when (pos) {
                5 -> NjjProtocolHelper.getInstance()
                  .setTimeFormat(false, object : NjjWriteCallback {
                    override fun onWriteSuccess() {
                      LogUtil.e("Command sent successfully")
                      //"Set time format successful"
                    }

                    override fun onWriteFail() {
                      //"Set time format failed"
                    }
                  })

                6 -> NjjProtocolHelper.getInstance().setTimeFormat(true, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    //"Set time format successful"
                  }

                  override fun onWriteFail() {
                    //"Set time format failed"
                  }
                })
              }
            }

            EVT_TYPE_TEMP_UNIT -> {
              val pos = 7
              when (pos) {
                7 -> NjjProtocolHelper.getInstance().setTempUnit(false, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    //"Command send failed"
                  }
                })

                8 -> NjjProtocolHelper.getInstance().setTempUnit(true, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    //"Command send failed"
                  }
                })
              }
            }

            EVT_TYPE_DATE_TIME -> {
              NjjProtocolHelper.getInstance().syncTime(object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully")
                  //"Time sync successful"
                }

                override fun onWriteFail() {
                  //"Time sync failed"
                }
              })
            }

            EVT_TYPE_TARGET_STEP -> {
              NjjProtocolHelper.getInstance().setTargetStep(800, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully")
                  //"Set target steps successful"
                }

                override fun onWriteFail() {
                  //"Set target steps failed"
                }
              })
            }

            EVT_TYPE_DISPLAY_TIME -> {
              NjjProtocolHelper.getInstance().setDisplayTime(10, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully")
                  //"Set screen-on duration successful"
                }

                override fun onWriteFail() {
                  //"Set screen-on duration failed"
                }
              })
            }

            EVT_TYPE_REAL_TIME_WEATHER -> {
              var syncWeatherData = NjjSyncWeatherData()
              syncWeatherData.tempData = 22
              syncWeatherData.weatherType = 5

              NjjProtocolHelper.getInstance()
                .syncWeatherTypeData(syncWeatherData, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Set weather successful"
                  }

                  override fun onWriteFail() {
                    //"Set weather failed"
                  }
                })
            }

            EVT_TYPE_RAISE_WRIST -> {
              var entity = NjjWristScreenEntity()
              entity.wristScreenStatus = 1
              entity.wristScreenBeginTime = 510
              entity.wristScreenEndTime = 1120
              NjjProtocolHelper.getInstance().upHandleScreenOn(entity, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully $entity")
                  //"Set raise to wake successful"
                }

                override fun onWriteFail() {
                  //"Set raise to wake failed"
                }
              })
            }

            EVT_TYPE_DISTURB -> {
              var entity = NjjDisturbEntity()
              entity.disturbStatus = 1
              entity.disturbInterval = 5
              entity.disturbBeginTime = 510
              entity.disturbEndTime = 1120
              NjjProtocolHelper.getInstance().syncNoDisturbSet(entity, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully $entity")
                  //"Set Do Not Disturb successful"
                }

                override fun onWriteFail() {
                  //"Set Do Not Disturb failed"
                }
              })
            }

            EVT_TYPE_DRINK_WATER -> {
              var entity = NjjDrinkWaterEntity()
              entity.drinkWaterStatus = 1
              entity.drinkWaterInterval = 5
              entity.drinkWaterBeginTime = 510
              entity.drinkWaterEndTime = 1120
              NjjProtocolHelper.getInstance().syncWaterNotify(entity, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully $entity")
                  LogUtil.e("Command sent successfully")
                  //"Set water reminder successful"
                }

                override fun onWriteFail() {
                  //"Command send failed"
                  //"Set water reminder failed"
                }
              })
            }

            EVT_TYPE_WASH_HAND -> {
              var njjWashHandEntity = NjjWashHandEntity()
              njjWashHandEntity.washHandStatus = 1
              njjWashHandEntity.washHandInterval = 5
              njjWashHandEntity.washHandBeginTime = 510
              njjWashHandEntity.washHandEndTime = 1120
              NjjProtocolHelper.getInstance()
                .syncWashNotify(njjWashHandEntity, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully $njjWashHandEntity")
                    //"Set Do Not Disturb successful"
                  }

                  override fun onWriteFail() {
                    //"Set Do Not Disturb failed"
                  }
                })
            }

            EVT_TYPE_LONG_SIT -> {
              var commonSetEntity = NjjLongSitEntity()
              commonSetEntity.longSitStatus = 1
              commonSetEntity.longSitInterval = 5
              commonSetEntity.longSitBeginTime = 510
              commonSetEntity.longSitEndTime = 1120
              NjjProtocolHelper.getInstance()
                .syncLongSitNotify(commonSetEntity, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully=$commonSetEntity")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    //"Command send failed"
                  }
                })
            }

            EVT_TYPE_ALARM -> {
              val pos = 38
              when (pos) {
                38 -> {
                  NjjProtocolHelper.getInstance().getAlarmClockInfo().subscribe {
                    LogUtil.e("Get alarm data successful")
                    //"Get alarm data successful ${it.forEach { it -> it.alarmCycle }}"
                  }
                }

                else -> {
                  // Add default data if empty
                  val infos: MutableList<NjjAlarmClockInfo> = ArrayList<NjjAlarmClockInfo>()
                  val timeArr = intArrayOf(
                    6 * 3600,
                    95 * 360,
                    12 * 3600,
                    18 * 3600,
                    225 * 360,
                    12 * 3600,
                    18 * 3600,
                    225 * 360
                  )
                  val cycleArr = intArrayOf(62, 65, 127, 65, 127, 127, 65, 127)
                  var count = 4

                  for (i in 0 until count) {
                    infos.add(
                      NjjAlarmClockInfo(
                        i.toString(), timeArr[i], cycleArr[i], false, true, 0
                      )
                    )
                  }
                  NjjProtocolHelper.getInstance()
                    .syncAlarmClockInfo(infos, object : NjjWriteCallback {
                      override fun onWriteSuccess() {
                        LogUtil.e("Command sent successfully")
                        //"Alarm setting successful"
                      }

                      override fun onWriteFail() {
                        //"Alarm setting failed"
                      }
                    })
                }
              }
            }

            EVT_TYPE_BP_DAY -> {
              NjjProtocolHelper.getInstance().syncBloodPressure().subscribe {
                LogUtil.e("All-day blood pressure callback successful")
                var stringBuffer = StringBuffer()
                stringBuffer.append("All-day BP=")
                it.forEach { njj ->
                  stringBuffer.append("${njj.diastolicPressure} ")
                }
                //stringBuffer
              }
            }

            EVT_TYPE_HR_DAY -> {
              NjjProtocolHelper.getInstance().syncHeartData().subscribe { it ->
                LogUtil.e("All-day heart rate callback successful")
                var stringBuffer = StringBuffer()
                stringBuffer.append("All-day HR=")
                it.forEach { njj ->
                  stringBuffer.append("${njj.heartRate} ")
                }
                //stringBuffer
              }

              /* NjjBleHelper.getInstance().syncBloodPressure().subscribe {
                 LogUtil.e("全天血压回调成功")
                 it.forEach { njj ->
                     LogUtil.e(njj.diastolicPressure)
                 }
             }

             NjjBleHelper.getInstance().syncOxData().subscribe {
                 LogUtil.e("全天血氧回调成功")
                 it.forEach { njj ->
                     LogUtil.e(njj.bloodOxy)
                 }
             }*/
            }

            EVT_TYPE_BO_DAY -> {
              NjjProtocolHelper.getInstance().syncOxData().subscribe {
                var stringBuffer = StringBuffer()
                stringBuffer.append("All-day SpO2=")
                it.forEach { njj ->
                  stringBuffer.append("${njj.bloodOxy} ")
                }
                //stringBuffer
              }
            }

            EVT_TYPE_ALL_DAY_FALG -> {
              NjjProtocolHelper.getInstance().syncHeartMonitor(1, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.e("Command sent successfully")
                  //"Command sent successfully"
                }

                override fun onWriteFail() {
                  //"Command send failed"
                }
              })
            }

            EVT_TYPE_TAKE_PHOTO -> {
              val pos = 35
              when (pos) {
                35 -> NjjProtocolHelper.getInstance()
                  .openTakePhotoCamera(true, object : NjjWriteCallback {
                    override fun onWriteSuccess() {
                      LogUtil.e("Command sent successfully")
                      //"Command sent successfully"
                    }

                    override fun onWriteFail() {
                      //"Command send failed"
                    }
                  })

                36 -> NjjProtocolHelper.getInstance()
                  .openTakePhotoCamera(false, object : NjjWriteCallback {
                    override fun onWriteSuccess() {
                      LogUtil.e("Command sent successfully")
                      //"Command sent successfully"
                    }

                    override fun onWriteFail() {
                      //"Command send failed"
                    }
                  })
              }
            }

            // Message push
            EVT_TYPE_ALERT_MSG -> {
              NjjProtocolHelper.getInstance()
                .setNotify(4, "Hello", "Test Message", object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    //"Command send failed"
                  }
                })
            }

            // Contact push
            EVT_TYPE_OTA_START -> {
            }

            EVT_TYPE_ADD_FRIEND -> {
              NjjProtocolHelper.getInstance().pushQRCode(0, "https://blog.csdn.net").subscribe {
                if (it == 1) {
                  LogUtil.e("Push successful")
                  //"Push successful"
                }
              }
            }

            EVT_TYPE_RECEIPT_CODE -> {
              NjjProtocolHelper.getInstance().pushPayCode(12, "https://blog.csdn.net").subscribe {
                LogUtil.e("Push successful")
                //"Push successful"
              }
            }

            EVT_TYPE_ANDROID_PHONE_CTRL -> {
              val pos = 43;
              when (pos) {
                43 -> NjjProtocolHelper.getInstance().handUpPhone(0, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    LogUtil.e("Command send failed")
                    //"Command send failed"
                  }
                })

                44 -> NjjProtocolHelper.getInstance().handUpPhone(1, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.e("Command sent successfully")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    LogUtil.e("Command send failed")
                    //"Command send failed"
                  }
                })
              }
            }

            EVT_TYPE_APP_REQUEST_SYNC -> {
              NjjProtocolHelper.getInstance().syncHomeData(object : NjjHomeDataCallBack {
                override fun homeDataCallBack(
                  njjEcgData: NjjEcgData,
                  njjHeartData: NjjHeartData,
                  njjStepData: NjjStepData,
                  njjBloodPressure: NjjBloodPressure,
                  njjBloodOxyData: NjjBloodOxyData
                ) {
                  LogUtil.e("njjStepData={${njjStepData.stepNum}}")
                }
              })
            }

            EVT_TYPE_WOMEN_HEALTH -> {
              var time = System.currentTimeMillis() - 1000
              var date = Date()
              date.time = System.currentTimeMillis()
              NjjProtocolHelper.getInstance().syncFemaleHealth(1, 3, 29, 7, time, date, 1, time)

              NjjProtocolHelper.getInstance().syncRealTimeECG(false, object : NjjECGCallBack {
                override fun onReceivePPGData(type: Int, time: Int, heart: Int) {
                  LogUtil.e("type=$type  heart=$heart")
                }

                override fun onECGReceiveEnd(type: Int) {
                  LogUtil.e("type=$type")
                }
              })
            }

            EVT_TYPE_ECG_HR -> {
              NjjProtocolHelper.getInstance().syncRealTimeECG(true, object : NjjECGCallBack {
                override fun onReceivePPGData(type: Int, time: Int, heart: Int) {
                  LogUtil.e("type=$type  heart=$heart")
                }

                override fun onECGReceiveEnd(type: Int) {
                  LogUtil.e("type=$type")
                }
              })
            }

            EVT_TYPE_DEVICE_FUN -> {
              NjjProtocolHelper.getInstance().getDeviceFun(object : NjjDeviceFunCallback {
                override fun onDeviceFunSuccess(bleDeviceFun: BleDeviceFun) {
                  Log.e("fan", "fan$bleDeviceFun")
                }

                override fun onDeviceFunFail() {
                }
              })
            }

            EVT_TYPE_BAND_CONFIG1 -> {
              NjjProtocolHelper.getInstance().getDeviceConfig1(object : NjjConfig1CallBack {
                override fun onWriteSuccess() {
                }

                override fun onLongSitEntity(njjLongSitEntity: NjjLongSitEntity) {
                  LogUtil.e(njjLongSitEntity.toString())
                }

                override fun onNjjDrinkWaterEntity(njjDrinkWaterEntity: NjjDrinkWaterEntity) {
                  LogUtil.e(njjDrinkWaterEntity.toString())
                }

                override fun onNjjWashHandEntity(njjWashHandEntity: NjjWashHandEntity) {
                  LogUtil.e(njjWashHandEntity.toString())
                }

                override fun onNjjWristScreenEntity(njjWristScreenEntity: NjjWristScreenEntity) {
                  LogUtil.e(njjWristScreenEntity.toString())
                }

                override fun onNjjDisturbEntity(njjDisturbEntity: NjjDisturbEntity) {
                  LogUtil.e(njjDisturbEntity.toString())
                }

                override fun onNjjMedicineEntity(njjMedicineEntity: NjjMedicineEntity) {
                  LogUtil.e(njjMedicineEntity.toString())
                }

                override fun onFail() {
                }
              })
            }
          }
        }
      }
       else -> result.notImplemented()
      }
    }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    Log.d("MicrowearSdkPlugin","onDetachedFromActivity")
  }



  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.d("MicrowearSdkPlugin","onAttachedToActivity")
    mActivity = binding.activity
    NJJOtaManage.getInstance().init(mActivity.application)
    ApplicationProxy.getInstance().setApplication(mActivity.application)
    CallBackManager.getInstance().registerConnectStatuesCallBack("", object : ConnectStatuesCallBack.ICallBack {
      override fun onConnected(mac: String?) {
        LogUtil.e("Connection successful")
      }

      override fun onConnecting(mac: String?) {
        LogUtil.e("Connecting")
      }

      override fun onDisConnected(mac: String?) {
        LogUtil.e("Disconnect")
      }

      override fun onConnectFail(code: Int) {
        LogUtil.e("Connection failed")
      }

      override fun onDiscoveredServices(code: Int) {
        LogUtil.e("onDiscoveredServices")
        if (code == Code.REQUEST_SUCCESS) {
          LogUtil.e("onDiscoveredServices : REQUEST_SUCCESS")
        }
      }

    })
    CallBackManager.getInstance().registerSomatosensoryGameCallback(object :SomatosensoryGameCallback.ICallBack{
      override fun onReceiveData(somatosensoryGame: SomatosensoryGame?) {
        LogUtil.e(somatosensoryGame.toString())
      }

      override fun onReceiveStatus(status: Int) {
        LogUtil.e("onReceiveStatus: $status")
      }

    })

    NjjProtocolHelper.getInstance().registerSingleHeartOxBloodCallback(
      object : NjjNotifyCallback {
        override fun onBloodPressureData(systolicPressure: Int, diastolicPressure: Int) {
          LogUtil.e("Single Blood Pressure: $systolicPressure/$diastolicPressure")
        }

        override fun onHeartRateData(rate: Int) {
          LogUtil.e("Single Heart Rate: $rate")
        }

        override fun onOxyData(rate: Int) {
          LogUtil.e("Single Blood Oxygen: $rate")
        }

        override fun takePhone(value: Int) {
          LogUtil.e("Take Photo: $value")
        }

        override fun onStepData(njjStepData: NjjStepData?) {
          LogUtil.e("Calories: ${njjStepData?.calData} Steps: ${njjStepData?.stepNum} Distance: ${njjStepData?.distance}")
        }

        override fun findPhone(value: Int) {
          LogUtil.e("Find Phone: $value")
        }

        override fun endCallPhone(value: Int) {
          if (value == 0) {
            LogUtil.e("End Call")
          }
        }
      })

  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.d("MicrowearSdkPlugin","onDetachedFromActivityForConfigChanges")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.d("MicrowearSdkPlugin","onReattachedToActivityForConfigChanges")
  }

  override fun onDetachedFromActivity() {
     Log.d("MicrowearSdkPlugin","onDetachedFromActivity")
  }
}
