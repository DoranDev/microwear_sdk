import Flutter
import UIKit

public class MicrowearSdkPlugin: NSObject, FlutterPlugin {
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

    enum GPSCommand: Int {
        case requestPermission = 0      // Request GPS positioning permission
        case countdown = 1             // Request countdown start
        case start = 2                 // Request activity start
        case sync = 3                  // Sync activity data
        case pause = 4                 // Pause activity
        case lanjut = 5            // Continue activity
        case end = 6                   // End activity
        case appBusy = 7               // APP is busy and cannot respond
    }




    func setupNotifications() {
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(handleGPSNotification(_:)),
                                               name: Notification.Name(KISBLEGPSSPORT_NOTIF),
                                               object: nil)

        NotificationCenter.default.addObserver(self,
                                               selector: #selector(handleGPSNotification(_:)),
                                               name: Notification.Name(KBLEGPSSPORT_NOTIF),
                                               object: nil)

        NotificationCenter.default.addObserver(self,
                                               selector: #selector(handleGPSNotification(_:)),
                                               name: Notification.Name(KBLEGPSSPORTSTOP_NOTIF),
                                               object: nil)

        NotificationCenter.default.addObserver(self,
                                               selector: #selector(handleGPSNotification(_:)),
                                               name: Notification.Name(KBLEGPSSPORTCONTINUETO_NOTIF),
                                               object: nil)

        NotificationCenter.default.addObserver(self,
                                               selector: #selector(handleGPSNotification(_:)),
                                               name: Notification.Name(KBLEGPSSPORTEnd_NOTIF),
                                               object: nil)
    }


    var sportID: Int = 140


    let workoutTimer = WorkoutTimer(interval: 1)

    //    @objc private func handleGPSNotification(_ notification: Notification) {
    //        DispatchQueue.main.async { [weak self] in
    //              guard let self = self else { return }
    //
    //            print("Thread check: \(Thread.isMainThread ? "Main" : "Background")")
    //               // Handle notifikasi di main thread
    //            let name = notification.name.rawValue
    //            print("handleGPSNotification")
    //            var item = [String: Any]()
    //
    //            if name == KBLEGPSSPORT_NOTIF {
    //                print("KBLEGPSSPORT_NOTIF(0)")
    //
    //                item["status"] = "onGPSPermission"
    //                self.registerGPSCallBackSink?(item)
    //
    //                self.cmdIDX = 0
    //                var sportState = NJY_SportStateModel()
    //                sportState.cmdId = self.cmdIDX // Example command ID
    //                sportState.aid = 0  // Example activity ID
    //
    //                // 2. Configure required info model
    //                var sportInfo = NJY_SportInfoModel()
    //                sportInfo.sport_state = 0     // Active state
    //                sportInfo.sport_type = self.sportID      //
    //                sportInfo.sport_gps = 0
    //                sportInfo.sport_hr = 0
    //                sportInfo.sport_valid = 0      // Valid data
    //                sportInfo.sport_cadence = 0
    //                sportInfo.sport_stride = 0
    //                sportInfo.reserve3 = 0
    //                sportInfo.sport_time = 0        // Start from 0 seconds
    //                sportInfo.sport_distance = 0    // Start from 0 meters
    //                sportInfo.sport_steps = 0       // Start from 0 steps
    //                sportInfo.sport_kcal = 0
    //                sportInfo.sport_speed = 0
    //    //
    //                sportState.infoModel = sportInfo
    //
    //
    //                let asyncGPSCallback = NJYAsyncCallback<AnyObject>.create(
    //                    self,
    //                    success: { [self] result in
    //                        if let model = result as? NJY_SportStateModel {
    //                            print("Get getGPSSynData Success: aid:\(model.aid) cmdId:\(model.cmdId)")
    //                            if(model.cmdId == 1){
    //                                // Now you can access model's properties
    //                                self.sportID = model.infoModel.sport_type
    //                                self.aid = model.aid
    //    //                            let sportState = NJY_SportStateModel()
    //    //                            sportState.cmdId = 1// Example command ID
    //    //                            sportState.aid = model.aid  // Example activity ID
    //    //
    //    //                            // 2. Configure required info model
    //    //                            let sportInfo = NJY_SportInfoModel()
    //    //                            sportInfo.sport_state = 1     // Active state
    //    //                            sportInfo.sport_type = model.infoModel.sport_type       // Running
    //    //                            sportInfo.sport_gps = 0
    //    //                            sportInfo.sport_hr = 0
    //    //                            sportInfo.sport_valid = 1       // Valid data
    //    //                            sportInfo.sport_cadence = 0
    //    //                            sportInfo.sport_stride = 0
    //    //                            sportInfo.reserve3 = 0
    //    //                            sportInfo.sport_time = 0        // Start from 0 seconds
    //    //                            sportInfo.sport_distance = 0    // Start from 0 meters
    //    //                            sportInfo.sport_steps = 0       // Start from 0 steps
    //    //                            sportInfo.sport_kcal = 0
    //    //                            sportInfo.sport_speed = 0
    //    //
    //    //                            sportState.infoModel = sportInfo
    //    //                            bleService.getGPSStart(sportState)
    //                                item["status"] = "onGPSPermission"
    //                                self.registerGPSCallBackSink?(item)
    //                            }
    //                        } else {
    //                            print("Received unexpected result type: \(type(of: result))")
    //                        }
    //                    }, failure: { error in
    //                        print("getGPSSynData Failure: \(error.localizedDescription)")
    //                    })
    //
    //                DispatchQueue.main.async {
    //                    self.bleService.getGPSSynData(sportState, callback: asyncGPSCallback)
    //                }
    //
    //                DispatchQueue.main.asyncAfter(deadline: .now() + 1) { [self] in
    //                    print("onGPSCountDown dijalankan setelah 1 detik")
    //
    //                    item["status"] = "onGPSCountdown"
    //                    self.registerGPSCallBackSink?(item)
    //                }
    //
    //            }
    //            else if name == KISBLEGPSSPORT_NOTIF {
    //                self.cmdIDX = 3
    //                print("KISBLEGPSSPORT_NOTIF(2)")
    //                var sportState = NJY_SportStateModel()
    //                sportState.cmdId = 3 // Example command ID
    //                sportState.aid = aid  // Example activity ID
    //
    //                // 2. Configure required info model
    //                var sportInfo = NJY_SportInfoModel()
    //                sportInfo.sport_state = 0     // Active state
    //                sportInfo.sport_type = self.sportID      //
    //                sportInfo.sport_gps = 0
    //                sportInfo.sport_hr = 0
    //                sportInfo.sport_valid = 0      // Valid data
    //                sportInfo.sport_cadence = 0
    //                sportInfo.sport_stride = 0
    //                sportInfo.reserve3 = 0
    //                sportInfo.sport_time = 0        // Start from 0 seconds
    //                sportInfo.sport_distance = 0    // Start from 0 meters
    //                sportInfo.sport_steps = 0       // Start from 0 steps
    //                sportInfo.sport_kcal = 0
    //                sportInfo.sport_speed = 0
    //    //
    //                sportState.infoModel = sportInfo
    //
    //
    //                let asyncGPSCallback = NJYAsyncCallback<AnyObject>.create(
    //                    self,
    //                    success: { [self] result in
    //                        if let model = result as? NJY_SportStateModel {
    //                            print("Get start Success: aid:\(model.aid) cmdId:\(model.cmdId)")
    //                        } else {
    //                            print("Received unexpected result type: \(type(of: result))")
    //                        }
    //                }, failure: { error in
    //                    print("getGPSSynData start Failure: \(error.localizedDescription)")
    //                })
    //                DispatchQueue.main.async {
    //                    self.bleService.getGPSSynData(sportState, callback: asyncGPSCallback)
    //                }
    //                DispatchQueue.main.asyncAfter(deadline: .now() + 3) { [self] in
    //                    print("onGPSStart dijalankan setelah 3 detik")
    //
    //                    item["status"] = "onGPSStart"
    //                    item["sportId"] = self.sportID
    //                    self.registerGPSCallBackSink?(item)
    //                }
    //
    //
    //            }
    //            else if name == KBLEGPSSPORTSTOP_NOTIF {
    //                self.cmdIDX =  4
    //    //            let sportState = NJY_SportStateModel()
    //    //            sportState.cmdId = cmdIDX // Example command ID
    //    //            sportState.aid = sportID  // Example activity ID
    //    //
    //    //            // 2. Configure required info model
    //    //            let sportInfo = NJY_SportInfoModel()
    //    //            sportInfo.sport_state = 0      // Active state
    //    //            sportInfo.sport_type = sportID       // Running
    //    //            sportInfo.sport_gps = 0
    //    //            sportInfo.sport_hr = 0
    //    //            sportInfo.sport_valid = 1       // Valid data
    //    //            sportInfo.sport_cadence = 0
    //    //            sportInfo.sport_stride = 0
    //    //            sportInfo.reserve3 = 0
    //    //            sportInfo.sport_time = 0        // Start from 0 seconds
    //    //            sportInfo.sport_distance = 0    // Start from 0 meters
    //    //            sportInfo.sport_steps = 0       // Start from 0 steps
    //    //            sportInfo.sport_kcal = 0
    //    //            sportInfo.sport_speed = 0
    //    //
    //    //            sportState.infoModel = sportInfo
    //    //            bleService.getGPSStart(sportState)
    //                print("KBLEGPSSPORTSTOP_NOTIF(3)")
    //                item["status"] = "onGPSPause"
    //                self.registerGPSCallBackSink?(item)
    //            }
    //            else if name == KBLEGPSSPORTCONTINUETO_NOTIF {
    //
    //                 self.cmdIDX = 5
    //
    //    //            let sportState = NJY_SportStateModel()
    //    //            sportState.cmdId = cmdIDX // Example command ID
    //    //            sportState.aid = sportID  // Example activity ID
    //    //
    //    //            // 2. Configure required info model
    //    //            let sportInfo = NJY_SportInfoModel()
    //    //            sportInfo.sport_state = 0      // Active state
    //    //            sportInfo.sport_type = sportID       // Running
    //    //            sportInfo.sport_gps = 0
    //    //            sportInfo.sport_hr = 0
    //    //            sportInfo.sport_valid = 1       // Valid data
    //    //            sportInfo.sport_cadence = 0
    //    //            sportInfo.sport_stride = 0
    //    //            sportInfo.reserve3 = 0
    //    //            sportInfo.sport_time = 0        // Start from 0 seconds
    //    //            sportInfo.sport_distance = 0    // Start from 0 meters
    //    //            sportInfo.sport_steps = 0       // Start from 0 steps
    //    //            sportInfo.sport_kcal = 0
    //    //            sportInfo.sport_speed = 0
    //    //
    //    //            sportState.infoModel = sportInfo
    //    //            bleService.getGPSStart(sportState)
    //                print("KBLEGPSSPORTCONTINUETO_NOTIF(5)")
    //                item["status"] = "onGPSContinue"
    //                self.registerGPSCallBackSink?(item)
    //            }
    //            else if name == KBLEGPSSPORTEnd_NOTIF {
    //                 self.cmdIDX = 6
    //    //            let sportState = NJY_SportStateModel()
    //    //            sportState.cmdId = cmdIDX // Example command ID
    //    //            sportState.aid = sportID  // Example activity ID
    //    //
    //    //            // 2. Configure required info model
    //    //            let sportInfo = NJY_SportInfoModel()
    //    //            sportInfo.sport_state = 0      // Active state
    //    //            sportInfo.sport_type = sportID       // Running
    //    //            sportInfo.sport_gps = 0
    //    //            sportInfo.sport_hr = 0
    //    //            sportInfo.sport_valid = 0       // Valid data
    //    //            sportInfo.sport_cadence = 0
    //    //            sportInfo.sport_stride = 0
    //    //            sportInfo.reserve3 = 0
    //    //            sportInfo.sport_time = 0        // Start from 0 seconds
    //    //            sportInfo.sport_distance = 0    // Start from 0 meters
    //    //            sportInfo.sport_steps = 0       // Start from 0 steps
    //    //            sportInfo.sport_kcal = 0
    //    //            sportInfo.sport_speed = 0
    //    //
    //    //            sportState.infoModel = sportInfo
    //    //            bleService.getGPSStart(sportState)
    //                item["status"] = "onGPSEnd"
    //                item["sportId"] = self.sportID
    //                self.registerGPSCallBackSink?(item)
    //                print("KBLEGPSSPORTEnd_NOTIF (6)")
    //            }else{
    //                print("GAK KEDETEKSI \(name)")
    //            }
    //           }
    //
    //    }

    @objc private func handleGPSNotification(_ notification: Notification) {

        let name = notification.name.rawValue
        print("handleGPSNotification")
        var item = [String: Any]()

        if name == KBLEGPSSPORT_NOTIF {
            print("KBLEGPSSPORT_NOTIF(0)")

            var sportState = NJY_SportStateModel()
            sportState.cmdId = GPSCommand.requestPermission.rawValue// Example command ID
            sportState.aid = 1  // Example activity ID

            self.bleService.getGPSSynData(sportState, callback: NJYAsyncCallback<AnyObject>.create(
                nil,
                success: { result in
                    if let model = result as? NJY_SportStateModel {
                        print("Get getGPSSynData Success: aid:\(model.aid) cmdId:\(model.cmdId)")
                        if(model.cmdId == 1){
                            self.sportID = model.aid
                            print("START")
                            var sportState1 = NJY_SportStateModel()
                            sportState1.cmdId = GPSCommand.requestPermission.rawValue
                            sportState1.aid = model.aid
                            self.bleService.getGPSStart(sportState1)

                            item["status"] = "onGPSPermission"
                            self.registerGPSCallBackSink?(item)

                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { [self] in
//                                print("onGPSCountDown dijalankan setelah 1 detik")

                                item["status"] = "onGPSCountdown"
                                self.registerGPSCallBackSink?(item)
                            }
                        }
                    } else {
                        print("Received unexpected result type: \(type(of: result))")
                    }
                }, failure: { error in
                    print("getGPSSynData Failure: \(error.localizedDescription)")
                }))

        }
        else if name == KISBLEGPSSPORT_NOTIF {

            workoutTimer.start()
            print("KISBLEGPSSPORT_NOTIF(2)")
            item["status"] = "onGPSStart"
            item["sportId"] = self.sportID
            self.registerGPSCallBackSink?(item)

        }
        else if name == KBLEGPSSPORTSTOP_NOTIF {
            workoutTimer.pause()
            print("pause")
            var sportState1 = NJY_SportStateModel()
            sportState1.cmdId = GPSCommand.pause.rawValue
            sportState1.aid = 1
            self.bleService.getGPSStart(sportState1)
            print("KBLEGPSSPORTSTOP_NOTIF(3)")
            item["status"] = "onGPSPause"
            self.registerGPSCallBackSink?(item)
        }
        else if name == KBLEGPSSPORTCONTINUETO_NOTIF {
            workoutTimer.resume()
            print("lanjut")
            var sportState1 = NJY_SportStateModel()
            sportState1.cmdId = GPSCommand.lanjut.rawValue
            sportState1.aid = 1
            self.bleService.getGPSStart(sportState1)
            print("KBLEGPSSPORTCONTINUETO_NOTIF(5)")
            item["status"] = "onGPSContinue"
            self.registerGPSCallBackSink?(item)
        }
        else if name == KBLEGPSSPORTEnd_NOTIF {
            workoutTimer.stop()
            print("stop")
            var sportState1 = NJY_SportStateModel()
            sportState1.cmdId = GPSCommand.end.rawValue
            sportState1.aid = 1
            self.bleService.getGPSStart(sportState1)
            item["status"] = "onGPSEnd"
            item["sportId"] = self.sportID
            self.registerGPSCallBackSink?(item)
            print("KBLEGPSSPORTEnd_NOTIF (6)")
        }else{
            print("GAK KEDETEKSI \(name)")
        }

    }

    init(_ channel: FlutterMethodChannel) {
        super.init()
        setupNotifications()
        workoutTimer.onStart = { initialTick in
            print("⏱ [START] Tick awal: \(initialTick)")
        }

        workoutTimer.onResume = { resumedTick in
            print("↩️ [RESUME] Dari tick: \(resumedTick)")
        }

        workoutTimer.onTick = { currentTick, elapsed in
            print("✅ [TICK #\(currentTick)] \(String(format: "%.2f", elapsed)) detik")
            print("sportID: \(self.sportID)")
            var sportStates = NJY_SportStateModel()
            sportStates.cmdId = 3
            sportStates.aid = 1
            var sportInfo2 = NJY_SportInfoModel()
            sportInfo2.sport_state = 0
            sportInfo2.sport_type = self.sportID
            sportInfo2.sport_gps = 0

            sportInfo2.sport_hr = self.sportHr
            sportInfo2.sport_valid = self.sportValid
            sportInfo2.sport_cadence = self.sportCadence
            sportInfo2.sport_stride = self.sportStride
            sportInfo2.reserve3 = 0
            sportInfo2.sport_time = Int(elapsed.rounded())
            sportInfo2.sport_speed = self.sportSpeed
            sportInfo2.sport_distance = self.sportDistance
            sportInfo2.sport_steps = self.sportSteps
            sportInfo2.sport_kcal = self.sportKcal
            sportStates.infoModel = sportInfo2
            DispatchQueue.main.async {

                if Int(elapsed.rounded()) % 1 == 0 {

                    self.bleService.getGPSSynData(sportStates, callback: NJYAsyncCallback<AnyObject>.create(
                        nil,
                        success: { result in
                            if let model = result as? NJY_SportStateModel {
                                print("getGPSSynData sportState: aid:\(model.aid) cmdId:\(model.cmdId)")

                            } else {
                                print("Received unexpected result type: \(type(of: result))")
                            }
                        },
                        failure: { error in
                            // Handle any errors received during the data fetch.
                            print("getGPSSynDataPlay Failure: \(error.localizedDescription)")
                        }
                    ))
                }
            }

        }

        workoutTimer.onPause = { totalTicks, elapsed in
            print("⏸ [PAUSE] Pada tick \(totalTicks) (\(String(format: "%.2f", elapsed)) detik)")
        }

        workoutTimer.onStop = { finalCount, totalTime in
            print("🛑 [STOP] Total: \(finalCount) tick (\(String(format: "%.2f", totalTime)) detik)")
        }

    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    func toDictionary<T: Encodable>(from model: T) -> [String: Any]? {
        do {
            let data = try JSONEncoder().encode(model)
            let dictionary = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String: Any]
            return dictionary
        } catch {
            print("Error converting model to dictionary: \(error)")
            return nil
        }
    }



    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "microwear_sdk", binaryMessenger: registrar.messenger())
        let instance = MicrowearSdkPlugin(channel)
        registrar.addMethodCallDelegate(instance, channel: channel)
        registrar.addApplicationDelegate(instance)

        let deviceDataReceivedHandler = DeviceDataReceivedStreamHandler(plugin: instance)
        let deviceDataReceivedChannel = FlutterEventChannel(name: deviceDataReceivedChannelName, binaryMessenger: registrar.messenger())
        deviceDataReceivedChannel.setStreamHandler(deviceDataReceivedHandler)

        let batteryLevelHandler = BatteryLevelStreamHandler(plugin: instance)
        let batteryLevelChannel = FlutterEventChannel(name: batteryLevelChannelName, binaryMessenger: registrar.messenger())
        batteryLevelChannel.setStreamHandler(batteryLevelHandler)

        let syncHourStepHandler = SyncHourStepStreamHandler(plugin: instance)
        let syncHourStepChannel = FlutterEventChannel(name: syncHourStepChannelName, binaryMessenger: registrar.messenger())
        syncHourStepChannel.setStreamHandler(syncHourStepHandler)

        let syncWeekDaySportsHandler = SyncWeekDaySportsStreamHandler(plugin: instance)
        let syncWeekDaySportsChannel = FlutterEventChannel(name: syncWeekDaySportsChannelName, binaryMessenger: registrar.messenger())
        syncWeekDaySportsChannel.setStreamHandler(syncWeekDaySportsHandler)

        let deviceConfigHandler = DeviceConfigStreamHandler(plugin: instance)
        let deviceConfigChannel = FlutterEventChannel(name: deviceConfigChannelName, binaryMessenger: registrar.messenger())
        deviceConfigChannel.setStreamHandler(deviceConfigHandler)

        let syncSleepDataHandler = SyncSleepDataStreamHandler(plugin: instance)
        let syncSleepDataChannel = FlutterEventChannel(name: syncSleepDataChannelName, binaryMessenger: registrar.messenger())
        syncSleepDataChannel.setStreamHandler(syncSleepDataHandler)

        let syncSportRecordHandler = SyncSportRecordStreamHandler(plugin: instance)
        let syncSportRecordChannel = FlutterEventChannel(name: syncSportRecordChannelName, binaryMessenger: registrar.messenger())
        syncSportRecordChannel.setStreamHandler(syncSportRecordHandler)

        let getAlarmClockInfoHandler = GetAlarmClockInfoStreamHandler(plugin: instance)
        let getAlarmClockInfoChannel = FlutterEventChannel(name: getAlarmClockInfoChannelName, binaryMessenger: registrar.messenger())
        getAlarmClockInfoChannel.setStreamHandler(getAlarmClockInfoHandler)

        let syncBloodPressureHandler = SyncBloodPressureStreamHandler(plugin: instance)
        let syncBloodPressureChannel = FlutterEventChannel(name: syncBloodPressureChannelName, binaryMessenger: registrar.messenger())
        syncBloodPressureChannel.setStreamHandler(syncBloodPressureHandler)

        let syncHeartDataHandler = SyncHeartDataStreamHandler(plugin: instance)
        let syncHeartDataChannel = FlutterEventChannel(name: syncHeartDataChannelName, binaryMessenger: registrar.messenger())
        syncHeartDataChannel.setStreamHandler(syncHeartDataHandler)

        let syncOxDataHandler = SyncOxDataStreamHandler(plugin: instance)
        let syncOxDataChannel = FlutterEventChannel(name: syncOxDataChannelName, binaryMessenger: registrar.messenger())
        syncOxDataChannel.setStreamHandler(syncOxDataHandler)

        let syncHomeDataHandler = SyncHomeDataStreamHandler(plugin: instance)
        let syncHomeDataChannel = FlutterEventChannel(name: syncHomeDataChannelName, binaryMessenger: registrar.messenger())
        syncHomeDataChannel.setStreamHandler(syncHomeDataHandler)

        let syncRealTimeECGHandler = SyncRealTimeECGStreamHandler(plugin: instance)
        let syncRealTimeECGChannel = FlutterEventChannel(name: syncRealTimeECGChannelName, binaryMessenger: registrar.messenger())
        syncRealTimeECGChannel.setStreamHandler(syncRealTimeECGHandler)

        let getDeviceFunHandler = GetDeviceFunStreamHandler(plugin: instance)
        let getDeviceFunChannel = FlutterEventChannel(name: getDeviceFunChannelName, binaryMessenger: registrar.messenger())
        getDeviceFunChannel.setStreamHandler(getDeviceFunHandler)

        let getDeviceConfig1Handler = GetDeviceConfig1StreamHandler(plugin: instance)
        let getDeviceConfig1Channel = FlutterEventChannel(name: getDeviceConfig1ChannelName, binaryMessenger: registrar.messenger())
        getDeviceConfig1Channel.setStreamHandler(getDeviceConfig1Handler)

        let connectStatusHandler = ConnectStatusStreamHandler(plugin: instance)
        let registerConnectStatuesCallBackChannel = FlutterEventChannel(name: registerConnectStatuesCallBackChannelName, binaryMessenger: registrar.messenger())
        registerConnectStatuesCallBackChannel.setStreamHandler(connectStatusHandler)

        let registerSomatosensoryGameCallbackHandler = RegisterSomatosensoryGameCallbackStreamHandler(plugin: instance)
        let registerSomatosensoryGameCallbackChannel = FlutterEventChannel(name: registerSomatosensoryGameCallbackChannelName, binaryMessenger: registrar.messenger())
        registerSomatosensoryGameCallbackChannel.setStreamHandler(registerSomatosensoryGameCallbackHandler)

        let registerSingleHeartOxBloodCallbackHandler = RegisterSingleHeartOxBloodCallbackStreamHandler(plugin: instance)
        let registerSingleHeartOxBloodCallbackChannel = FlutterEventChannel(name: registerSingleHeartOxBloodCallbackChannelName, binaryMessenger: registrar.messenger())
        registerSingleHeartOxBloodCallbackChannel.setStreamHandler(registerSingleHeartOxBloodCallbackHandler)

        let registerMac3CallBackHandler = RegisterMac3CallBackStreamHandler(plugin: instance)
        let registerMac3CallBackChannel = FlutterEventChannel(name: registerMac3CallBackChannelName, binaryMessenger: registrar.messenger())
        registerMac3CallBackChannel.setStreamHandler(registerMac3CallBackHandler)


        let registerGPSCallBackHandler = RegisterGPSCallBackStreamHandler(plugin: instance)
        let registerGPSCallBackChannel = FlutterEventChannel(name: registerGPSCallBackChannelName, binaryMessenger: registrar.messenger())
        registerGPSCallBackChannel.setStreamHandler(registerGPSCallBackHandler)

        let onLoadingHandler = OnLoadingStreamHandler(plugin: instance)
        let onLoadingChannel = FlutterEventChannel(name: onLoadingChannelName, binaryMessenger: registrar.messenger())
        onLoadingChannel.setStreamHandler(onLoadingHandler)

    }


    var sportKcal =  0
    var sportHr =  0
    var sportValid =  1
    var sportTime =  0
    var sportStride = 0
    var sportCadence =  0
    var sportSteps =  0
    var sportDistance = 0
    var sportSpeed =  0
    var sportType = 0


    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let args = call.arguments as? [String: Any]

        switch call.method {
        case "connect":
            let macAddress = args?["macAddress"] as? String ?? ""
            connectToDevice(with: macAddress, result: result)
        case "reconnect":
            bleService.reconnect()
        case "creteBond":
            let macAddress = args?["macAddress"] as? String ?? ""
            // TODO: Implement createBond functionality
            result(nil)
        case "startService":
            // TODO: Implement startService functionality
            result("service_started")
        case "stopService":
            // TODO: Implement stopService functionality
            bleService.stopScan()
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
            disconnectFromDevice(with: macAddressCache, result: result)
        case "sendRequest":
            let microwearDeviceControlValue = args?["microwearDeviceControl"] as? Int
            let data = args?["data"] as? [String: Any]

            switch microwearDeviceControlValue {
            case 0: // MicrowearDeviceControl.alertFindWatch.value
                print("Find watch command sent")
                bleService.sendAlertFindWatch()
            case 11: // MicrowearDeviceControl.alertMsg.value
                print("Message push notification command sent")
                let notif = NJY_NotifModel()
                notif.msgWhatsApp = 1
                bleService.sendNotif(notif)

            case 29: // MicrowearDeviceControl.ecgHr.value
                print("Sync 24-hour blood oxygen data command sent") // Mirip dengan mesBo2
                let asyncCallback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                    print("Sync 24-hour blood Success: \(result)")
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
                let timeModel = NJY_CurDateTimeModel()
                let dateNow = Date()
                let tempInterval = dateNow.timeIntervalSince1970

                let zone = TimeZone.current
                let offset = TimeInterval(zone.secondsFromGMT())
                let curInterval = tempInterval + offset

                timeModel.curInterval = Int(curInterval)
                timeModel.curTimeZone = 0
                timeModel.curTimeZone2 = 0
                bleService.sendCurDateTime(timeModel)
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
                    print("Get battery  Success: \(result)")
                    if let batteryLevelSink = self.batteryLevelSink {
                        print("About to send battery to Flutter")
                        var item = [String: Any]()
                        item["battery"] = result

                        batteryLevelSink(item)
                    } else {
                        print("batteryLevelSink nil")
                    }

                }, failure: { error in
                    print("Get battery Failure: \(error.localizedDescription)")
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
                print("sendTrainingData command sent")
                let async3Callback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                    if let model = result as?  NJY_SportRecordModel {
                        print("Get sendTrainingData Success: \(model.debugDescription)")
                        // Now you can access model's properties
                        if let syncSportRecordSink = self.syncSportRecordSink {
                            print("About to send sendTrainingData to Flutter")
                            var item = [String: Any]()
                            item["date"] = model.startTimeStamp
                            item["duration"] = model.sportTime
                            item["kcal"] = model.sportKcal
                            item["timeMill"] = model.startTimeStamp
                            item["distance"] = model.sportDistance
                            item["heartRate"] = model.sportAvgHr
                            item["stepNum"] = model.sportSteps
                            item["model"] = model.sportType.rawValue
                            print(item)
                            syncSportRecordSink(item)
                        } else {
                            print("syncSportRecordSink nil")
                        }
                    } else {
                        print("Received unexpected result type: \(type(of: result))")
                    }

                }, failure: { error in
                    print("Get sendTrainingData Failure: \(error.localizedDescription)")
                })
                bleService.sendTrainingData(async3Callback)

            case 81: // MicrowearDeviceControl.sportData.value
                print("Exercise data command sent")
            case 82: // MicrowearDeviceControl.weatherForecast.value
                print("Forecast weather command sent") // Mirip dengan real time weather
                guard let weathers = args?["weathers"] as? [[String: Any]] else {
                    break
                }

                let forecastModel = NJY_WeatherForecastModel()
                var infoModels = [NJY_WeatherForecastInfoModel]()

                for (index, weatherData) in weathers.enumerated() {
                    let infoModel = NJY_WeatherForecastInfoModel()

                    // Map the data from dictionary to model
                    infoModel.week = weatherData["week"] as? Int ?? index
                    infoModel.type = weatherData["weatherType"] as? Int ?? 0
                    infoModel.temp_high = weatherData["highestTemp"] as? Int ?? 32
                    infoModel.temp_low = weatherData["minimumTemp"] as? Int ?? 29
                    infoModel.pressure = weatherData["pressure"] as? Int ?? 1013
                    infoModel.ult_level = weatherData["ultLevel"] as? Int ?? 2
                    infoModel.humidity = weatherData["humidity"] as? Int ?? 50
                    infoModel.wind_dir = wind_diryType(rawValue: weatherData["windDirDay"] as? Int ?? 0) ?? wind_diryType.NONE
                    infoModel.wind_lvl = weatherData["windScaleDay"] as? Int ?? 1
                    infoModel.visibility = weatherData["vis"] as? Int ?? 10000
                    infoModel.precipitation = weatherData["precip"] as? Int ?? 0

                    infoModels.append(infoModel)

                    // Stop after 7 days if there's more data
                    if infoModels.count >= 7 {
                        break
                    }
                }

                // Fill remaining days with default data if less than 7 days provided
                while infoModels.count < 7 {
                    let defaultModel = NJY_WeatherForecastInfoModel()
                    defaultModel.week = infoModels.count
                    defaultModel.type = 0
                    defaultModel.temp_high = 31
                    defaultModel.temp_low = 29
                    // Set other default values...
                    infoModels.append(defaultModel)
                }

                forecastModel.infoModelList = infoModels

                bleService.sendWeatherForecast(forecastModel)
            case 83: // MicrowearDeviceControl.realTimeWeather.value
                print("Real-time weather command sent")
                let tempData = args?["tempData"] as? Int ?? 31
                let weatherTypeRaw = args?["weatherType"] as? Int ?? 0

                let weatherModel = NJY_WeatherModel()

                // Konversi integer ke enum WeatherType
                let weatherTypeNew = weatherType(rawValue: weatherTypeRaw) ?? weatherType.SUNNY

                weatherModel.curTemp = tempData
                weatherModel.type = weatherTypeNew

                bleService.sendRealTimeWeather(weatherModel)
            case 84: // MicrowearDeviceControl.raiseWrist.value
                print("Raise wrist to wake screen command sent")
                let timeSetModel = NJY_TimeSetModel()
                timeSetModel.isOn = true

                bleService.sendRaiseWrist(timeSetModel)
            case 85: // MicrowearDeviceControl.disturb.value
                print("Do not disturb mode command sent")
            case 86: // MicrowearDeviceControl.longSit.value
                print("Long sit reminder sent")
            case 87: // MicrowearDeviceControl.drinkWater.value
                print("Drink water reminder sent")
            case 88: // MicrowearDeviceControl.washHand.value'
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
                var isOpen = false
                if data != nil {
                    isOpen = data?["isOpen"] as? Bool ?? false
                }

                let photoCallback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                    // Handle the result
                    var item = [String: Any]()
                    item["takePhone"] = result
                    self.registerSingleHeartOxBloodCallbackSink?(item)
                }, failure: { error in
                    print("photoCallback Failure: \(error.localizedDescription)")
                })

                bleService.sendTakePhoto(isOpen, callback: photoCallback)
            case 101: // MicrowearDeviceControl.ctrlMusic.value
                print("Music control command sent")

            case 102: // MicrowearDeviceControl.bandConfig1.value
                print("Band configuration 1 command sent")
            case 103: // MicrowearDeviceControl.phoneSystemType.value
                print("Set phone system type command sent")
            case 105: // MicrowearDeviceControl.appRequestSync.value
                print("Sync data command sent") // Mirip dengan sync data
                let callBack = NJYAsyncCallback<AnyObject>.create(self, success: { result in

                    let sysModel = result as! NJY_SysDataModel
                    let currentTimeMillis = Date.now.ISO8601Format()
                    var item = [String: Any]()

                    // Add data to the dictionary
                    item["stepData"] = ["duration": 0.50, "timeMill": currentTimeMillis, "distance": sysModel.distance, "stepNum": sysModel.steps, "calData": sysModel.kcal]
                    item["bloodOxyData"] = ["timeMill": currentTimeMillis, "bloodOxy": sysModel.spo2]
                    item["heartData"] = ["timeMill": currentTimeMillis, "heartRate": sysModel.sbp]

                    // Use the item dictionary...
                    print(item)
                    self.syncHomeDataSink!(item)

                }, failure: { error in
                    print("syncHomeDataSink Failure: \(error.localizedDescription)")
                })

                bleService.sendSysData(callBack)
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
            case 121: // MicrowearDeviceControl.deviceFun.value
                print("Device function command sent")
                let asyncCallback = NJYAsyncCallback<AnyObject>.create(self, success: { result in
                    print("Get Device function  Success: \(result)")

                    if let deviceFunModel: NJY_DeviceFunModel = result as? NJY_DeviceFunModel {
                        print("Get Device function Success: \(deviceFunModel)")
                        var item = [String: Any]()
                        item["customVideoDial"] = result.customVideoDial
                        item["customDialVersion"] = result.customDialVersion
                        item["customSupportMedic"] = result.customSupportMedic
                        item["customSupportGpsSport"] = result.customSupportGpsSport
                        item["customSupportUserHeadLogo"] = result.customSupportUserHeadLogo
                        item["customSupportAlbumw"] = result.customSupportAlbumw
                        item["customSupportAlbumh"] = result.customSupportAlbumh
                        item["customSupportLyric"] = result.customSupportLyric
                        self.getDeviceFunSink?(item)
                    } else {
                        print("Get Device function Success: Unable to cast result to NJY_DeviceFunModel")
                    }
                }, failure: { error in
                    print("Get Device function Failure: \(error.localizedDescription)")
                })
                bleService.getDeviceFun(asyncCallback)
            case 122: // MicrowearDeviceControl.watchCallInfo.value
                print("Watch call info command sent")
            case 200: // MicrowearDeviceControl.otaStart.value
                print("OTA START command sent")
                var otaType = ""
                if data != nil {
                    otaType = data?["otaType"] as? String ?? ""
                }
                switch otaType {
                case "startPushDial":
                    if let dialPath = data?["path"] as? String {

                        let fileManager = FileManager.default
                        if(fileManager.fileExists(atPath: dialPath)){


                            // Buat instance NJYAsyncCallback dengan progress
                            let callback = NJYAsyncCallback.create(nil,
                                                                   success: { (result: AnyObject) in
                                // Handle success
                                print("Upgrade successful: \(result)")
                                var item = [String: Any]()
                                item["status"] = "onPushSuccess"
                                self.onLoadingSink?(item)
                            },
                                                                   progress: { (progress: Float) in
                                // Handle progress
                                print("Progress nya startPushDial:\(progress): \(progress * 100)%")
                                var item = [String: Any]()
                                item["status"] = "onPushProgress"
                                item["progress"] = progress * 100
                                self.onLoadingSink?(item)
                            },
                                                                   failure: { (error: Error) in
                                // Handle failure
                                print("Upgrade failed: \(error.localizedDescription)")
                                print("dial path: \(dialPath)")
                                var item = [String: Any]()
                                item["status"] = "onPushError"
                                self.onLoadingSink?(item)
                            }
                            )

                            let binaryFileURL = URL(fileURLWithPath: dialPath)
                            do {
                                let binaryData = try Data(contentsOf: binaryFileURL)
                                print("Successfully loaded binary data: \(binaryData.count) bytes")
                                // Use binaryData as needed
                                bleService.sendDialInstall(dialPath, data: binaryData, type: 1, callback: callback)
                            } catch {
                                print("Error loading binary file: \(error)")
                            }

                        }else{
                            print("tidak ada file di path")
                        }
                    } else {
                        // Handle kasus ketika dialPath nil
                        print("dialPath is nil")
                    }
                case "startPushCustomDial":
                    print("startPushCustomDial")
                    // Buat instance NJYAsyncCallback dengan progress

                    let callback = NJYAsyncCallback.create(nil,
                                                           success: { (result: AnyObject) in
                        // Handle success
                        print("Upgrade successful: \(result)")
                        var item = [String: Any]()
                        item["status"] = "onPushSuccess"
                        self.onLoadingSink?(item)
                    },
                                                           progress: { (progress: Float) in
                        // Handle progress
                        print("Progress: \(progress * 100)%")
                        var item = [String: Any]()
                        item["status"] = "onPushProgress"
                        item["progress"] = progress * 100
                        self.onLoadingSink?(item)
                    },
                                                           failure: { (error: Error) in
                        // Handle failure
                        print("Upgrade failed: \(error.localizedDescription)")
                        var item = [String: Any]()
                        item["status"] = "onPushError"
                        self.onLoadingSink?(item)
                    }
                    )

                    var path = ""
                    var bigWidth = 320
                    var bigHeight = 380
                    var smallNeedWidth = 240
                    var smallNeedHeight = 283
                    var timePosition = 0
                    var colors = "#FF0000"
                    var colorValue: UInt64 = 0

                    if let data = data {
                        path = data["path"] as? String ?? path
                        bigWidth = data["bigWidth"] as? Int ?? bigWidth
                        bigHeight = data["bigHeight"] as? Int ?? bigHeight
                        smallNeedWidth = data["smallNeedWidth"] as? Int ?? smallNeedWidth
                        smallNeedHeight = data["smallNeedHeight"] as? Int ?? smallNeedHeight
                        timePosition = data["timePosition"] as? Int ?? timePosition
                        colors = data["colors"] as? String ?? colors
                    }

                    let nJY_DailInfoModel = NJY_DailInfoModel()

                    nJY_DailInfoModel.dateLocation = 0
                    nJY_DailInfoModel.dateTopPosition = 0
                    nJY_DailInfoModel.dateBelowPosition = 0

                    // Then adjust your scanning code:
                    let scanner = Scanner(string: colors.replacingOccurrences(of: "#", with: ""))
                    if scanner.scanHexInt64(&colorValue) {
                        let r = Int((colorValue >> 16) & 0xFF)  // Extract red component
                        let g = Int((colorValue >> 8) & 0xFF)   // Extract green component
                        let b = Int(colorValue & 0xFF)          // Extract blue component

                        nJY_DailInfoModel.colorR = r
                        nJY_DailInfoModel.colorG = g
                        nJY_DailInfoModel.colorB = b
                    } else {
                        print("Failed to parse hex value")
                    }

                    nJY_DailInfoModel.screenHigh = bigHeight
                    nJY_DailInfoModel.screenWidth = bigWidth
                    nJY_DailInfoModel.imageHigh = smallNeedHeight
                    nJY_DailInfoModel.imageWidth = smallNeedWidth

                    do {
                        let bgData = try UIImage.getBitmapDataFromImageFile(atPath: path)
                        print("Successfully loaded bg data: \(bgData?.count) bytes")
                        nJY_DailInfoModel.imageBg = bgData! as Data
                    } catch {
                        print("Error loading bg file: \(error)")
                    }

                    do {
                        let thumbData = try UIImage.getBitmapDataFromImageFile(atPath: path)
                        print("Successfully loaded bg data: \(thumbData?.count) bytes")
                        nJY_DailInfoModel.thumbnailImage = thumbData as! Data
                    } catch {
                        print("Error loading bg file: \(error)")
                    }

                    nJY_DailInfoModel.dailType = 0


                    bleService.sendCustomDialInstall(nJY_DailInfoModel, type: 1, callback: callback)
                default:
                    print("Invalid otaType selected")
                }
            case 124:

                if data != nil {
                    let fun = data?["func"] as? String ?? ""
                    switch(fun){
                    case "startGPSStatus":
                     //   print("startGPSStatus command sent \(String(describing: data))")

                        break
                    case "syncGPSData":
                      //  print("syncGPSData command sent \(String(describing: data))")
                        var sportStates = NJY_SportStateModel()

                        sportKcal = data?["sportKcal"] as? Int ?? 0
                        sportHr = data?["sportHr"] as? Int ?? 0
                        sportValid = data?["sportValid"] as? Int ?? 1
                        sportTime = data?["sportTime"] as? Int ?? 0
                        sportStride = data?["sportStride"] as? Int ?? 0
                        sportCadence = data?["sportCadence"] as? Int ?? 0
                        sportSteps = data?["sportSteps"] as? Int ?? 0
                        sportDistance = data?["sportDistance"] as? Int ?? 0
                        sportSpeed = data?["sportSpeed"] as? Int ?? 0
                        sportType = data?["sportType"] as? Int ?? 0

                        break

                    default:
                        print("command sent \(String(describing: data))")
                        print("no Function")
                        break
                    }
                    //            case 124:
                    //                let cmd = cmdIDX
                    //                print("gpsSport cmdIDX \(cmd)")
                    //                if data != nil {
                    //                  let fun = data?["func"] as? String ?? ""
                    //                    switch(fun){
                    //                        case "startGPSStatus":
                    //                            print("startGPSStatus command sent \(String(describing: data))")
                    //
                    //                            var sportState1 = NJY_SportStateModel()
                    //                        sportState1.cmdId = 0 // Example command ID
                    //                        sportState1.aid = aid  // Example activity ID
                    //
                    //                            // 2. Configure required info model
                    //                            let sportInfo1 = NJY_SportInfoModel()
                    //                            sportInfo1.sport_state = 0     // Active state
                    //                            sportInfo1.sport_type = sportID       // Running
                    //                            sportInfo1.sport_gps = 0
                    //                            sportInfo1.sport_hr = 0
                    //                            sportInfo1.sport_valid = 1       // Valid data
                    //                            sportInfo1.sport_cadence = 0
                    //                            sportInfo1.sport_stride = 0
                    //                            sportInfo1.reserve3 = 0
                    //                            sportInfo1.sport_time = 0        // Start from 0 seconds
                    //                            sportInfo1.sport_distance = 0    // Start from 0 meters
                    //                            sportInfo1.sport_steps = 0       // Start from 0 steps
                    //                            sportInfo1.sport_kcal = 0
                    //                            sportInfo1.sport_speed = 0
                    //
                    //                        sportState1.infoModel = sportInfo1
                    //                        DispatchQueue.main.async {
                    //                            self.bleService.getGPSStart(sportState1)
                    //                        }
                    //                        break
                    //                        case "syncGPSData":
                    //                            print("syncGPSData command sent \(String(describing: data))")
                    //                            var sportStates = NJY_SportStateModel()
                    //
                    //                            let sportKcal = data?["sportKcal"] as? Int ?? 0
                    //                            let sportHr = data?["sportHr"] as? Int ?? 0
                    //                            let sportValid = data?["sportValid"] as? Int ?? 1
                    //                            let sportTime = data?["sportTime"] as? Int ?? 0
                    //                            let sportStride = data?["sportStride"] as? Int ?? 0
                    //                            let sportCadence = data?["sportCadence"] as? Int ?? 0
                    //                            let sportSteps = data?["sportSteps"] as? Int ?? 0
                    //                            let sportDistance = data?["sportDistance"] as? Int ?? 0
                    //                            let sportSpeed = data?["sportSpeed"] as? Int ?? 0
                    //                            let sportType = data?["sportType"] as? Int ?? 0
                    //
                    //                            sportStates.cmdId = cmd// Example command ID
                    //                            sportStates.aid = aid  // Example activity ID
                    //
                    //                            // 2. Configure required info model
                    //                            var sportInfo2 = NJY_SportInfoModel()
                    //                            sportInfo2.sport_state = 0   // Active state
                    //                            sportInfo2.sport_type = sportID       // Running
                    //                            sportInfo2.sport_gps = 0
                    //
                    //                            sportInfo2.sport_hr = sportHr
                    //                            sportInfo2.sport_valid = sportValid      // Valid data
                    //                            sportInfo2.sport_cadence = sportCadence
                    //                            sportInfo2.sport_stride = sportStride
                    //                            sportInfo2.reserve3 = 0
                    //                            sportInfo2.sport_time = sportTime        // Start from 0 seconds
                    //                            sportInfo2.sport_speed = sportSpeed
                    //                            sportInfo2.sport_distance = sportDistance    // Start from 0 meters
                    //                            sportInfo2.sport_steps = sportSteps       // Start from 0 steps
                    //                            sportInfo2.sport_kcal = sportKcal
                    //
                    //    //                        sportInfo.sport_steps = 10000       // Start from 0 steps
                    //    //                        sportInfo.sport_kcal = 10000
                    //    //                        sportInfo.sport_distance = 10000
                    //                            print("awal getGPSSynDataPlay sportState: aid:\(sportStates.aid) cmdId:\(sportStates.cmdId)")
                    //                            sportStates.infoModel = sportInfo2
                    //                            let asyncGPSCallbacka = NJYAsyncCallback<AnyObject>.create(
                    //                            self,
                    //                            success: { result in
                    //                                if let model = result as? NJY_SportStateModel {
                    //                                    print("Get getGPSSynDataPlay sportState: aid:\(model.aid) cmdId:\(model.cmdId)")
                    //                                    // Now you can access model's properties
                    //                                   // self.aid = model.aid
                    //                                    self.sportID = model.infoModel.sport_type
                    //                                } else {
                    //                                    print("Received unexpected result type: \(type(of: result))")
                    //                                }
                    //                            }, failure: { error in
                    //                                print("getGPSSynDataPlay Failure: \(error.localizedDescription)")
                    //                            })
                    //                        DispatchQueue.main.async {
                    //                            if sportTime % 1 == 0 {
                    //                                self.bleService.getGPSSynData(sportStates, callback: asyncGPSCallbacka)
                    //                            }
                    //                        }
                    //                        break
                    //
                    //                    default:
                    //                        print("command sent \(String(describing: data))")
                    //                        print("no Function")
                    //                        break
                    //                    }

                }
                break
            default:
                print("Invalid row selected \(String(describing: microwearDeviceControlValue))")
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
                                print("getDeviceVer Success: \(result)")
                                var item = [String: Any]()
                                item["status"] = "onDiscoveredServices"
                                item["mac"] = self.macAddressCache

                                print(item)

                                if let registerConnectStatuesCallBackSink = self.registerConnectStatuesCallBackSink {
                                    print("About to send data to Flutter")
                                    registerConnectStatuesCallBackSink(item)
                                    print("Data sent to Flutter")
                                } else {
                                    print("Callback sink is nil")
                                }

                                DispatchQueue.main.asyncAfter(deadline: .now() + 3) { [self] in
                                    print("Ini juga dijalankan setelah 3 detik")

                                    item["status"] = "onConnected"
                                    item["mac"] = self.macAddressCache

                                    print(item)

                                    if let registerConnectStatuesCallBackSink = self.registerConnectStatuesCallBackSink {
                                        print("About to send data to Flutter")
                                        registerConnectStatuesCallBackSink(item)
                                        print("Data sent to Flutter")
                                    } else {
                                        print("Callback sink is nil")
                                    }
                                }

                            }, failure: { error in
                                print("getDeviceVer Failure: \(error.localizedDescription)")
                                var item = [String: Any]()
                                item["status"] = "onConnectFail"
                                item["mac"] = self.macAddressCache

                                if let registerConnectStatuesCallBackSink = self.registerConnectStatuesCallBackSink {
                                    registerConnectStatuesCallBackSink(item)
                                }

                            })
                            bleService.getDeviceVer(asyncCallback)
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
        bleService.unbind();
        result(FlutterError(code: "DEVICE_NOT_FOUND", message: "Device with address \(bleAddress) not found", details: nil))
    }
}

class DataReceivedStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("DataReceivedStreamHandler.onListen called")
        plugin?.deviceDataReceivedSink = events
        print("DataReceivedsink set: \(plugin?.deviceDataReceivedSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("DataReceivedStreamHandler.onCancel called")
        return nil
    }
}

class DeviceConfigStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("DeviceConfigStreamHandler.onListen called")
        plugin?.deviceConfigSink = events
        print("DeviceConfig sink set: \(plugin?.deviceConfigSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("DeviceConfigStreamHandler.onCancel called")
        return nil
    }
}

class ConnectStatusStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("ConnectStatusStreamHandler.onListen called")
        plugin?.registerConnectStatuesCallBackSink = events
        print("Connect status sink set: \(plugin?.registerConnectStatuesCallBackSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("ConnectStatusStreamHandler.onCancel called")
        return nil
    }
}

class BatteryLevelStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("BatteryLevelStreamHandler.onListen called")
        plugin?.batteryLevelSink = events
        print("Battery level sink set: \(plugin?.batteryLevelSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("BatteryLevelStreamHandler.onCancel called")
        return nil
    }
}

class SyncHourStepStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncHourStepStreamHandler.onListen called")
        plugin?.syncHourStepSink = events
        print("Sync hour step sink set: \(plugin?.syncHourStepSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncHourStepStreamHandler.onCancel called")
        return nil
    }
}

class SyncWeekDaySportsStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncWeekDaySportsStreamHandler.onListen called")
        plugin?.syncWeekDaySportsSink = events
        print("Sync week day sports sink set: \(plugin?.syncWeekDaySportsSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncWeekDaySportsStreamHandler.onCancel called")
        return nil
    }
}

class DeviceDataReceivedStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("DeviceDataReceivedStreamHandler.onListen called")
        plugin?.deviceDataReceivedSink = events
        print("Device data received sink set: \(plugin?.deviceDataReceivedSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("DeviceDataReceivedStreamHandler.onCancel called")
        return nil
    }
}

class SyncSleepDataStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncSleepDataStreamHandler.onListen called")
        plugin?.syncSleepDataSink = events
        print("Sync sleep data sink set: \(plugin?.syncSleepDataSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncSleepDataStreamHandler.onCancel called")
        return nil
    }
}

class SyncSportRecordStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncSportRecordStreamHandler.onListen called")
        plugin?.syncSportRecordSink = events
        print("Sync sport record sink set: \(plugin?.syncSportRecordSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncSportRecordStreamHandler.onCancel called")
        return nil
    }
}

class GetAlarmClockInfoStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("GetAlarmClockInfoStreamHandler.onListen called")
        plugin?.getAlarmClockInfoSink = events
        print("Get alarm clock info sink set: \(plugin?.getAlarmClockInfoSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("GetAlarmClockInfoStreamHandler.onCancel called")
        return nil
    }
}

class SyncBloodPressureStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncBloodPressureStreamHandler.onListen called")
        plugin?.syncBloodPressureSink = events
        print("Sync blood pressure sink set: \(plugin?.syncBloodPressureSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncBloodPressureStreamHandler.onCancel called")
        return nil
    }
}

class SyncHeartDataStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncHeartDataStreamHandler.onListen called")
        plugin?.syncHeartDataSink = events
        print("Sync heart data sink set: \(plugin?.syncHeartDataSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncHeartDataStreamHandler.onCancel called")
        return nil
    }
}

class SyncOxDataStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncOxDataStreamHandler.onListen called")
        plugin?.syncOxDataSink = events
        print("Sync ox data sink set: \(plugin?.syncOxDataSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncOxDataStreamHandler.onCancel called")
        return nil
    }
}

class SyncHomeDataStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncHomeDataStreamHandler.onListen called")
        plugin?.syncHomeDataSink = events
        print("Sync home data sink set: \(plugin?.syncHomeDataSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncHomeDataStreamHandler.onCancel called")
        return nil
    }
}

class SyncRealTimeECGStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("SyncRealTimeECGStreamHandler.onListen called")
        plugin?.syncRealTimeECGSink = events
        print("Sync real time ECG sink set: \(plugin?.syncRealTimeECGSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("SyncRealTimeECGStreamHandler.onCancel called")
        return nil
    }
}

class GetDeviceFunStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("GetDeviceFunStreamHandler.onListen called")
        plugin?.getDeviceFunSink = events
        print("Get device fun sink set: \(plugin?.getDeviceFunSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("GetDeviceFunStreamHandler.onCancel called")
        return nil
    }
}

class GetDeviceConfig1StreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("GetDeviceConfig1StreamHandler.onListen called")
        plugin?.getDeviceConfig1Sink = events
        print("Get device config1 sink set: \(plugin?.getDeviceConfig1Sink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("GetDeviceConfig1StreamHandler.onCancel called")
        return nil
    }
}

class RegisterSomatosensoryGameCallbackStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("RegisterSomatosensoryGameCallbackStreamHandler.onListen called")
        plugin?.registerSomatosensoryGameCallbackSink = events
        print("Register somatosensory game callback sink set: \(plugin?.registerSomatosensoryGameCallbackSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("RegisterSomatosensoryGameCallbackStreamHandler.onCancel called")
        return nil
    }
}

class RegisterSingleHeartOxBloodCallbackStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("RegisterSingleHeartOxBloodCallbackStreamHandler.onListen called")
        plugin?.registerSingleHeartOxBloodCallbackSink = events
        print("Register single heart ox blood callback sink set: \(plugin?.registerSingleHeartOxBloodCallbackSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("RegisterSingleHeartOxBloodCallbackStreamHandler.onCancel called")
        return nil
    }
}

class RegisterMac3CallBackStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("RegisterMac3CallBackStreamHandler.onListen called")
        plugin?.registerMac3CallBackSink = events
        print("Register Mac3 callback sink set: \(plugin?.registerMac3CallBackSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("RegisterMac3CallBackStreamHandler.onCancel called")
        return nil
    }
}

class RegisterGPSCallBackStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("RegisterGPSCallBackStreamHandler.onListen called")
        plugin?.registerGPSCallBackSink = events
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("RegisterGPSCallBackStreamHandler.onCancel called")
        return nil
    }
}

class OnLoadingStreamHandler: NSObject, FlutterStreamHandler {
    weak var plugin: MicrowearSdkPlugin?

    init(plugin: MicrowearSdkPlugin) {
        self.plugin = plugin
    }

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        print("OnLoadingStreamHandler.onListen called")
        plugin?.onLoadingSink = events
        print("On loading sink set: \(plugin?.onLoadingSink != nil)")
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("OnLoadingStreamHandler.onCancel called")
        return nil
    }
}
