enum MicrowearDeviceControl {
  /// Find watch
  alertFindWatch(0),

  /// Message push notification
  alertMsg(11),

  /// ECG measurement
  ecgHr(29),

  /// Heart rate measurement
  mesHr(30),

  /// Blood pressure measurement
  mesBp(31),

  /// Blood oxygen measurement
  mesBo2(32),

  /// Shutdown
  shutdown(33),

  /// Reboot
  reboot(34),

  /// Factory reset
  reset(35),

  /// Low power shutdown
  lowPowerShutdown(36),

  /// Product ID
  productId(60),

  /// TP version
  tpVer(61),

  /// Firmware version
  firmwareVer(62),

  /// UI version
  uiVer(63),

  /// GSENSOR coordinates
  gSensorXyz(64),

  /// User information
  userInfo(65),

  /// Unit system
  unitSystem(66),

  /// Date and time
  dateTime(67),

  /// Time format
  timeMode(68),

  /// Temperature format
  tempUnit(69),

  /// Language
  language(70),

  /// Watch face ID
  timeStyle(71),

  /// Battery level
  bat(72),

  /// Target steps
  targetStep(73),

  /// Hourly steps
  hourStep(74),

  /// Historical 7-day exercise data (24-hour hourly steps, full-day exercise data)
  historySportData(75),

  /// Message notifications
  notifications(76),

  /// Screen on duration
  displayTime(77),

  /// Band configuration
  bandConfig(78),

  /// Sleep data
  sleepData(79),

  /// Exercise records
  sportRecord(80),

  /// Exercise data
  sportData(81),

  /// Weather forecast
  weatherForecast(82),

  /// Real-time weather
  realTimeWeather(83),

  /// Raise wrist to wake screen
  raiseWrist(84),

  /// Do not disturb mode
  disturb(85),

  /// Sedentary reminder
  longSit(86),

  /// Drink water reminder
  drinkWater(87),

  /// Hand washing reminder
  washHand(88),

  /// Schedule reminder
  schedule(89),

  /// Alarm
  alarm(90),

  /// Body temperature
  temp(91),

  /// Heart rate
  hr(92),

  /// Blood pressure
  bp(93),

  /// Blood oxygen
  bo(94),

  /// All-day heart rate
  hrDay(95),

  /// All-day blood oxygen
  boDay(96),

  /// All-day temperature
  tempDay(97),

  /// All-day measurement switch
  allDayFlag(98),

  /// Test settings hr/bp/bo2
  hrBpBo2(99),

  /// Take photo
  takePhoto(100),

  /// Music control
  ctrlMusic(101),

  /// Band configuration
  bandConfig1(102),

  /// Set phone system type
  phoneSystemType(103),

  /// APP request sync basic data
  appRequestSync(105),

  /// Find phone
  findPhone(106),

  /// All-day blood pressure
  bpDay(107),

  /// Business card/friend code
  addFriend(108),

  /// Payment code
  receiptCode(109),

  /// Women's health
  womenHealth(110),

  /// Phone answer/hang up status
  androidPhoneCtrl(111),

  /// Unbind
  unbind(112),

  /// ECG command
  hrEcg(120),

  /// Sync firmware supported features + device screen information
  deviceFun(121),

  /// Sync 3.0 device MAC + name
  watchCallInfo(122),

  /// GPS Sport
  gpsSport(124),

  /// Motion game
  motionGame(126),

  ///stock
  stock(138),

  /// OTA start command
  otaStart(200),

  /// OTA data command
  otaData(201),

  /// OTA end
  otaEnd(202),

  /// OTA Big start command
  otaBigStart(206),

  /// OTA Big data command
  otaBigData(207),

  /// OTA Big end
  otaBigEnd(208);

  final int value;
  const MicrowearDeviceControl(this.value);
}
