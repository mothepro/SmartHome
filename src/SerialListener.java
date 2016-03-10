/**
 * Event callable interface
 *
 * @author Mo
 */
public interface SerialListener {
    //  new data is found from port
    void onData(String data);

}
