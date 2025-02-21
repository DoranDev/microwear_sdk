import Flutter
import UIKit

public class MicrowearSdkPlugin: NSObject, FlutterPlugin, FlutterStreamHandler{

    // Event sinks for various event channels
    private var deviceDataReceivedSink: FlutterEventSink?
    private var batteryLevelSink: FlutterEventSink?
    private var syncHourStepSink: FlutterEventSink?
    private var syncWeekDaySportsSink: FlutterEventSink?
    private var deviceConfigSink: FlutterEventSink?
    private var syncSleepDataSink: FlutterEventSink?
    private var syncSportRecordSink: FlutterEventSink?
    private var getAlarmClockInfoSink: FlutterEventSink?
    private var syncBloodPressureSink: FlutterEventSink?
    private var syncHeartDataSink: FlutterEventSink?
    private var syncOxDataSink: FlutterEventSink?
    private var syncHomeDataSink: FlutterEventSink?
    private var syncRealTimeECGSink: FlutterEventSink?
    private var getDeviceFunSink: FlutterEventSink?
    private var getDeviceConfig1Sink: FlutterEventSink?
    private var registerConnectStatuesCallBackSink: FlutterEventSink?
    private var registerSomatosensoryGameCallbackSink: FlutterEventSink?
    private var registerSingleHeartOxBloodCallbackSink: FlutterEventSink?
    private var registerMac3CallBackSink: FlutterEventSink?
    private var registerGPSCallBackSink: FlutterEventSink?
    private var onLoadingSink: FlutterEventSink?
    
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
    
    private static func setupEventChannel(name: String, registrar: FlutterPluginRegistrar, handler: MicrowearSdkPlugin ) {
          let channel = FlutterEventChannel(name: name, binaryMessenger: registrar.messenger())
          channel.setStreamHandler(handler)
      }
      

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "microwear_sdk", binaryMessenger: registrar.messenger())
        let instance = MicrowearSdkPlugin(channel)
        registrar.addMethodCallDelegate(instance, channel: channel)
        let deviceDataReceivedChannel = FlutterEventChannel(name: deviceDataReceivedChannelName, binaryMessenger: registrar.messenger())
        deviceDataReceivedChannel.setStreamHandler(instance)
        
        
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let args = call.arguments as? [String: Any]
        
        switch call.method {
        case "connect":
            let macAddress = args?["macAddress"] as? String ?? ""
            // TODO: Implement connect functionality
            result(nil)
            
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
            // TODO: Implement disconnect functionality
            result(nil)
            
        case "sendRequest":
            guard let microwearDeviceControlValue = args?["microwearDeviceControl"] as? Int,
                  let data = args?["data"] as? [String: Any] else {
                result(FlutterError(code: "INVALID_ARGUMENTS",
                                   message: "Invalid arguments for sendRequest",
                                   details: nil))
                return
            }
            
            // TODO: Implement sendRequest functionality based on the microwearDeviceControlValue
    
            
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
