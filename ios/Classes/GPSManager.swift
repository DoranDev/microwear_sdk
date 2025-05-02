import Flutter
import Foundation

// MARK: - Notification Constants
//let KBLEGPSSPORT_NOTIF = "NotificationGpsSport"                      // GPS sport notification (apakah mulai olahraga)
let KBLEGPSSPORT_NOTIF = "收到GPS运动指令"
let KBLEGPSSPORTSTOP_NOTIF = "NotificationGpsSportStop"              // GPS sport pause notification
let KBLEGPSSPORTCONTINUETO_NOTIF = "NotificationGpsSportcontinueTo"  // GPS sport continue notification
let KBLEGPSSPORTEnd_NOTIF = "NotificationGpsSportEnd"                // GPS sport end notification
let kBLENEWLIST = "NotificationNewList"                              // BLE request new list notification

// MARK: - GPS Command Constants
let GPS_CMD_GPS = 0
let GPS_CMD_APP_BUSY = 1

// MARK: - GPS Callback Protocol
protocol GPSCallbackProtocol: AnyObject {
    func onGPSPermission()
    func onGPSCountdown(sportId: Int)
    func onGPSStart(sportId: Int)
    func onGPSSync(gpsSportEntity: GPSSportEntity?)
    func onGPSPause(sportId: Int)
    func onGPSContinue(sportId: Int)
    func onGPSEnd(sportId: Int)
}

// MARK: - GPS Sport Entity Model
struct GPSSportEntity: Codable {
    let sportId: Int
    let duration: Int
    let distance: Double
    let calories: Int
    let speed: Double
    let steps: Int
    let pace: Double
    // Tambahkan properti lain sesuai kebutuhan
}

// MARK: - Sport State Model (Converted from NJY_SportStateModel)
class SportStateModel {
    var cmdId: Int = 0
    var aid: Int = 0
}

// MARK: - GPS Manager (Singleton)
class GPSManager {
    static let shared = GPSManager()
    private var callback: GPSCallbackProtocol?
    
    private init() {
        setupNotificationObservers()
    }
    
    func registerCallback(_ callback: GPSCallbackProtocol) {
        self.callback = callback
    }
    
    private func setupNotificationObservers() {
        // Register for all relevant notifications
        NotificationCenter.default.addObserver(self,
                                              selector: #selector(handleGPSStart),
                                              name: NSNotification.Name(KBLEGPSSPORT_NOTIF),
                                              object: nil)
        
        NotificationCenter.default.addObserver(self,
                                              selector: #selector(handleGPSPause),
                                              name: NSNotification.Name(KBLEGPSSPORTSTOP_NOTIF),
                                              object: nil)
        
        NotificationCenter.default.addObserver(self,
                                              selector: #selector(handleGPSContinue),
                                              name: NSNotification.Name(KBLEGPSSPORTCONTINUETO_NOTIF),
                                              object: nil)
        
        NotificationCenter.default.addObserver(self,
                                              selector: #selector(handleGPSEnd),
                                              name: NSNotification.Name(KBLEGPSSPORTEnd_NOTIF),
                                              object: nil)
    }
    
    @objc private func handleGPSStart(_ notification: Notification) {
        if let sportId = notification.userInfo?["sportId"] as? Int {
            callback?.onGPSStart(sportId: sportId)
        }
    }
    
    @objc private func handleGPSPause(_ notification: Notification) {
        if let sportId = notification.userInfo?["sportId"] as? Int {
            callback?.onGPSPause(sportId: sportId)
        }
    }
    
    @objc private func handleGPSContinue(_ notification: Notification) {
        if let sportId = notification.userInfo?["sportId"] as? Int {
            callback?.onGPSContinue(sportId: sportId)
        }
    }
    
    @objc private func handleGPSEnd(_ notification: Notification) {
        if let sportId = notification.userInfo?["sportId"] as? Int {
            callback?.onGPSEnd(sportId: sportId)
        }
    }
    
    
    private func sendLocalNotification(message: String) {
        let content = UNMutableNotificationContent()
        content.title = "GPS Sport"
        content.body = message
        content.sound = .default
        
        let request = UNNotificationRequest(identifier: UUID().uuidString,
                                           content: content,
                                           trigger: UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: false))
        
        UNUserNotificationCenter.current().add(request)
    }
}


// MARK: - GPS Callback Handler
class GPSCallbackHandler: NSObject, GPSCallbackProtocol {
    private var callbackSink: FlutterEventSink?
    
    func setCallbackSink(_ sink: @escaping FlutterEventSink) {
        self.callbackSink = sink
    }
    
    func onGPSPermission() {
        do {
            let resultMap: [String: Any] = ["status": "onGPSPermission"]
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
    
    func onGPSCountdown(sportId: Int) {
        do {
            let resultMap: [String: Any] = [
                "status": "onGPSCountdown",
                "sportId": sportId
            ]
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
    
    func onGPSStart(sportId: Int) {
        do {
            let resultMap: [String: Any] = [
                "status": "onGPSStart",
                "sportId": sportId
            ]
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
    
    func onGPSSync(gpsSportEntity: GPSSportEntity?) {
        do {
            var resultMap: [String: Any] = ["status": "onGPSSync"]
            
            if let entity = gpsSportEntity {
                let jsonData = try JSONEncoder().encode(entity)
                if let jsonDict = try JSONSerialization.jsonObject(with: jsonData) as? [String: Any] {
                    resultMap["gpsSportEntity"] = jsonDict
                }
            } else {
                resultMap["gpsSportEntity"] = nil
            }
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
    
    func onGPSPause(sportId: Int) {
        do {
            let resultMap: [String: Any] = [
                "status": "onGPSPause",
                "sportId": sportId
            ]
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
    
    func onGPSContinue(sportId: Int) {
        do {
            let resultMap: [String: Any] = [
                "status": "onGPSContinue",
                "sportId": sportId
            ]
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
    
    func onGPSEnd(sportId: Int) {
        do {
            let resultMap: [String: Any] = [
                "status": "onGPSEnd",
                "sportId": sportId
            ]
            print(resultMap)
            callbackSink?(resultMap)
        } catch {
            callbackSink?(FlutterError(code: "SERIALIZATION_ERROR",
                                      message: "Failed to serialize result",
                                      details: error.localizedDescription))
        }
    }
}

