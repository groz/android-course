package tm.hackweek.arithmetictrainer;

public final class Global {
    public static Global Instance = new Global();

    private WorkoutConfig mWorkoutConfig;

    public WorkoutConfig getWorkoutConfig() {
        return mWorkoutConfig;
    }

    public void setWorkoutConfig(WorkoutConfig config) {
        mWorkoutConfig = config;
    }

    private Global() {
        // TODO: load config from persistent storage
        mWorkoutConfig = WorkoutConfig.EASY;
    }
}
