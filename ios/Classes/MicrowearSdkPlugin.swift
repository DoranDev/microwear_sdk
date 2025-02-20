import Flutter
import UIKit

public class MicrowearSdkPlugin: NSObject, FlutterPlugin, FlutterStreamHandler{
    
    init(_ channel: FlutterMethodChannel) {
        super.init()
    }

    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        switch arguments as? String {
        case MicrowearSdkPlugin.eventChannelNameDeviceDataReceived:
            deviceDataReceivedSink = events
        default:
            break
        }
        return nil
    }

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        return nil
    }

    static let eventChannelNameDeviceDataReceived = "deviceDataReceived"
    var deviceDataReceivedSink: FlutterEventSink?

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "microwear_sdk", binaryMessenger: registrar.messenger())
        let instance = MicrowearSdkPlugin(channel)
        registrar.addMethodCallDelegate(instance, channel: channel)
        let deviceDataReceivedChannel = FlutterEventChannel(name: eventChannelNameDeviceDataReceived, binaryMessenger: registrar.messenger())
        deviceDataReceivedChannel.setStreamHandler(instance)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let args = call.arguments as? [String: Any]
        switch call.method {
        case "connect":
            let macAddress = (args?["macAddress"] as? String)!
            //TODO
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
