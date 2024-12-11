class MicrowearWeatherData {
  int week;
  int tempData;
  int weatherType;
  String pressure;
  String ultLevel;
  int humidity;
  int windDirDay;
  String windScaleDay;
  int vis;
  int precip;

  MicrowearWeatherData({
    required this.week,
    required this.tempData,
    required this.weatherType,
    required this.pressure,
    required this.ultLevel,
    required this.humidity,
    required this.windDirDay,
    required this.windScaleDay,
    required this.vis,
    required this.precip,
  });

  Map<String, dynamic> toMap() {
    return {
      "week": week,
      "tempData": tempData,
      "weatherType": weatherType,
      "pressure": pressure,
      "ultLevel": ultLevel,
      "humidity": humidity,
      "windDirDay": windDirDay,
      "windScaleDay": windScaleDay,
      "vis": vis,
      "precip": precip,
    };
  }
}
