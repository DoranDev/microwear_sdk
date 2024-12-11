/// Enum representing GPS commands with descriptive comments and values
enum MicrowearGpsCommand {
  /// Request GPS location permission
  requestPermission(0),

  /// Request to start countdown
  startCountdown(1),

  /// Request to start GPS activity
  startActivity(2),

  /// Sync GPS activity data
  syncActivity(3),

  /// Pause GPS activity
  pauseActivity(4),

  /// Resume GPS activity
  resumeActivity(5),

  /// End GPS activity
  endActivity(6),

  /// App is busy, unable to respond at the moment
  appBusy(7);

  /// The integer value associated with the command
  final int value;

  /// Constructor to associate the value with the enum member
  const MicrowearGpsCommand(this.value);
}
