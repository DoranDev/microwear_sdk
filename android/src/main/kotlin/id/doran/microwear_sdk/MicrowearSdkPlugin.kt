package id.doran.microwear_sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.njj.njjsdk.callback.CallBackManager
import com.njj.njjsdk.callback.ConnectStatuesCallBack
import com.njj.njjsdk.callback.GPSCallback
import com.njj.njjsdk.callback.Mac3CallBack
import com.njj.njjsdk.callback.NjjBatteryCallBack
import com.njj.njjsdk.callback.NjjConfig1CallBack
import com.njj.njjsdk.callback.NjjDeviceFunCallback
import com.njj.njjsdk.callback.NjjECGCallBack
import com.njj.njjsdk.callback.NjjFirmwareCallback
import com.njj.njjsdk.callback.NjjHomeDataCallBack
import com.njj.njjsdk.callback.NjjNotifyCallback
import com.njj.njjsdk.callback.NjjStockCallback
import com.njj.njjsdk.callback.NjjWriteCallback
import com.njj.njjsdk.callback.SomatosensoryGameCallback
import com.njj.njjsdk.library.Code
import com.njj.njjsdk.manger.NJJOtaManage
import com.njj.njjsdk.manger.NjjBleManger
import com.njj.njjsdk.manger.NjjProtocolHelper
import com.njj.njjsdk.manger.NjjPushDataHelper
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ADD_FRIEND
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ALARM
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ALERT_FIND_WATCH
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ALERT_MSG
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ALL_DAY_FALG
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ANDROID_PHONE_CTRL
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_APP_REQUEST_SYNC
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_BAND_CONFIG
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_BAND_CONFIG1
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_BAT
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_BO_DAY
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_BP_DAY
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_DATE_TIME
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_DEVICE_FUN
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_DISPLAY_TIME
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_DISTURB
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_DRINK_WATER
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_ECG_HR
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_FIRMWARE_VER
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_GPS_SPORT
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_HISTORY_SPORT_DATA
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_HOUR_STEP
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_HR_DAY
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_LONG_SIT
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_OTA_START
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_RAISE_WRIST
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_REAL_TIME_WEATHER
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_RECEIPT_CODE
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_SLEEP_DATA
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_SPORT_RECORD
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_STOCK
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_TAKE_PHOTO
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_TARGET_STEP
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_TEMP_UNIT
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_TIME_MODE
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_TP_VER
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_UI_VER
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_UNIT_SYSTEM
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_WASH_HAND
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_WEATHER_FORECAST
import com.njj.njjsdk.protocol.cmd.EVT_TYPE_WOMEN_HEALTH
import com.njj.njjsdk.protocol.entity.BLEDevice
import com.njj.njjsdk.protocol.entity.BleDeviceFun
import com.njj.njjsdk.protocol.entity.EmergencyContact
import com.njj.njjsdk.protocol.entity.NJJGPSSportEntity
import com.njj.njjsdk.protocol.entity.NJJWeatherData
import com.njj.njjsdk.protocol.entity.NjjAlarmClockInfo
import com.njj.njjsdk.protocol.entity.NjjBloodOxyData
import com.njj.njjsdk.protocol.entity.NjjBloodPressure
import com.njj.njjsdk.protocol.entity.NjjDisturbEntity
import com.njj.njjsdk.protocol.entity.NjjDrinkWaterEntity
import com.njj.njjsdk.protocol.entity.NjjEcgData
import com.njj.njjsdk.protocol.entity.NjjHeartData
import com.njj.njjsdk.protocol.entity.NjjLongSitEntity
import com.njj.njjsdk.protocol.entity.NjjMedicineEntity
import com.njj.njjsdk.protocol.entity.NjjStepData
import com.njj.njjsdk.protocol.entity.NjjSyncWeatherData
import com.njj.njjsdk.protocol.entity.NjjWashHandEntity
import com.njj.njjsdk.protocol.entity.NjjWristScreenEntity
import com.njj.njjsdk.protocol.entity.SomatosensoryGame
import com.njj.njjsdk.utils.ApplicationProxy
import com.njj.njjsdk.utils.BleBeaconUtil
import com.njj.njjsdk.utils.LogUtil
import id.doran.microwear_sdk.service.MicrowearService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
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


  private var registerMac3CallBackChannel: EventChannel? = null
  private var registerMac3CallBackSink : EventChannel.EventSink? = null
  private val registerMac3CallBackHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      registerMac3CallBackSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var registerGPSCallBackChannel: EventChannel? = null
  private var registerGPSCallBackSink : EventChannel.EventSink? = null
  private val registerGPSCallBackHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      registerGPSCallBackSink = eventSink
    }
    override fun onCancel(o: Any?) {}
  }

  private var onLoadingChannel: EventChannel? = null
  private var onLoadingSink : EventChannel.EventSink? = null
  private val onLoadingHandler = object : EventChannel.StreamHandler {
    override fun onListen(arg: Any?, eventSink: EventChannel.EventSink?) {
      onLoadingSink = eventSink
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

    registerMac3CallBackChannel = EventChannel(flutterPluginBinding.binaryMessenger, "registerMac3CallBack")
    registerMac3CallBackChannel!!.setStreamHandler(registerMac3CallBackHandler)

    registerGPSCallBackChannel = EventChannel(flutterPluginBinding.binaryMessenger, "registerGPSCallBack")
    registerGPSCallBackChannel!!.setStreamHandler(registerGPSCallBackHandler)

    onLoadingChannel = EventChannel(flutterPluginBinding.binaryMessenger, "onLoading")
    onLoadingChannel!!.setStreamHandler(onLoadingHandler)

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
        val bondState = NjjBleManger.getInstance().bluetoothClient.getBondState(macAddress)
        result.success(bondState)
      }
      "getConnectStatus" -> {
        val macAddress = call.argument<String>("macAddress")
        val connectStatus = NjjBleManger.getInstance().bluetoothClient.getConnectStatus(macAddress)
        result.success(connectStatus)
      }
      "registerConnectStatue" -> {
        val macAddress = call.argument<String>("macAddress")
        NjjBleManger.getInstance().registerConnectStatue(macAddress)
      }
      "unregisterConnectStatue" -> {
        NjjBleManger.getInstance().unregisterConnectStatue()
      }

      "startService" -> {
        val intent = Intent(mContext, MicrowearService::class.java)
        mContext.startService(intent)
        result.success("Service started")
      }
      "stopService" -> {
        val intent = Intent(mContext, MicrowearService::class.java)
        mContext.stopService(intent)
        result.success("Service stopped")
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

            EVT_TYPE_WEATHER_FORECAST -> {
              var listRaw :List<Map<String, Any>> =  emptyList()
              data?.let {
                listRaw = it["weathers"] as List<Map<String, Any>>
              }

              var listWeather : MutableList<NJJWeatherData> = mutableListOf()

              listRaw.forEach { item ->
                val week = item["week"] as Int
                val weatherType = item["weatherType"] as Int
                val tempData = item["tempData"] as Int
                val highestTemp = item["highestTemp"] as Int
                val minimumTemp = item["minimumTemp"] as Int
                val tempLimit = item["tempLimit"] as Int
                val pressure = item["pressure"] as String
                val ultLevel = item["ultLevel"] as String
                val humidity = item["humidity"] as Int
                val windDirDay = item["windDirDay"] as Int
                val windScaleDay = item["windScaleDay"] as String
                val vis = item["vis"] as Int
                val precip = item["precip"] as Int

                val weatherData = NJJWeatherData()
                weatherData.week = week
                weatherData.tempData = tempData
                weatherData.highestTemp = highestTemp
                weatherData.minimumTemp = minimumTemp
                weatherData.tempLimit = tempLimit
                weatherData.weatherType = weatherType
                weatherData.pressure = pressure
                weatherData.ultLevel = ultLevel
                weatherData.humidity = humidity
                weatherData.windDirDay = windDirDay
                weatherData.windScaleDay = windScaleDay
                weatherData.vis = vis
                weatherData.precip = precip

                listWeather.add(weatherData)
              }

              NjjProtocolHelper.getInstance()
                .syncWeekWeatherTypeData(listWeather)
            }

            EVT_TYPE_STOCK -> {
              var count = 0
              var id = 0
              var code = ""
              var companyName = ""
              var currentPrice = ""
              var changePercent = ""

              data?.let {
                count = it["count"] as Int
                id = it["id"] as Int
                code = it["code"] as String
                companyName = it["companyName"] as String
                currentPrice = it["currentPrice"] as String
                changePercent = it["changePercent"] as String
              }

              NjjProtocolHelper.getInstance()
                .sendStock(count, id, code, companyName, currentPrice, changePercent)
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
                    it?.let {
                      try {
                        // Serialize the list to JSON
                        val jsonResult = gson.toJson(it)

                        LogUtil.d("Serialized JSON result: $jsonResult")

                        // Deserialize back to a List of Maps (or other suitable format)
                        val deserializedData = gson.fromJson(jsonResult, List::class.java)
                        getAlarmClockInfoSink?.success(deserializedData)
                      } catch (e: JsonSyntaxException) {
                        LogUtil.e("Deserialization failed: ${e.message}")
                        getAlarmClockInfoSink?.error(
                          "Serialization Error",
                          "Failed to serialize result due to JSON structure mismatch",
                          e.message
                        )
                      } catch (e: Exception) {
                        e.printStackTrace()
                        getAlarmClockInfoSink?.error(
                          "Serialization Error",
                          "An unexpected error occurred",
                          e.message
                        )
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


            EVT_TYPE_OTA_START -> {
              var njjPushDataHelper = NjjPushDataHelper()
              var otaType = ""
              data?.let {
                otaType = it["otaType"] as? String ?: otaType
              }
              when(otaType) {
                "startPushContactDial" -> {
                  var contactList: MutableList<EmergencyContact> = mutableListOf()
                  var listContact: List<Map<String, String>> = emptyList()
                  data?.let {
                    val tempList = it["listContact"]
                    if (tempList is List<*>) {  // Check if it is a List
                      listContact = tempList.filterIsInstance<Map<String, String>>() // Safe cast
                    }
                  }
                  for (cont in listContact) {
                    var emergencyContact = EmergencyContact()
                    emergencyContact.contactName = cont["displayName"]
                    emergencyContact.phoneNumber = cont["phone"]
                    contactList.add(emergencyContact)
                  }
                  njjPushDataHelper.startPushContactDial(contactList, object :
                    NjjPushDataHelper.NJjPushListener {
                    override fun onPushSuccess(){
                      LogUtil.d("onPushSuccess")
                    }

                    override fun onPushError(code: Int){
                      LogUtil.d("onPushError")
                    }

                    override fun onPushStart(){
                      LogUtil.d("onPushStart")
                    }

                    override fun onPushProgress(progress: Int){
                      LogUtil.d("onPushProgress $progress")
                    }
                  })
                }
                "startPushDial" -> {
                  var path =""
                  data?.let {
                    path = it["path"] as? String ?: path
                  }

                  // Create a File object from the provided path
                  val file = File(path)

                  // Check if the file exists
                  if (file.exists()) {
                    try {
                      njjPushDataHelper.startPushDial(file.absolutePath.toString(), object :
                        NjjPushDataHelper.NJjPushListener {
                        override fun onPushSuccess(){
                          LogUtil.d("onPushSuccess")
                          var map =   HashMap<String, Any?>()
                          map["status"] = "onPushSuccess"
                          onLoadingSink?.success(map)
                        }

                        override fun onPushError(code: Int){
                          LogUtil.d("onPushError")
                          var map =   HashMap<String, Any?>()
                          map["status"] = "onPushError"
                          onLoadingSink?.success(map)
                        }

                        override fun onPushStart(){
                          LogUtil.d("onPushStart")
                          var map =   HashMap<String, Any?>()
                          map["status"] = "onPushStart"
                          onLoadingSink?.success(map)
                        }

                        override fun onPushProgress(progress: Int){
                          LogUtil.d("onPushProgress")
                          var map =   HashMap<String, Any?>()
                          map["status"] = "onPushProgress"
                          map["progress"] = progress
                          onLoadingSink?.success(map)
                        }
                      })
                    } catch (e: Exception) {
                      // Handle any exceptions during the BLE transmission
                      Log.e("BLE Error", "Error sending file via BLE: ${e.message}")
                    }
                  } else {
                    // Handle the error: file does not exist
                    Log.e("File Error", "File not found at path: $path")
                  }
//                                val inputStream: InputStream = mContext!!.assets.open(path)
//                                BleConnector.sendStream(bleKey, inputStream, 0)


                }
                "startPushCustomDial" -> {
                  var path =""
                  var bigWidth = 320
                  var bigHeight = 380
                  var smallNeedWidth = 240
                  var smallNeedHeight = 283
                  var timePosition =0
                  var colors = "#FF0000"
                  data?.let {
                    path = it["path"] as? String ?: path
                    bigWidth = it["bigWidth"] as? Int ?: bigWidth
                    bigHeight = it["bigHeight"] as? Int ?: bigHeight
                    smallNeedWidth = it["smallNeedWidth"] as? Int ?: smallNeedWidth
                    smallNeedHeight = it["smallNeedHeight"] as? Int ?: smallNeedHeight
                    timePosition = it["timePosition"] as? Int ?: timePosition
                    colors = it["colors"] as? String ?: colors
                  }
                  njjPushDataHelper.starPushCustomDial(path,bigWidth,bigHeight,smallNeedWidth,smallNeedHeight,timePosition,colors, object :
                    NjjPushDataHelper.NJjPushListener {
                    override fun onPushSuccess(){
                      LogUtil.d("onPushSuccess")
                      var map =   HashMap<String, Any?>()
                      map["status"] = "onPushSuccess"
                      onLoadingSink?.success(map)
                    }

                    override fun onPushError(code: Int){
                      LogUtil.d("onPushError")
                      var map =   HashMap<String, Any?>()
                      map["status"] = "onPushError"
                      onLoadingSink?.success(map)
                    }

                    override fun onPushStart(){
                      LogUtil.d("onPushStart")
                      var map =   HashMap<String, Any?>()
                      map["status"] = "onPushStart"
                      onLoadingSink?.success(map)
                    }

                    override fun onPushProgress(progress: Int){
                      LogUtil.d("onPushProgress")
                      var map =   HashMap<String, Any?>()
                      map["status"] = "onPushProgress"
                      map["progress"] = progress
                      onLoadingSink?.success(map)
                    }
                  })
                }
              }
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

            EVT_TYPE_GPS_SPORT -> {
              var func = "startGPSData"
              data?.let {
                func = it["func"] as? String ?: func
              }

              when (func) {
                "startGPSStatus" -> {
                  var tipe = 0
                  var status = 0
                  data?.let {
                    tipe = it["tipe"] as? Int ?: tipe
                    status = it["status"] as? Int ?: status
                  }
                  /**
                   * Reply to the status of the APP when the watch starts.
                   *
                   * @param type
                   * GPS_CMD_GPS = 0 //Request GPS positioning rights:
                   * GPS_CMD_APP_BUSY = 7 //APP is busy and cannot respond at this time
                   *
                   * @param status
                   * - 1: GPS open reply
                   * - 0: GPS closed reply
                   */
                  NjjProtocolHelper.getInstance().startGPSStatus(tipe, status)
                }
                "sendGPSStatus" -> {
                  var tipe = 0
                  var sportId = 0
                  data?.let {
                    tipe = it["tipe"] as? Int ?: tipe
                    sportId = it["sportId"] as? Int ?: sportId
                  }
                  /**
                   * Send the status of the current movement.
                   *
                   * @param type
                   * GPS_CMD_PAUSE= 4, //Pause
                   * GPS_CMD_CONTINUE= 5, //Continue
                   * GPS_CMD_END= 6, //End
                   *
                   * @param sportId The ID of the current sport activity.
                   */
                  NjjProtocolHelper.getInstance().sendGPSStatus(tipe, sportId)
                }
                "syncGPSData" -> {
                  var gpsSportEntity = NJJGPSSportEntity()
                  data?.let {
                    gpsSportEntity.sportHr = it["sportHr"] as Int
                    gpsSportEntity.sportValid = it["sportValid"] as Int
                    gpsSportEntity.sportType = it["sportType"] as Int
                    gpsSportEntity.sportTime = it["sportTime"] as Int
                    gpsSportEntity.sportSteps = it["sportSteps"] as Int
                    gpsSportEntity.sportKcal = it["sportKcal"] as Int
                    gpsSportEntity.sportDistance = it["sportDistance"] as Int
                    gpsSportEntity.sportSpeed = it["sportSpeed"] as Int
                    gpsSportEntity.sportCadence = it["sportCadence"] as Int
                    gpsSportEntity.sportStride = it["sportStride"] as Int
                  }
                  /**
                   * Synchronize the sports data of the mobile phone.
                   *
                   * @param gpsSportEntity The data entity containing GPS sport information.
                   */
                  NjjProtocolHelper.getInstance().syncGPSData(gpsSportEntity)
                }
              }
            }
          }
        }
      }
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
//    channel.setMethodCallHandler(null)
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
            var map =   HashMap<String, Any?>()
            map["status"] = "onConnected"
            map["mac"] = mac
            registerConnectStatuesCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onConnecting(mac: String?) {
        LogUtil.d("Connecting")
        try{
            var map =   HashMap<String, Any?>()
            map["status"] = "onConnecting"
            map["mac"] = mac
            registerConnectStatuesCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerConnectStatuesCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onDisConnected(mac: String?) {
        LogUtil.d("Disconnect")
        try{
            var map =   HashMap<String, Any?>()
            map["status"] = "onDisConnected"
            map["mac"] = mac
            registerConnectStatuesCallBackSink?.success(map)
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

    CallBackManager.getInstance().registerStockCallback(object :NjjStockCallback.ICallBack{
      override fun onReceiveData() {
        TODO("Not yet implemented")
      }
    })

    CallBackManager.getInstance().registerMac3CallBack(object :Mac3CallBack.ICallBack{
      override fun onSuccess(mac: String?) {
        LogUtil.d("onSuccess: $mac")
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onSuccess"
          map["mac"] = mac
          registerMac3CallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerMac3CallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onFail() {
        LogUtil.d("onFail")
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onFail"
          registerMac3CallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerMac3CallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }
    })

    CallBackManager.getInstance().registerGPSCallbackCallBack(object : GPSCallback.ICallBack {
      override fun onGPSPermission() {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSPermission"
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onGPSCountdown(sportId: Int) {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSCountdown"
          map["sportId"] = sportId
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onGPSStart(sportId: Int) {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSStart"
          map["sportId"] = sportId
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }

      }

      override fun onGPSSync(gpsSportEntity: NJJGPSSportEntity?) {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSSync"
          val jsonResult = gson.toJson(gpsSportEntity)
          map["gpsSportEntity"] = gson.fromJson(jsonResult, HashMap::class.java)
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onGPSPause(sportId: Int) {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSPause"
          map["sportId"] = sportId
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onGPSContinue(sportId: Int) {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSContinue"
          map["sportId"] = sportId
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
        }
      }

      override fun onGPSEnd(sportId: Int) {
        try {
          var map =  HashMap<String, Any?>()
          map["status"] = "onGPSEnd"
          map["sportId"] = sportId
          registerGPSCallBackSink?.success(map)
        } catch (e: Exception) {
          e.printStackTrace()
          registerGPSCallBackSink?.error("Serialization Error", "Failed to serialize result", e.message)
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
