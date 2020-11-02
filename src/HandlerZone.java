import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class HandlerZone extends BaseHandler {

    public HandlerZone(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId){

        }
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) throws SFSException {
        switch (type) {
            case USER_JOIN_ZONE:
                OnUserJoinZone(event);
                break;
            case USER_DISCONNECT:
                OnUserDisconnect(event);
            case USER_LOGIN:
                OnUserLogin(event);
                break;
            case USER_LOGOUT:
                OnUserLogout(event);
                break;
        }
    }

    private void OnUserLogin(ISFSEvent event) {
        trace("____________________________ OnUserLogin ____________________________");
        ISFSObject dataRec = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);

        int cmdId = dataRec.getInt(CmdDefine.CMD_ID);
        switch (cmdId){
            case CmdDefine.CMD.LOGIN:
                HandleLogin(event);
                break;
            case CmdDefine.CMD.REGISTER:
                HandleRegister(event);
                break;
        }
    }

    private void HandleRegister(ISFSEvent event) {

    }

    private void HandleLogin(ISFSEvent event) {

    }

    private void OnUserLogout(ISFSEvent event) {
        trace("____________________________ OnUserLogout ____________________________");
    }

    public void OnUserJoinZone(ISFSEvent event){
        trace("____________________________ OnUserJoinZone ____________________________");
    }

    public void OnUserDisconnect(ISFSEvent event){
        trace("____________________________ OnUserDisconnect ____________________________");
    }

    @Override
    protected void initHandlerClientRequest() {

    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addEventHandler(SFSEventType.USER_JOIN_ZONE, this);
        this.extension.addEventHandler(SFSEventType.USER_DISCONNECT, this);

        this.extension.addEventHandler(SFSEventType.USER_LOGIN, this);
        this.extension.addEventHandler(SFSEventType.USER_LOGOUT, this);
    }
}
