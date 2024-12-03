package id.doran.microwear_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.njj.njjsdk.callback.*
import com.njj.njjsdk.library.Code
import com.njj.njjsdk.manger.NJJOtaManage
import com.njj.njjsdk.manger.NjjBleManger
import com.njj.njjsdk.manger.NjjProtocolHelper
import com.njj.njjsdk.protocol.cmd.*
import com.njj.njjsdk.protocol.entity.*
import com.njj.njjsdk.utils.ApplicationProxy
import com.njj.njjsdk.utils.BleBeaconUtil
import com.njj.njjsdk.utils.LogUtil
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Date


/** MicrowearSdkPlugin */
class MicrowearSdkPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  private lateinit var mContext : Context
  private lateinit var mActivity: Activity
  private var gson = Gson()

  fun createBLEDeviceFromMac(macAddress: String?): BLEDevice? {
    // Obtain the Bluetooth adapter
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    // Ensure Bluetooth is enabled and the MAC address is valid
    if (bluetoothAdapter != null && BluetoothAdapter.checkBluetoothAddress(macAddress)) {
      val device = bluetoothAdapter.getRemoteDevice(macAddress)

      // Create and populate the BLEDevice
      val bleDevice = BLEDevice().apply {
        this.device = device
        this.rssi = -100 // Set a default RSSI value
        this.scanRecord = generateScanRecordWithNJYID() // Generate scan record with NJYID "ID" field
      }

      return bleDevice
    }
    return null // Return null if unable to create the BLEDevice
  }


  // Helper function to create a synthetic scan record with the NJYID in the correct format
  private fun generateScanRecordWithNJYID(): ByteArray {
    val idBytes = byteArrayOf(0xBC.toByte(), 0xBC.toByte()) // "BCBC" as hex bytes
    val manufacturerData = byteArrayOf(0xFF.toByte()) + idBytes // Manufacturer data type (0xFF) + ID bytes

    val buffer = ByteBuffer.allocate(manufacturerData.size + 2)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.put((manufacturerData.size).toByte()) // Length of this data field
    buffer.put(manufacturerData) // Manufacturer data with "ID"

    return buffer.array()
  }

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

  private var syncSleepDataChannel: EventChannel? = null
  private var syncSleepDataSink : EventChannel.EventSink? = null
  private val syncSleepDataHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncSleepDataSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var syncSportRecordChannel: EventChannel? = null
  private var syncSportRecordSink : EventChannel.EventSink? = null
  private val syncSportRecordHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      syncSportRecordSink = eventSink
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

  private var registerConnectStatuesCallBackChannel: EventChannel? = null
  private var registerConnectStatuesCallBackSink : EventChannel.EventSink? = null
  private val registerConnectStatuesCallBackHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      registerConnectStatuesCallBackSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var registerSomatosensoryGameCallbackChannel: EventChannel? = null
  private var registerSomatosensoryGameCallbackSink : EventChannel.EventSink? = null
  private val registerSomatosensoryGameCallbackHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      registerSomatosensoryGameCallbackSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var registerSingleHeartOxBloodCallbackChannel: EventChannel? = null
  private var registerSingleHeartOxBloodCallbackSink : EventChannel.EventSink? = null
  private val registerSingleHeartOxBloodCallbackHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      registerSingleHeartOxBloodCallbackSink = eventSink
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

    syncSleepDataChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncSleepData")
    syncSleepDataChannel!!.setStreamHandler(syncSleepDataHandler)

    syncSportRecordChannel = EventChannel(flutterPluginBinding.binaryMessenger, "syncSportRecord")
    syncSportRecordChannel!!.setStreamHandler(syncSportRecordHandler)

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

    registerConnectStatuesCallBackChannel = EventChannel(flutterPluginBinding.binaryMessenger, "registerConnectStatuesCallBack")
    registerConnectStatuesCallBackChannel!!.setStreamHandler(registerConnectStatuesCallBackHandler)

    registerSomatosensoryGameCallbackChannel = EventChannel(flutterPluginBinding.binaryMessenger, "registerSomatosensoryGameCallback")
    registerSomatosensoryGameCallbackChannel!!.setStreamHandler(registerSomatosensoryGameCallbackHandler)

    registerSingleHeartOxBloodCallbackChannel = EventChannel(flutterPluginBinding.binaryMessenger, "registerSingleHeartOxBloodCallback")
    registerSingleHeartOxBloodCallbackChannel!!.setStreamHandler(registerSingleHeartOxBloodCallbackHandler)

  }


  @SuppressLint("CheckResult")
  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
      "connect" -> {
        val macAddress = call.argument<String>("macAddress")
        LogUtil.d("connect $macAddress")
        val bleDevice = createBLEDeviceFromMac(macAddress)
        if (bleDevice != null) {
          LogUtil.d("bleDevice != null")
          NjjBleManger.getInstance().clearRequest(bleDevice.device.address)

          val beaconMap = BleBeaconUtil.parseData(bleDevice.getScanRecord())
          val id = beaconMap["ID"]
          if (id != null) {
            if (!id.startsWith("BCBC")) {
                  LogUtil.d("connect fail no BCBC: ${beaconMap}")
            }else{
               NjjBleManger.getInstance().connectionRequest(bleDevice)
            }
          }else{
            LogUtil.d("connect fail no id: ${beaconMap}")
          }
        }
      }
      "disconnect" -> {
        NjjBleManger.getInstance().disConnection()
      }
      "creteBond" -> {
        val macAddress = call.argument<String>("macAddress")
        NjjBleManger.getInstance().creteBond(macAddress)
      }
      "getBondState" -> {
        val macAddress = call.argument<String>("macAddress")
        NjjBleManger.getInstance().bluetoothClient.getBondState(macAddress)
      }
      "getConnectStatus" -> {
        val macAddress = call.argument<String>("macAddress")
        NjjBleManger.getInstance().bluetoothClient.getConnectStatus(macAddress)
      }
      "registerConnectStatue" -> {
        val macAddress = call.argument<String>("macAddress")
        NjjBleManger.getInstance().registerConnectStatue(macAddress)
      }
      "unregisterConnectStatue" -> {
        NjjBleManger.getInstance().unregisterConnectStatue()
      }
      "sendRequest" -> {
        val microwearDeviceControl: Int? = call.argument<Int>("microwearDeviceControl")
        val data: Map<String, Any?>? = call.argument<Map<String, Any?>>("data")
        if (microwearDeviceControl != null) {
          when (microwearDeviceControl) {
            //Watch face push
            -1 -> {

            }

            EVT_TYPE_ALERT_FIND_WATCH -> {
              NjjProtocolHelper.getInstance().findMe(object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Find watch successful")
                  //"Find watch successful"
                }

                override fun onWriteFail() {
                  LogUtil.d("Find watch failed")
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
                  LogUtil.d("Firmware version: $firmware")
                  result.success(firmware)
                  //"Firmware version: $firmware"
                }

                override fun onFirmwareFail() {
                  LogUtil.d("Get failed")
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
                  LogUtil.d("Battery level: $batteryLevel")
                  //"Battery level: $batteryLevel"
                  getBatteryLevelSink?.success(batteryLevel)
                }

                override fun onFail() {
                  LogUtil.d("Get battery level failed")

                }
              })
            }

            EVT_TYPE_HOUR_STEP -> {
              NjjProtocolHelper.getInstance().syncHourStep().subscribe {
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncHourStepSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncHourStepSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_HISTORY_SPORT_DATA -> {
              NjjProtocolHelper.getInstance().syncWeekDaySports().subscribe {
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncWeekDaySportsSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncWeekDaySportsSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_BAND_CONFIG -> {
              NjjProtocolHelper.getInstance().deviceConfig.subscribe {
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    deviceConfigSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    deviceConfigSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_SLEEP_DATA -> {
              NjjProtocolHelper.getInstance().syncSleepData().subscribe {
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncSleepDataSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncSleepDataSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_SPORT_RECORD -> {
              NjjProtocolHelper.getInstance().syncSportRecord().subscribe {
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncSportRecordSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncSportRecordSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_UNIT_SYSTEM -> {
              var isMetricSystem = true

              isMetricSystem = data?.get("isMetricSystem") as? Boolean ?: isMetricSystem
              NjjProtocolHelper.getInstance().setUnitFormat(isMetricSystem, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Set unit successful"
                }

                override fun onWriteFail() {
                  //"Set unit failed"
                  LogUtil.d("Command sent failed")
                }
              })

            }

            EVT_TYPE_TIME_MODE -> {
              var is24 = true
              is24 = data?.get("is24") as? Boolean ?: is24

              NjjProtocolHelper.getInstance()
                .setTimeFormat(is24, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.d("Command sent successfully")
                    //"Set time format successful"
                  }

                  override fun onWriteFail() {
                    //"Set time format failed"
                  }
                })
            }

            EVT_TYPE_TEMP_UNIT -> {
              var isCen = true

              isCen = data?.get("isCen") as? Boolean ?: isCen


              NjjProtocolHelper.getInstance().setTempUnit(isCen, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Command sent successfully"
                }

                override fun onWriteFail() {
                  //"Command send failed"
                }
              })
            }

            EVT_TYPE_DATE_TIME -> {
              NjjProtocolHelper.getInstance().syncTime(object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Time sync successful"
                }

                override fun onWriteFail() {
                  //"Time sync failed"
                }
              })
            }

            EVT_TYPE_TARGET_STEP -> {
              var step = 800

              step = data?.get("step") as? Int ?: step

              NjjProtocolHelper.getInstance().setTargetStep(step, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Set target steps successful"
                }

                override fun onWriteFail() {
                  //"Set target steps failed"
                }
              })
            }

            EVT_TYPE_DISPLAY_TIME -> {
              var time = 10

              time = data?.get("time") as? Int ?: time

              NjjProtocolHelper.getInstance().setDisplayTime(time, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Set screen-on duration successful"
                }

                override fun onWriteFail() {
                  //"Set screen-on duration failed"
                }
              })
            }

            EVT_TYPE_REAL_TIME_WEATHER -> {
              var tempData = 22
              var weatherType = 5

              data?.let {
                tempData = it["tempData"] as? Int ?: tempData
                weatherType = it["weatherType"] as? Int ?: weatherType
              }
              var syncWeatherData = NjjSyncWeatherData()
              syncWeatherData.tempData = tempData
              syncWeatherData.weatherType = weatherType

              NjjProtocolHelper.getInstance()
                .syncWeatherTypeData(syncWeatherData, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.d("Command sent successfully")
                    //"Set weather successful"
                  }

                  override fun onWriteFail() {
                    //"Set weather failed"
                  }
                })
            }

            EVT_TYPE_RAISE_WRIST -> {
              var wristScreenStatus = 1
              var wristScreenBeginTime = 510
              var wristScreenEndTime = 1120
              data?.let {
                wristScreenStatus = it["wristScreenStatus"] as? Int ?: wristScreenStatus
                wristScreenBeginTime = it["wristScreenBeginTime"] as? Int ?: wristScreenBeginTime
                wristScreenEndTime = it["wristScreenEndTime"] as? Int ?: wristScreenEndTime
              }
              var entity = NjjWristScreenEntity()
              entity.wristScreenStatus = wristScreenStatus
              entity.wristScreenBeginTime = wristScreenBeginTime
              entity.wristScreenEndTime = wristScreenEndTime
              NjjProtocolHelper.getInstance().upHandleScreenOn(entity, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully $entity")
                  //"Set raise to wake successful"
                }

                override fun onWriteFail() {
                  //"Set raise to wake failed"
                }
              })
            }

            EVT_TYPE_DISTURB -> {
              var disturbStatus = 1
              var disturbInterval = 5
              var disturbBeginTime = 510
              var disturbEndTime = 1120
              data?.let {
                disturbStatus = it["disturbStatus"] as? Int ?: disturbStatus
                disturbInterval = it["disturbInterval"] as? Int ?: disturbInterval
                disturbBeginTime = it["disturbBeginTime"] as? Int ?: disturbBeginTime
                disturbEndTime = it["disturbEndTime"] as? Int ?: disturbEndTime
              }
              var entity = NjjDisturbEntity()
              entity.disturbStatus = disturbStatus
              entity.disturbInterval = disturbInterval
              entity.disturbBeginTime = disturbBeginTime
              entity.disturbEndTime = disturbEndTime
              NjjProtocolHelper.getInstance().syncNoDisturbSet(entity, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully $entity")
                  //"Set Do Not Disturb successful"
                }

                override fun onWriteFail() {
                  //"Set Do Not Disturb failed"
                }
              })
            }

            EVT_TYPE_DRINK_WATER -> {
              var drinkWaterStatus = 1
              var drinkWaterInterval = 5
              var drinkWaterBeginTime = 510
              var drinkWaterEndTime = 1120
              data?.let {
                drinkWaterStatus = it["drinkWaterStatus"] as? Int ?: drinkWaterStatus
                drinkWaterInterval = it["drinkWaterInterval"] as? Int ?: drinkWaterInterval
                drinkWaterBeginTime = it["drinkWaterBeginTime"] as? Int ?: drinkWaterBeginTime
                drinkWaterEndTime = it["drinkWaterEndTime"] as? Int ?: drinkWaterEndTime
              }
              var entity = NjjDrinkWaterEntity()
              entity.drinkWaterStatus = drinkWaterStatus
              entity.drinkWaterInterval = drinkWaterInterval
              entity.drinkWaterBeginTime = drinkWaterBeginTime
              entity.drinkWaterEndTime = drinkWaterEndTime
              NjjProtocolHelper.getInstance().syncWaterNotify(entity, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully $entity")
                  LogUtil.d("Command sent successfully")
                  //"Set water reminder successful"
                }

                override fun onWriteFail() {
                  //"Command send failed"
                  //"Set water reminder failed"
                }
              })
            }

            EVT_TYPE_WASH_HAND -> {
              var washHandStatus = 1
              var washHandInterval = 5
              var washHandBeginTime = 510
              var washHandEndTime = 1120
              data?.let {
                washHandStatus = it["washHandStatus"] as? Int ?: washHandStatus
                washHandInterval = it["washHandInterval"] as? Int ?: washHandInterval
                washHandBeginTime = it["washHandBeginTime"] as? Int ?: washHandBeginTime
                washHandEndTime = it["washHandEndTime"] as? Int ?: washHandEndTime
              }

              var njjWashHandEntity = NjjWashHandEntity()
              njjWashHandEntity.washHandStatus = washHandStatus
              njjWashHandEntity.washHandInterval = washHandInterval
              njjWashHandEntity.washHandBeginTime = washHandBeginTime
              njjWashHandEntity.washHandEndTime = washHandEndTime
              NjjProtocolHelper.getInstance()
                .syncWashNotify(njjWashHandEntity, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.d("Command sent successfully $njjWashHandEntity")
                    //"Set Do Not Disturb successful"
                  }

                  override fun onWriteFail() {
                    //"Set Do Not Disturb failed"
                  }
                })
            }

            EVT_TYPE_LONG_SIT -> {
              var longSitStatus = 1
              var longSitInterval = 5
              var longSitBeginTime = 510
              var longSitEndTime = 1120
              data?.let {
                longSitStatus = it["longSitStatus"] as? Int ?: longSitStatus
                longSitInterval = it["longSitInterval"] as? Int ?: longSitInterval
                longSitBeginTime = it["longSitBeginTime"] as? Int ?: longSitBeginTime
                longSitEndTime = it["longSitEndTime"] as? Int ?: longSitEndTime
              }
              var commonSetEntity = NjjLongSitEntity()
              commonSetEntity.longSitStatus = longSitStatus
              commonSetEntity.longSitInterval = longSitInterval
              commonSetEntity.longSitBeginTime = longSitBeginTime
              commonSetEntity.longSitEndTime = longSitEndTime
              NjjProtocolHelper.getInstance()
                .syncLongSitNotify(commonSetEntity, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.d("Command sent successfully=$commonSetEntity")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    //"Command send failed"
                  }
                })
            }

            EVT_TYPE_ALARM -> {
              var method = "GET"
              data?.let {
                method = it["method"] as? String ?: method
              }
              when (method) {
                "GET" -> {
                  NjjProtocolHelper.getInstance().alarmClockInfo.subscribe {
                    LogUtil.d("Get alarm data successful")
                    it?.let {
                      try {
                        val jsonResult = gson.toJson(it)
                        getAlarmClockInfoSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                      } catch (e: Exception) {
                        e.printStackTrace()
                        getAlarmClockInfoSink?.error("Serialization Error", "Failed to serialize result", e.message)
                      }
                    }
                  }
                }

                "POST" -> {
                  var index =  0
                  var mEnabled = 0
                  var mRepeat: String? = null
                  var mHour =  0
                  var mMinute =  0
                  var listRepeat :List<String> =  emptyList()
                  data?.let {
                    index = it["index"] as? Int ?: index
                    mEnabled = it["mEnabled"] as? Int ?: mEnabled
                    mRepeat = it["mRepeat"] as? String ?: mRepeat
                    mHour = it["mHour"] as? Int ?: mHour
                    mMinute = it["mMinute"] as? Int ?: mMinute
                    listRepeat = it["listRepeat"] as? List<String> ?: listRepeat
                  }

                  var bleRepeat = 0
                  // Map listRepeat items to integer flags if listRepeat is not empty
                  if (listRepeat.isNotEmpty()) {
                    for (item in listRepeat) {
                      val itemRepeat = when (item) {
                        "MONDAY" -> 0b00000010
                        "TUESDAY" -> 0b00000100
                        "WEDNESDAY" -> 0b00001000
                        "THURSDAY" -> 0b00010000
                        "FRIDAY" -> 0b00100000
                        "SATURDAY" -> 0b01000000
                        "SUNDAY" -> 0b00000001
                        "ONCE" -> 0b00000000
                        "WORKDAY" -> 0b00111110 // Monday to Friday
                        "WEEKEND" -> 0b01000001 // Saturday and Sunday
                        "EVERYDAY" -> 0b01111111 // All days
                        else -> null
                      }
                      // Add itemRepeat to bleRepeat only if it’s not null
                      itemRepeat?.let { bleRepeat = bleRepeat or it }
                    }
                  }
                  // If mRepeat is specified, override bleRepeat with the single repeat value
                  mRepeat?.let {
                    bleRepeat = when (it) {
                      "MONDAY" -> 0b00000010
                      "TUESDAY" -> 0b00000100
                      "WEDNESDAY" -> 0b00001000
                      "THURSDAY" -> 0b00010000
                      "FRIDAY" -> 0b00100000
                      "SATURDAY" -> 0b01000000
                      "SUNDAY" -> 0b00000001
                      "ONCE" -> 0b00000000
                      "WORKDAY" -> 0b00111110 // Monday to Friday
                      "WEEKEND" -> 0b01000001 // Saturday and Sunday
                      "EVERYDAY" -> 0b01111111 // All days
                      else -> bleRepeat
                    }
                  }
                  // Generate default arrays for time and cycle values
                  val timeArr = intArrayOf(6 * 3600, 95 * 360, 12 * 3600, 18 * 3600, 225 * 360, 12 * 3600, 18 * 3600, 225 * 360)
                  val cycleArr = intArrayOf(62, 65, 127, 65, 127, 127, 65, 127)
                  val infos: MutableList<NjjAlarmClockInfo> = ArrayList()
                  val count = 4

                  // Loop to add customized alarms to infos
                  for (i in 0 until count) {
                    // Calculate the alarm time in seconds from mHour and mMinute if this is the indexed alarm
                    val alarmTime = if (index == i) mHour * 3600 + mMinute * 60 else timeArr[i]

                    // Set alarm cycle to bleRepeat if the current index matches, else use cycleArr default
                    val alarmCycle = if (index == i) bleRepeat else cycleArr[i]

                    infos.add(
                      NjjAlarmClockInfo(
                        i.toString(),      // Unique alarm identifier
                        alarmTime,         // Alarm time in seconds
                        alarmCycle,        // Alarm cycle based on repeats
                        mEnabled == 1,     // Alarm state based on mEnabled (true if enabled)
                        true, // Assuming delete flag is true by default
                        0           // Set type to 0 as it's not needed
                      )
                    )
                  }

                  // Sync the alarm data using NjjProtocolHelper
                  NjjProtocolHelper.getInstance().syncAlarmClockInfo(infos, object : NjjWriteCallback {
                    override fun onWriteSuccess() {
                      LogUtil.d("Command sent successfully")
                      // "Alarm setting successful"
                    }

                    override fun onWriteFail() {
                      // "Alarm setting failed"
                    }
                  })
                }
              }
            }

            EVT_TYPE_BP_DAY -> {
              NjjProtocolHelper.getInstance().syncBloodPressure().subscribe {
                LogUtil.d("All-day blood pressure callback successful")
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncBloodPressureSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncBloodPressureSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_HR_DAY -> {
              NjjProtocolHelper.getInstance().syncHeartData().subscribe { it ->
                LogUtil.d("All-day heart rate callback successful")
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncHeartDataSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncHeartDataSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_BO_DAY -> {
              NjjProtocolHelper.getInstance().syncOxData().subscribe {
                it?.let {
                  try {
                    val jsonResult = gson.toJson(it)
                    syncOxDataSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncOxDataSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              }
            }

            EVT_TYPE_ALL_DAY_FALG -> {
              NjjProtocolHelper.getInstance().syncHeartMonitor(1, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Command sent successfully"
                }

                override fun onWriteFail() {
                  //"Command send failed"
                }
              })
            }

            EVT_TYPE_TAKE_PHOTO -> {
              var isOpen = true
              data?.let {
                isOpen = it["isOpen"] as? Boolean ?: isOpen
              }
              NjjProtocolHelper.getInstance()
                .openTakePhotoCamera(isOpen, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.d("Command sent successfully")
                    //"Command sent successfully"
                  }

                  override fun onWriteFail() {
                    //"Command send failed"
                  }
                })
            }

            // Message push
            EVT_TYPE_ALERT_MSG -> {
              var messageId = 1
              var title = "Hello"
              var value = "Test Message"
              data?.let {
                messageId = it["messageId"] as? Int ?: messageId
                title = it["title"] as? String ?: title
                value = it["value"] as? String ?: value
              }
              NjjProtocolHelper.getInstance()
                .setNotify(messageId, title, value, object : NjjWriteCallback {
                  override fun onWriteSuccess() {
                    LogUtil.d("Command sent successfully")
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
              var type = 0
              var content = "https://blog.csdn.net"
              data?.let {
                type = it["type"] as? Int ?: type
                content = it["content"] as? String ?: content
              }
              NjjProtocolHelper.getInstance().pushQRCode(type, content).subscribe {
                LogUtil.d("Push successful")
                if (it == 1) {
                  LogUtil.d("Push successful")
                  //"Push successful"
                }
              }
            }

            EVT_TYPE_RECEIPT_CODE -> {
              var type = 12
              var content = "https://blog.csdn.net"
              data?.let {
                type = it["type"] as? Int ?: type
                content = it["content"] as? String ?: content
              }
              NjjProtocolHelper.getInstance().pushPayCode(type, content).subscribe {
                LogUtil.d("Push successful")
                //"Push successful"
              }
            }

            EVT_TYPE_ANDROID_PHONE_CTRL -> {
              //hang up 0；Answer 1
              var type = 0
              data?.let {
                type = it["type"] as? Int ?: type
              }
              NjjProtocolHelper.getInstance().handUpPhone(type, object : NjjWriteCallback {
                override fun onWriteSuccess() {
                  LogUtil.d("Command sent successfully")
                  //"Command sent successfully"
                }

                override fun onWriteFail() {
                  LogUtil.d("Command send failed")
                  //"Command send failed"
                }
              })
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
                  try {
                    val njjEcgDatajsonResult = gson.toJson(njjEcgData)
                    val njjHeartDatajsonResult = gson.toJson(njjHeartData)
                    val njjStepDatajsonResult = gson.toJson(njjStepData)
                    val njjBloodPressurejsonResult = gson.toJson(njjBloodPressure)
                    val njjBloodOxyDatajsonResult = gson.toJson(njjBloodOxyData)
                    var map =   HashMap<String, Any?>()
                    map["ecgData"] = gson.fromJson(njjEcgDatajsonResult, HashMap::class.java)
                    map["heartData"] = gson.fromJson(njjHeartDatajsonResult, HashMap::class.java)
                    map["stepData"] = gson.fromJson(njjStepDatajsonResult, HashMap::class.java)
                    map["bloodPressure"] = gson.fromJson(njjBloodPressurejsonResult, HashMap::class.java)
                    map["bloodOxyData"] = gson.fromJson(njjBloodOxyDatajsonResult, HashMap::class.java)
                    syncHomeDataSink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncHomeDataSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }

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
                  LogUtil.d("type=$type  heart=$heart")
                  try {
                    var map =   HashMap<String, Any?>()
                    map["isOpen"] = false
                    map["type"] = type
                    map["time"] = time
                    map["heart"] = heart
                    syncRealTimeECGSink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncRealTimeECGSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onECGReceiveEnd(type: Int) {
                  LogUtil.d("type=$type")
                  try {
                    var map =   HashMap<String, Any?>()
                    map["isOpen"] = false
                    map["type"] = type
                    syncRealTimeECGSink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncRealTimeECGSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              })
            }

            EVT_TYPE_ECG_HR -> {
              var isOpen = true
              data?.let {
                isOpen = it["isOpen"] as? Boolean ?: isOpen
              }
              NjjProtocolHelper.getInstance().syncRealTimeECG(isOpen, object : NjjECGCallBack {
                override fun onReceivePPGData(type: Int, time: Int, heart: Int) {
                  LogUtil.d("type=$type  heart=$heart")
                  try {
                    var map =   HashMap<String, Any?>()
                    map["isOpen"] = true
                    map["type"] = type
                    map["time"] = time
                    map["heart"] = heart
                    syncRealTimeECGSink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncRealTimeECGSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onECGReceiveEnd(type: Int) {
                  LogUtil.d("type=$type")
                  try {
                    var map =   HashMap<String, Any?>()
                    map["isOpen"] = true
                    map["type"] = type
                    syncRealTimeECGSink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    syncRealTimeECGSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }
              })
            }

            EVT_TYPE_DEVICE_FUN -> {
              NjjProtocolHelper.getInstance().getDeviceFun(object : NjjDeviceFunCallback {
                override fun onDeviceFunSuccess(bleDeviceFun: BleDeviceFun) {
                  Log.e("fan", "fan$bleDeviceFun")
                  try {
                    val jsonResult = gson.toJson(bleDeviceFun)
                    getDeviceFunSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceFunSink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onDeviceFunFail() {
                }
              })
            }

            EVT_TYPE_BAND_CONFIG1 -> {
              NjjProtocolHelper.getInstance().getDeviceConfig1(object : NjjConfig1CallBack {
                var map =   HashMap<String, Any?>()
                override fun onWriteSuccess() {
                  Log.e("onWriteSuccess", "getDeviceConfig1")
                }

                override fun onLongSitEntity(njjLongSitEntity: NjjLongSitEntity) {
                  LogUtil.d(njjLongSitEntity.toString())
                  try {
                    val jsonResult = gson.toJson(njjLongSitEntity)
                    map["njjLongSitEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
                    getDeviceConfig1Sink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceConfig1Sink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onNjjDrinkWaterEntity(njjDrinkWaterEntity: NjjDrinkWaterEntity) {
                  LogUtil.d(njjDrinkWaterEntity.toString())
                  try{
                    val jsonResult = gson.toJson(njjDrinkWaterEntity)
                    map["njjDrinkWaterEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
                    getDeviceConfig1Sink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceConfig1Sink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onNjjWashHandEntity(njjWashHandEntity: NjjWashHandEntity) {
                  LogUtil.d(njjWashHandEntity.toString())
                  try{
                    val jsonResult = gson.toJson(njjWashHandEntity)
                    map["njjWashHandEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
                    getDeviceConfig1Sink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceConfig1Sink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onNjjWristScreenEntity(njjWristScreenEntity: NjjWristScreenEntity) {
                  LogUtil.d(njjWristScreenEntity.toString())
                  try{
                    val jsonResult = gson.toJson(njjWristScreenEntity)
                    map["njjWristScreenEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
                    getDeviceConfig1Sink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceConfig1Sink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onNjjDisturbEntity(njjDisturbEntity: NjjDisturbEntity) {
                  LogUtil.d(njjDisturbEntity.toString())
                  try{
                    val jsonResult = gson.toJson(njjDisturbEntity)
                    map["njjDisturbEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
                    getDeviceConfig1Sink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceConfig1Sink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onNjjMedicineEntity(njjMedicineEntity: NjjMedicineEntity) {
                  LogUtil.d(njjMedicineEntity.toString())
                  try{
                    val jsonResult = gson.toJson(njjMedicineEntity)
                    map["njjMedicineEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
                    getDeviceConfig1Sink?.success(map)
                  } catch (e: Exception) {
                    e.printStackTrace()
                    getDeviceConfig1Sink?.error("Serialization Error", "Failed to serialize result", e.message)
                  }
                }

                override fun onFail() {
                  Log.e("onFail", "getDeviceConfig1")
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
    NjjBleManger.getInstance().init(mActivity.application)
    ApplicationProxy.getInstance().setApplication(mActivity.application)
    CallBackManager.getInstance().registerConnectStatuesCallBack("", object : ConnectStatuesCallBack.ICallBack {
      override fun onConnected(mac: String?) {
        LogUtil.d("Connection successful")
        try{
          if (mac != null) {
            var map =   HashMap<String, Any?>()
            map["status"] = "onConnected"
            map["mac"] = mac
            registerConnectStatuesCallBackSink?.success(map)
          }
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onConnecting(mac: String?) {
        LogUtil.d("Connecting")
        try{
          if (mac != null) {
            var map =   HashMap<String, Any?>()
            map["status"] = "onConnecting"
            map["mac"] = mac
            registerConnectStatuesCallBackSink?.success(map)
          }
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onDisConnected(mac: String?) {
        LogUtil.d("Disconnect")
        try{
          if (mac != null) {
            var map =   HashMap<String, Any?>()
            map["status"] = "onDisConnected"
            map["mac"] = mac
            registerConnectStatuesCallBackSink?.success(map)
          }
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onConnectFail(mac: String?) {
        LogUtil.d("Connection failed")
        try{
          var map =   HashMap<String, Any?>()
          map["status"] = "onConnectFail"
          map["mac"] = mac
          registerConnectStatuesCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onDiscoveredServices(code: Int,mac: String?) {
        LogUtil.d("onDiscoveredServices")
        try{
          var map =   HashMap<String, Any?>()
          map["status"] = "onDiscoveredServices"
          map["code"] = code
          map["code_name"] = Code.toString(code)
          registerConnectStatuesCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

    })
    CallBackManager.getInstance().registerSomatosensoryGameCallback(object :SomatosensoryGameCallback.ICallBack{
      override fun onReceiveData(somatosensoryGame: SomatosensoryGame?) {
        LogUtil.d(somatosensoryGame.toString())
        try{
          val jsonResult = gson.toJson(somatosensoryGame)
          registerSomatosensoryGameCallbackSink?.success(gson.fromJson(jsonResult, HashMap::class.java))
        } catch (e: Exception) {
          e.printStackTrace()
          registerSomatosensoryGameCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onReceiveStatus(status: Int) {
        LogUtil.d("onReceiveStatus: $status")
      }

    })

    NjjProtocolHelper.getInstance().registerSingleHeartOxBloodCallback(
      object : NjjNotifyCallback {

        override fun onBloodPressureData(systolicPressure: Int, diastolicPressure: Int) {
          LogUtil.d("Single Blood Pressure: $systolicPressure/$diastolicPressure")
          try {
            var mapDetail =  HashMap<String, Any?>()
            mapDetail["systolicPressure"] = systolicPressure
            mapDetail["diastolicPressure"] = diastolicPressure
            var map =  HashMap<String, Any?>()
            map["onBloodPressureData"] = mapDetail
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
        }

        override fun onHeartRateData(rate: Int) {
          LogUtil.d("Single Heart Rate: $rate")
          try {
            var map =  HashMap<String, Any?>()
            map["onHeartRateData"] = rate
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
        }

        override fun onOxyData(rate: Int) {
          LogUtil.d("Single Blood Oxygen: $rate")
          try {
            var map =  HashMap<String, Any?>()
            map["onOxyData"] = rate
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
        }

        override fun takePhone(value: Int) {
          LogUtil.d("Take Photo: $value")
          try {
            var map =  HashMap<String, Any?>()
            map["takePhone"] = value
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
        }

        override fun onStepData(njjStepData: NjjStepData?) {
          LogUtil.d("Calories: ${njjStepData?.calData} Steps: ${njjStepData?.stepNum} Distance: ${njjStepData?.distance}")
          try {
            val jsonResult = gson.toJson(njjStepData)
            var map =  HashMap<String, Any?>()
            map["onStepData"] = gson.fromJson(jsonResult, HashMap::class.java)
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
        }

        override fun findPhone(value: Int) {
          LogUtil.d("Find Phone: $value")
          try {
            var map =  HashMap<String, Any?>()
            map["findPhone"] = value
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
        }

        override fun endCallPhone(value: Int) {
          try {
            var map = HashMap<String, Any?>()
            map["endCallPhone"] = value
            registerSingleHeartOxBloodCallbackSink?.success(map)
          } catch (e: Exception) {
            e.printStackTrace()
            registerSingleHeartOxBloodCallbackSink?.error("Serialization Error", "Failed to serialize result", e.message)
          }
          if (value == 0) {
            LogUtil.d("End Call")
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
