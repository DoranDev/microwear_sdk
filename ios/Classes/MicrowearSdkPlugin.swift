import Flutter
import UIKit

public class MicrowearSdkPlugin: NSObject, FlutterPlugin, FlutterStreamHandler {
    
    // Event sinks for various event channels
     var deviceDataReceivedSink: FlutterEventSink?
     var batteryLevelSink: FlutterEventSink?
     var syncHourStepSink: FlutterEventSink?
     var syncWeekDaySportsSink: FlutterEventSink?
     var deviceConfigSink: FlutterEventSink?
     var syncSleepDataSink: FlutterEventSink?
     var syncSportRecordSink: FlutterEventSink?
     var getAlarmClockInfoSink: FlutterEventSink?
     var syncBloodPressureSink: FlutterEventSink?
     var syncHeartDataSink: FlutterEventSink?
     var syncOxDataSink: FlutterEventSink?
     var syncHomeDataSink: FlutterEventSink?
     var syncRealTimeECGSink: FlutterEventSink?
     var getDeviceFunSink: FlutterEventSink?
     var getDeviceConfig1Sink: FlutterEventSink?
     var registerConnectStatuesCallBackSink: FlutterEventSink?
     var registerSomatosensoryGameCallbackSink: FlutterEventSink?
     var registerSingleHeartOxBloodCallbackSink: FlutterEventSink?
     var registerMac3CallBackSink: FlutterEventSink?
     var registerGPSCallBackSink: FlutterEventSink?
     var onLoadingSink: FlutterEventSink?

    // Event channel names
    static let deviceDataReceivedChannelName = "deviceDataReceived"
    static let batteryLevelChannelName = "getBatteryLevel"
    static let syncHourStepChannelName = "syncHourStep"
    static let syncWeekDaySportsChannelName = "syncWeekDaySports"
    static let deviceConfigChannelName = "deviceConfig"
    static let syncSleepDataChannelName = "syncSleepData"
    static let syncSportRecordChannelName = "syncSportRecord"
    static let getAlarmClockInfoChannelName = "getAlarmClockInfo"
    static let syncBloodPressureChannelName = "syncBloodPressure"
    static let syncHeartDataChannelName = "syncHeartData"
    static let syncOxDataChannelName = "syncOxData"
    static let syncHomeDataChannelName = "syncHomeData"
    static let syncRealTimeECGChannelName = "syncRealTimeECG"
    static let getDeviceFunChannelName = "getDeviceFun"
    static let getDeviceConfig1ChannelName = "getDeviceConfig1"
    static let registerConnectStatuesCallBackChannelName = "registerConnectStatuesCallBack"
    static let registerSomatosensoryGameCallbackChannelName = "registerSomatosensoryGameCallback"
    static let registerSingleHeartOxBloodCallbackChannelName = "registerSingleHeartOxBloodCallback"
    static let registerMac3CallBackChannelName = "registerMac3CallBack"
    static let registerGPSCallBackChannelName = "registerGPSCallBack"
    static let onLoadingChannelName = "onLoading"
    
    let bleService = NJYBleService.sharedInstance()
    var macAddressCache = ""

    init(_ channel: FlutterMethodChannel) {
        super.init()
    }

    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        let channelName = arguments as? String

        switch channelName {
        case MicrowearSdkPlugin.deviceDataReceivedChannelName:
            deviceDataReceivedSink = events
        case MicrowearSdkPlugin.batteryLevelChannelName:
            batteryLevelSink = events
        case MicrowearSdkPlugin.syncHourStepChannelName:
            syncHourStepSink = events
        case MicrowearSdkPlugin.syncWeekDaySportsChannelName:
            syncWeekDaySportsSink = events
        case MicrowearSdkPlugin.deviceConfigChannelName:
            deviceConfigSink = events
        case MicrowearSdkPlugin.syncSleepDataChannelName:
            syncSleepDataSink = events
        case MicrowearSdkPlugin.syncSportRecordChannelName:
            syncSportRecordSink = events
        case MicrowearSdkPlugin.getAlarmClockInfoChannelName:
            getAlarmClockInfoSink = events
        case MicrowearSdkPlugin.syncBloodPressureChannelName:
            syncBloodPressureSink = events
        case MicrowearSdkPlugin.syncHeartDataChannelName:
            syncHeartDataSink = events
        case MicrowearSdkPlugin.syncOxDataChannelName:
            syncOxDataSink = events
        case MicrowearSdkPlugin.syncHomeDataChannelName:
            syncHomeDataSink = events
        case MicrowearSdkPlugin.syncRealTimeECGChannelName:
            syncRealTimeECGSink = events
        case MicrowearSdkPlugin.getDeviceFunChannelName:
            getDeviceFunSink = events
        case MicrowearSdkPlugin.getDeviceConfig1ChannelName:
            getDeviceConfig1Sink = events
        case MicrowearSdkPlugin.registerConnectStatuesCallBackChannelName:
            registerConnectStatuesCallBackSink = events
        case MicrowearSdkPlugin.registerSomatosensoryGameCallbackChannelName:
            registerSomatosensoryGameCallbackSink = events
        case MicrowearSdkPlugin.registerSingleHeartOxBloodCallbackChannelName:
            registerSingleHeartOxBloodCallbackSink = events
        case MicrowearSdkPlugin.registerMac3CallBackChannelName:
            registerMac3CallBackSink = events
        case MicrowearSdkPlugin.registerGPSCallBackChannelName:
            registerGPSCallBackSink = events
        case MicrowearSdkPlugin.onLoadingChannelName:
            onLoadingSink = events
        default:
            break
        }

        return nil
    }

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        return nil
    }

    private static func setupEventChannel(name: String, registrar: FlutterPluginRegistrar, handler: MicrowearSdkPlugin) {
        let channel = FlutterEventChannel(name: name, binaryMessenger: registrar.messenger())
        channel.setStreamHandler(handler)
    }

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "microwear_sdk", binaryMessenger: registrar.messenger())
        let instance = MicrowearSdkPlugin(channel)
        registrar.addMethodCallDelegate(instance, channel: channel)
        let deviceDataReceivedChannel = FlutterEventChannel(name: deviceDataReceivedChannelName, binaryMessenger: registrar.messenger())
        deviceDataReceivedChannel.setStreamHandler(instance)

        let batteryLevelChannel = FlutterEventChannel(name: batteryLevelChannelName, binaryMessenger: registrar.messenger())
        batteryLevelChannel.setStreamHandler(instance)

        let syncHourStepChannel = FlutterEventChannel(name: syncHourStepChannelName, binaryMessenger: registrar.messenger())
        syncHourStepChannel.setStreamHandler(instance)

        let syncWeekDaySportsChannel = FlutterEventChannel(name: syncWeekDaySportsChannelName, binaryMessenger: registrar.messenger())
        syncWeekDaySportsChannel.setStreamHandler(instance)

        let deviceConfigChannel = FlutterEventChannel(name: deviceConfigChannelName, binaryMessenger: registrar.messenger())
        deviceConfigChannel.setStreamHandler(instance)

        let syncSleepDataChannel = FlutterEventChannel(name: syncSleepDataChannelName, binaryMessenger: registrar.messenger())
        syncSleepDataChannel.setStreamHandler(instance)

        let syncSportRecordChannel = FlutterEventChannel(name: syncSportRecordChannelName, binaryMessenger: registrar.messenger())
        syncSportRecordChannel.setStreamHandler(instance)

        let getAlarmClockInfoChannel = FlutterEventChannel(name: getAlarmClockInfoChannelName, binaryMessenger: registrar.messenger())
        getAlarmClockInfoChannel.setStreamHandler(instance)

        let syncBloodPressureChannel = FlutterEventChannel(name: syncBloodPressureChannelName, binaryMessenger: registrar.messenger())
        syncBloodPressureChannel.setStreamHandler(instance)

        let syncHeartDataChannel = FlutterEventChannel(name: syncHeartDataChannelName, binaryMessenger: registrar.messenger())
        syncHeartDataChannel.setStreamHandler(instance)

        let syncOxDataChannel = FlutterEventChannel(name: syncOxDataChannelName, binaryMessenger: registrar.messenger())
        syncOxDataChannel.setStreamHandler(instance)

        let syncHomeDataChannel = FlutterEventChannel(name: syncHomeDataChannelName, binaryMessenger: registrar.messenger())
        syncHomeDataChannel.setStreamHandler(instance)

        let syncRealTimeECGChannel = FlutterEventChannel(name: syncRealTimeECGChannelName, binaryMessenger: registrar.messenger())
        syncRealTimeECGChannel.setStreamHandler(instance)

        let getDeviceFunChannel = FlutterEventChannel(name: getDeviceFunChannelName, binaryMessenger: registrar.messenger())
        getDeviceFunChannel.setStreamHandler(instance)

        let getDeviceConfig1Channel = FlutterEventChannel(name: getDeviceConfig1ChannelName, binaryMessenger: registrar.messenger())
        getDeviceConfig1Channel.setStreamHandler(instance)

        let registerConnectStatuesCallBackChannel = FlutterEventChannel(name: registerConnectStatuesCallBackChannelName, binaryMessenger: registrar.messenger())
        registerConnectStatuesCallBackChannel.setStreamHandler(instance)

        let registerSomatosensoryGameCallbackChannel = FlutterEventChannel(name: registerSomatosensoryGameCallbackChannelName, binaryMessenger: registrar.messenger())
        registerSomatosensoryGameCallbackChannel.setStreamHandler(instance)

        let registerSingleHeartOxBloodCallbackChannel = FlutterEventChannel(name: registerSingleHeartOxBloodCallbackChannelName, binaryMessenger: registrar.messenger())
        registerSingleHeartOxBloodCallbackChannel.setStreamHandler(instance)

        let registerMac3CallBackChannel = FlutterEventChannel(name: registerMac3CallBackChannelName, binaryMessenger: registrar.messenger())
        registerMac3CallBackChannel.setStreamHandler(instance)

        let registerGPSCallBackChannel = FlutterEventChannel(name: registerGPSCallBackChannelName, binaryMessenger: registrar.messenger())
        registerGPSCallBackChannel.setStreamHandler(instance)

        let onLoadingChannel = FlutterEventChannel(name: onLoadingChannelName, binaryMessenger: registrar.messenger())
        onLoadingChannel.setStreamHandler(instance)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let args = call.arguments as? [String: Any]

        switch call.method {
        case "connect":
            let macAddress = args?["macAddress"] as? String ?? ""
            connectToDevice(with: macAddress, result: result)

        case "creteBond":
            let macAddress = args?["macAddress"] as? String ?? ""
            // TODO: Implement createBond functionality
            result(nil)

        case "startService":
            // TODO: Implement startService functionality
            result("service_started")

        case "stopService":
            // TODO: Implement stopService functionality
            result("service_stopped")

        case "getBondState":
            let macAddress = args?["macAddress"] as? String ?? ""
            // TODO: Implement getBondState functionality
            result(nil)

        case "getConnectStatus":
            let macAddress = args?["macAddress"] as? String ?? ""
            // TODO: Implement getConnectStatus functionality
            result(nil)

        case "registerConnectStatue":
            let macAddress = args?["macAddress"] as? String ?? ""
            // TODO: Implement registerConnectStatus functionality
            result(nil)

        case "unregisterConnectStatue":
            // TODO: Implement unregisterConnectStatus functionality
            result(nil)

        case "disconnect":
            result(nil)

        case "sendRequest":
            guard let microwearDeviceControlValue = args?["microwearDeviceControl"] as? Int,
                  let data = args?["data"] as? [String: Any]
            else {
                result(FlutterError(code: "INVALID_ARGUMENTS",
                                    message: "Invalid arguments for sendRequest",
                                    details: nil))
                return
            }

      

            switch microwearDeviceControlValue {
            case 0: // MicrowearDeviceControl.alertFindWatch.value
                print("Find watch command sent")
                bleService.sendAlertFindWatch()
            case 11: // MicrowearDeviceControl.alertMsg.value
                print("Message push notification command sent")
                var notif = NJY_NotifModel()
                notif.msgWhatsApp = 1
                bleService.sendNotif(notif)
            case 29: // MicrowearDeviceControl.ecgHr.value
                print("Sync 24-hour blood oxygen data command sent") // Mirip dengan mesBo2
                let asyncCallback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                    print("Success: \(result)")
                }, failure: { error in
                    print("Failure: \(error.localizedDescription)")
                })
                bleService.sendSysHourBloodData(asyncCallback)
            case 30: // MicrowearDeviceControl.mesHr.value
                print("Sync 24-hour heart rate data command sent") // Mirip dengan hrDay
            case 31: // MicrowearDeviceControl.mesBp.value
                print("Sync 24-hour blood pressure data command sent") // Mirip dengan bpDay
            case 32: // MicrowearDeviceControl.mesBo2.value
                print("Sync 24-hour blood oxygen data command sent") // Mirip dengan boDay
            case 33: // MicrowearDeviceControl.shutdown.value
                print("Shutdown command sent")
            case 34: // MicrowearDeviceControl.reboot.value
                print("Reboot command sent")
            case 35: // MicrowearDeviceControl.reset.value
                print("Factory reset command sent")
            case 36: // MicrowearDeviceControl.lowPowerShutdown.value
                print("Low power shutdown command sent")
            case 60: // MicrowearDeviceControl.productId.value
                print("Get device information command sent") // Mirip dengan device information

            case 61: // MicrowearDeviceControl.tpVer.value
                print("Get version command sent") // Mirip dengan version
            case 62: // MicrowearDeviceControl.firmwareVer.value
                print("Firmware OTA update command sent") // Mirip dengan firmware update
            case 63: // MicrowearDeviceControl.uiVer.value
                print("UI version command sent")
            case 64: // MicrowearDeviceControl.gSensorXyz.value
                print("GSENSOR coordinates command sent")
            case 65: // MicrowearDeviceControl.userInfo.value
                print("User info command sent")
                let userInfoModel = NJY_UserInfoModel()
                bleService.sendUserInfo(userInfoModel)
            case 66: // MicrowearDeviceControl.unitSystem.value
                print("Set unit system command sent")
            case 67: // MicrowearDeviceControl.dateTime.value
                print("Set date and time command sent")
                let curDateTimeModel = NJY_CurDateTimeModel()
                bleService.sendCurDateTime(curDateTimeModel)
            case 68: // MicrowearDeviceControl.timeMode.value
                print("Set time format command sent")
                bleService.sendTimeMode(0)
            case 69: // MicrowearDeviceControl.tempUnit.value
                print("Set temperature unit command sent")
            case 70: // MicrowearDeviceControl.language.value
                print("Set language command sent")
            case 71: // MicrowearDeviceControl.timeStyle.value
                print("System watch face command sent") // Mirip dengan watch face ID
            case 72: // MicrowearDeviceControl.bat.value
                print("Get battery level command sent")
                let asyncCallback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                    print("Success: \(result)")
                }, failure: { error in
                    print("Failure: \(error.localizedDescription)")
                })
                bleService.getDeviceBat(asyncCallback)
            case 73: // MicrowearDeviceControl.targetStep.value
                print("Step goal command sent")
            case 74: // MicrowearDeviceControl.hourStep.value
                print("Hourly steps command sent")
            case 75: // MicrowearDeviceControl.historySportData.value
                print("Exercise record command sent") // Mirip dengan exercise data
            case 76: // MicrowearDeviceControl.notifications.value
                print("Get notifications command sent")
            case 77: // MicrowearDeviceControl.displayTime.value
                print("Screen on duration command sent")
            case 78: // MicrowearDeviceControl.bandConfig.value
                print("Band configuration command sent")
            case 79: // MicrowearDeviceControl.sleepData.value
                print("Sleep data command sent")
            case 80: // MicrowearDeviceControl.sportRecord.value
                print("Exercise record command sent")
            case 81: // MicrowearDeviceControl.sportData.value
                print("Exercise data command sent")
            case 82: // MicrowearDeviceControl.weatherForecast.value
                print("Real-time weather command sent") // Mirip dengan real time weather
            case 83: // MicrowearDeviceControl.realTimeWeather.value
                print("Real-time weather command sent")
            case 84: // MicrowearDeviceControl.raiseWrist.value
                print("Raise wrist to wake screen command sent")
                let timeSetModel = NJY_TimeSetModel()
                bleService.sendRaiseWrist(timeSetModel)
            case 85: // MicrowearDeviceControl.disturb.value
                print("Do not disturb mode command sent")
            case 86: // MicrowearDeviceControl.longSit.value
                print("Long sit reminder sent")
            case 87: // MicrowearDeviceControl.drinkWater.value
                print("Drink water reminder sent")
            case 88: // MicrowearDeviceControl.washHand.value
                print("Hand washing reminder command sent")
            case 89: // MicrowearDeviceControl.schedule.value
                print("Schedule reminder command sent")
            case 90: // MicrowearDeviceControl.alarm.value
                print("Set alarm command sent")
            case 91: // MicrowearDeviceControl.temp.value
                print("Body temperature command sent")
            case 92: // MicrowearDeviceControl.hr.value
                print("Heart rate command sent")
            case 93: // MicrowearDeviceControl.bp.value
                print("Blood pressure command sent")
            case 94: // MicrowearDeviceControl.bo.value
                print("Blood oxygen command sent")
            case 95: // MicrowearDeviceControl.hrDay.value
                print("All-day heart rate command sent")
            case 96: // MicrowearDeviceControl.boDay.value
                print("All-day blood oxygen command sent")
            case 97: // MicrowearDeviceControl.tempDay.value
                print("All-day temperature command sent")
            case 98: // MicrowearDeviceControl.allDayFlag.value
                print("Set all-day measurement switch command sent")
            case 99: // MicrowearDeviceControl.hrBpBo2.value
                print("Test settings hr/bp/bo2 command sent")
            case 100: // MicrowearDeviceControl.takePhoto.value
                print("Take photo command sent")
            case 101: // MicrowearDeviceControl.ctrlMusic.value
                print("Music control command sent")
            case 102: // MicrowearDeviceControl.bandConfig1.value
                print("Band configuration 1 command sent")
            case 103: // MicrowearDeviceControl.phoneSystemType.value
                print("Set phone system type command sent")
            case 105: // MicrowearDeviceControl.appRequestSync.value
                print("Sync data command sent") // Mirip dengan sync data
            case 106: // MicrowearDeviceControl.findPhone.value
                print("Find phone command sent")
            case 107: // MicrowearDeviceControl.bpDay.value
                print("All-day blood pressure command sent")
            case 108: // MicrowearDeviceControl.addFriend.value
                print("Friend code or payment code command sent") // Mirip dengan add friend/receipt code
            case 109: // MicrowearDeviceControl.receiptCode.value
                print("Friend code or payment code command sent") // Mirip dengan add friend/receipt code
            case 110: // MicrowearDeviceControl.womenHealth.value
                print("Female health command sent")
            case 111: // MicrowearDeviceControl.androidPhoneCtrl.value
                print("Phone answer/hang up status command sent")
            case 112: // MicrowearDeviceControl.unbind.value
                print("Unbind command sent")
            case 120: // MicrowearDeviceControl.hrEcg.value
                print("Start ECG command sent") // Mirip dengan start ecg
            case 121: // MicrowearDeviceControl.deviceFun.value
                print("Device function command sent")
            case 122: // MicrowearDeviceControl.watchCallInfo.value
                print("Watch call info command sent")
            default:
                print("Invalid row selected")
            }
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    


    private func connectToDevice(with bleAddress: String, result: @escaping FlutterResult) {
        bleService.scan()
        DispatchQueue.main.asyncAfter(deadline: .now() + 3) { [self] in
            print("Ini dijalankan setelah 3 detik")
            let newAdress = bleAddress.replacingOccurrences(of: ":", with: "").lowercased()
            bleService.stopScan()
            if let peripherals = self.bleService.bleModals as? [NJY_Peripherial] {
                for peripheral in peripherals {
                    if peripheral.mac == newAdress {
                        macAddressCache = bleAddress
                        bleService.connect(peripheral.peripheral)
                        result("Connected to \(peripheral.name)")
                        
                        
                        DispatchQueue.main.asyncAfter(deadline: .now() + 3) { [self] in
                            print("Ini juga dijalankan setelah 3 detik")
                            let asyncCallback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                                print("getCentralManagerDidUpdateState Success: \(result)")
                                var item = [String: Any]()
                                item["status"] = "onDiscoveredServices"
                                item["mac"] = self.macAddressCache
                                
                                self.registerConnectStatuesCallBackSink?(item)
                             
                            }, failure: { error in
                                print("getCentralManagerDidUpdateState Failure: \(error.localizedDescription)")
                                var item = [String: Any]()
                                item["status"] = "onConnectFail"
                                item["mac"] = self.macAddressCache

                                self.registerConnectStatuesCallBackSink?(item)
                                
                            })
                            bleService.getDeviceBat(asyncCallback)
                            return
                        }
                    }
                }
            }
            result(FlutterError(code: "DEVICE_NOT_FOUND", message: "Device with address \(bleAddress) not found", details: nil))
        }
       

    }

    func disconnectFromDevice(with bleAddress: String, result: @escaping FlutterResult) {
        let bleService = NJYBleService.sharedInstance()

        if let peripherals = bleService.bleModals as? [NJY_Peripherial] {
            for peripheral in peripherals {
                if peripheral.mac == bleAddress {
                    bleService.disconnectPeripheral(peripheral.peripheral)
                    result("Disconnected from \(peripheral.name)")
                    return
                }
            }
        }
        result(FlutterError(code: "DEVICE_NOT_FOUND", message: "Device with address \(bleAddress) not found", details: nil))
    }
}
