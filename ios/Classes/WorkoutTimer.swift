

class WorkoutTimer {
    private var timer: Timer?
    private let interval: TimeInterval
    private var tickCount: Int = 0
    private var startTime: Date?
    private var pauseTime: Date?
    private var totalPausedTime: TimeInterval = 0
    
    // Callback handlers
    var onStart: ((_ initialTick: Int) -> Void) = { _ in }
    var onResume: ((_ resumedTick: Int) -> Void) = { _ in }
    var onTick: ((_ currentTick: Int, _ elapsedTime: TimeInterval) -> Void) = { _, _ in }
    var onPause: ((_ totalTicks: Int, _ elapsedTime: TimeInterval) -> Void) = { _, _ in }
    var onStop: ((_ finalTickCount: Int, _ totalTime: TimeInterval) -> Void) = { _, _ in }
    
    init(interval: TimeInterval) {
        self.interval = interval
    }
    
    // Start atau restart timer
    func start() {
        stop()
        resetState()
        startTime = Date()
        onStart(0)
        startTimer()
    }
    
    // Pause timer
    func pause() {
        guard timer != nil, pauseTime == nil else { return }
        
        pauseTime = Date()
        timer?.invalidate()
        
        let elapsed = Date().timeIntervalSince(startTime!) - totalPausedTime
        onPause(tickCount, elapsed)
    }
    
    // Resume timer dari pause
    func resume() {
        guard let pauseTime = pauseTime else { return }
        
        totalPausedTime += Date().timeIntervalSince(pauseTime)
        self.pauseTime = nil
        onResume(tickCount)
        startTimer()
    }
    
    // Stop timer sepenuhnya
    func stop() {
        guard startTime != nil else { return }
        
        let totalActiveTime = Date().timeIntervalSince(startTime!) - totalPausedTime
        onStop(tickCount, totalActiveTime)
        
        timer?.invalidate()
        resetState()
    }
    
    private func startTimer() {
        timer = Timer.scheduledTimer(withTimeInterval: interval, repeats: true) { [weak self] _ in
            self?.handleTick()
        }
    }
    
    private func handleTick() {
        tickCount += 1
        let elapsed = Date().timeIntervalSince(startTime!) - totalPausedTime
        onTick(tickCount, elapsed)
    }
    
    private func resetState() {
        timer = nil
        startTime = nil
        pauseTime = nil
        tickCount = 0
        totalPausedTime = 0
    }
    
    deinit {
        stop()
    }
}
