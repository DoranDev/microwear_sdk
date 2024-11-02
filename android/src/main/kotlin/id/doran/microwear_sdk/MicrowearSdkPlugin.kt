package id.doran.microwear_sdk

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.EventChannel

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

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    Log.d("MicrowearSdkPlugin","onAttachedToEngine")
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "microwear_sdk")
    channel.setMethodCallHandler(this)
    mContext = flutterPluginBinding.applicationContext

    deviceDataReceivedChannel = EventChannel(flutterPluginBinding.binaryMessenger, "deviceDataReceived")
    deviceDataReceivedChannel!!.setStreamHandler(deviceDataReceivedHandler)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    Log.d("MicrowearSdkPlugin","onDetachedFromActivity")
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.d("MicrowearSdkPlugin","onAttachedToActivity")
    mActivity = binding.activity
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
