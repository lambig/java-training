package broker;

interface RunnerBroker {

    public RunnerBroker subscribe(String eventName, Runnable callback);

    public RunnerBroker publish(String eventName);

}