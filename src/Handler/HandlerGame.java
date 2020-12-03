package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_Account;
import Controls.C_Character;
import Models.M_Character;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.List;


public class HandlerGame extends BaseHandler {
    public List<User> listUser = new ArrayList<>();

    public HandlerGame(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_GAME);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.START_PVP:
                break;
        }
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {
            case USER_JOIN_ROOM:
                HandleJoinRoomGame(type, event);
                break;
            case USER_DISCONNECT:
                HandleDisconnectGame(type, event);
                break;
        }
    }

    private void HandleDisconnectGame(SFSEventType type, ISFSEvent event) {
        trace("____________________________ HandleDisconnectGame ____________________________");

        extension.getParentRoom().removeUser((User) event.getParameter(SFSEventParam.USER));
    }

    private void HandleJoinRoomGame(SFSEventType type, ISFSEvent event) {
        trace("____________________________ HandleJoinRoomGame ____________________________");

        User user = (User) event.getParameter(SFSEventParam.USER);
        listUser.add(user);

        trace(user.getName() + " => " + extension.getParentRoom().getName());

        if(extension.getParentRoom().isFull()){
            // === Thao tác database và Gửi dữ liệu xuống ===
            ISFSObject packet = new SFSObject();
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

            ISFSArray list = new SFSArray();
            for (int i = 0; i < listUser.size(); i++) {
                ISFSObject object = new SFSObject();
                int id_ac = Integer.parseInt(listUser.get(i).getName());
                object.putSFSObject(CmdDefine.ModuleGame.ACCOUNTS, C_Account.get(id_ac).parse());

                ArrayList<M_Character> lstCharacter = C_Character.gets(id_ac);
                ISFSArray arr = new SFSArray();
                for(int j = 0; j < lstCharacter.size(); j++){
                    arr.addSFSObject(lstCharacter.get(j).parse());
                }
                object.putSFSArray(CmdDefine.ModuleGame.CHARACTERS, arr);

                list.addSFSObject(object);
            }
            packet.putSFSArray(CmdDefine.ModuleGame.LIST, list);

            packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.JOIN_ROOM_GAME);
            trace(packet.getDump());
            this.send(this.module, packet, extension.getParentRoom().getUserList());
        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_GAME, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_GAME, this);

        this.extension.addEventHandler(SFSEventType.USER_JOIN_ROOM, this);
        this.extension.addEventHandler(SFSEventType.USER_DISCONNECT, this);
    }
}
