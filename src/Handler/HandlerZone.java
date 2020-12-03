package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Base.RoomManage;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class HandlerZone extends BaseHandler {

    public HandlerZone(BaseExtension extension) {
        super(extension, "");
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId){

        }
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {
            case USER_JOIN_ZONE:
                OnUserJoinZone(event);
                break;
            case USER_DISCONNECT:
                OnUserDisconnect(event);
                break;
            case SERVER_READY:
                OnServerReady(event);
                break;
        }
    }

    public void OnUserJoinZone(ISFSEvent event){
        trace("____________________________ OnUserJoinZone ____________________________");
    }

    public void OnUserDisconnect(ISFSEvent event){
        trace("____________________________ OnUserDisconnect ____________________________");
    }

    public void OnServerReady(ISFSEvent event){
        trace("____________________________ OnServerReady ____________________________");
        RoomManage manage = new RoomManage(this.getParentExtension());
        manage.initRoom("HandlerExtension", CmdDefine.Room.Global, CmdDefine.Room.Global, 10000);
    }

    @Override
    protected void initHandlerClientRequest() {

    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addEventHandler(SFSEventType.USER_JOIN_ZONE, this);
        this.extension.addEventHandler(SFSEventType.USER_DISCONNECT, this);
        this.extension.addEventHandler(SFSEventType.SERVER_READY, this);
    }
}
