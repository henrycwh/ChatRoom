package Networks;

public interface ReceiveEventHandler {
    void onReceiveData(RemoteClient object, Byte[] data);
}
