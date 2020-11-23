package Handler;

import Base.BaseExtension;
import Base.BaseHandler;

import java.util.ArrayList;
import java.util.List;

import Controls.C_Account;
import Models.M_Account;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class HandlerCF extends BaseHandler {
    public HandlerCF(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.GET_ACCOUNT_GLOBAL:
                HandleGetUserGlobal(user, data);
                break;
        }
    }

    private void HandleGetUserGlobal(User user, ISFSObject data) {
        trace("____________________________ HandleGetUserGlobal ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database ===
        ArrayList<M_Account> lstAccOnl = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        ids.add(user.getName());

        // Online
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        List<User> lstUser = room.getUserList();
        for (int i = 0; i < lstUser.size(); i++) {
            if(!lstUser.get(i).getName().equals(user.getName())){
                lstAccOnl.add(C_Account.get(Integer.parseInt(lstUser.get(i).getName())));
                ids.add(lstUser.get(i).getName());
            }
        }

        // Offline
        ArrayList<M_Account> lstAccOff = new ArrayList<>();
        boolean isEx = false;
        ArrayList<M_Account> fakeAcc = C_Account.getAll();
        for (int i = 0; i < fakeAcc.size(); i++) {
            for(int j = 0; j < ids.size(); j++){
                if(Integer.parseInt(ids.get(j)) == fakeAcc.get(i).id) {
                    isEx = true;
                    break;
                }
                isEx = false;
            }
            if(!isEx){
                lstAccOff.add(fakeAcc.get(i));
                isEx = false;
            }
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray accOnls = new SFSArray();
        for(int i = 0; i < lstAccOnl.size(); i++){
            accOnls.addSFSObject(lstAccOnl.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleCF.ACCOUNTS_ONLINE, accOnls);

        ISFSArray accOffs = new SFSArray();
        for(int i = 0; i < lstAccOff.size(); i++){
            accOffs.addSFSObject(lstAccOff.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleCF.ACCOUNTS_OFFLINE, accOffs);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_ACCOUNT_GLOBAL);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_CHAT_AND_FRIEND, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_CHAT_AND_FRIEND, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_CHAT_AND_FRIEND, this);
    }
}
