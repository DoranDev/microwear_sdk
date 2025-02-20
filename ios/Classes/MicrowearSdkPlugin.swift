import Flutter
import UIKit

public class MicrowearSdkPlugin: NSObject, FlutterPlugin, FlutterStreamHandler{
    
    init(_ channel: FlutterMethodChannel) {
        super.init()
    }

    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        switch arguments as? String {
        case MicrowearSdkPlugin.eventChannelNameOnReadPower:
            onReadPowerSink = events
        default:
            break
        }
        return nil
    }

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        return nil
    }

    static let eventChannelNameOnReadPower = "onReadPower"
    var onReadPowerSink: FlutterEventSink?

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "microwear_sdk", binaryMessenger: registrar.messenger())
        let instance = MicrowearSdkPlugin(channel)
        registrar.addMethodCallDelegate(instance, channel: channel)
        let onReadPowerChannel = FlutterEventChannel(name: eventChannelNameOnReadPower, binaryMessenger: registrar.messenger())
        onReadPowerChannel.setStreamHandler(instance)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "getPlatformVersion":
            result("iOS " + UIDevice.current.systemVersion)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
