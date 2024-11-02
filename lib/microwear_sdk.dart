import 'package:flutter/services.dart';

class MicrowearSdk {
  final methodChannel = const MethodChannel('microwear_sdk');

  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
