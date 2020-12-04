package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Base.RoomManage;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HandlerGame extends BaseHandler {
    public List<User> listUser = new ArrayList<>();
    public Map<Integer, List<M_Character>> mapArrange = new HashMap<>();

    public HandlerGame(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_GAME);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.OUT_ROOM_GAME:
                HandleOutRoomGame(user, data);
                break;
            case CmdDefine.CMD.UN_ACTIVE_CHAR:
                HandleUnActive(user, data);
                break;
            case CmdDefine.CMD.ACTIVE_CHAR:
                HandleActive(user, data);
                break;
            case CmdDefine.CMD.CHANGE_CHAR:
                HandleChange(user, data);
                break;
            case CmdDefine.CMD.LOCK_ARRANGE:
                HandleLock(user, data);
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

    private void HandleLock(User user, ISFSObject data) {
        trace("____________________________ HandleLock ____________________________");
        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Gửi dữ liệu xuống ===
        data.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        data.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        this.send(this.module, data, extension.getParentRoom().getUserList());

        // === Thao tác room
        List<M_Character> lstCharacter = new ArrayList<>();
        ISFSArray objs = data.getSFSArray(CmdDefine.ModuleAccount.CHARACTERS);
        for(int i = 0; i < objs.size(); i++){
            lstCharacter.add(new M_Character(objs.getSFSObject(i)));
        }
        mapArrange.put(id_ac, lstCharacter);

        if(mapArrange.size() > 1){
            StartGame();
        }
    }

    private void StartGame(){
        trace("____________________________ StartGame ____________________________");
        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.START_GAME);
        trace(packet.getDump());
        this.send(this.module, packet, extension.getParentRoom().getUserList());
    }

    private void HandleChange(User user, ISFSObject data) {
        trace("____________________________ HandleChange ____________________________");
        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database

        // === Gửi dữ liệu xuống ===
        data.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        data.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        this.send(this.module, data, extension.getParentRoom().getUserList());
    }

    private void HandleActive(User user, ISFSObject data) {
        trace("____________________________ HandleActive ____________________________");
        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database

        // === Gửi dữ liệu xuống ===
        data.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        data.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        this.send(this.module, data, extension.getParentRoom().getUserList());
    }

    private void HandleUnActive(User user, ISFSObject data) {
        trace("____________________________ HandleUnActive ____________________________");
        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database

        // === Gửi dữ liệu xuống ===
        data.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        data.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        this.send(this.module, data, extension.getParentRoom().getUserList());
    }

    private void HandleOutRoomGame(User user, ISFSObject data) {
        trace("____________________________ HandleOutRoomGame ____________________________");
        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database

        // === Gửi dữ liệu xuống ===
        data.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        data.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        this.send(this.module, data, extension.getParentRoom().getUserList());

        // === Thao tác với room
        extension.getParentRoom().removeUser(user);
        if(extension.getParentRoom().isEmpty()){
            RoomManage manage = new RoomManage(this.getParentExtension());
            manage.removeRoom(extension.getParentRoom());
        }
    }

    private void HandleDisconnectGame(SFSEventType type, ISFSEvent event) {
        trace("____________________________ HandleDisconnectGame ____________________________");

        extension.getParentRoom().removeUser((User) event.getParameter(SFSEventParam.USER));

        if(extension.getParentRoom().isEmpty()){
            RoomManage manage = new RoomManage(this.getParentExtension());
            manage.removeRoom(extension.getParentRoom());
        }
    }

    private void HandleJoinRoomGame(SFSEventType type, ISFSEvent event) {
        trace("____________________________ HandleJoinRoomGame ____________________________");

        User user = (User) event.getParameter(SFSEventParam.USER);
        trace(user.getName() + " => " + extension.getParentRoom().getName());

        listUser.add(user);
        if(extension.getParentRoom().isFull()){
            call(user);
        }
    }

    private synchronized void call(User user) {
        if (listUser.size() > 1){
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
