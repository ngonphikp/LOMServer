package Handler;

import Base.BaseExtension;
import Base.BaseHandler;

import java.util.ArrayList;
import java.util.List;

import Controls.C_Account;
import Controls.C_Guild;
import Models.M_Account;
import Util.C_Util;
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
                HandleGetAccountGlobal(user, data);
                break;
            case CmdDefine.CMD.SEND_MESSAGE_GLOBAL:
                HandleSendMessageGlobal(user, data);
                break;
            case CmdDefine.CMD.GET_ACCOUNT_GUILD:
                HandleGetAccountGuild(user, data);
                break;
            case CmdDefine.CMD.SEND_MESSAGE_GUILD:
                HandleSendMessageGuild(user, data);
                break;
        }
    }

    private void HandleGetAccountGlobal(User user, ISFSObject data) {
        trace("____________________________ HandleGetAccountGlobal ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database và room global ===
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
        ArrayList<M_Account> allAcc = C_Account.getAll();
        for (int i = 0; i < allAcc.size(); i++) {
            for(int j = 0; j < ids.size(); j++){
                if(Integer.parseInt(ids.get(j)) == allAcc.get(i).id) {
                    isEx = true;
                    break;
                }
                isEx = false;
            }
            if(!isEx){
                lstAccOff.add(allAcc.get(i));
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

    private void HandleSendMessageGlobal(User user, ISFSObject data) {
        trace("____________________________ HandleSendMessageGlobal ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        String message = data.getUtfString(CmdDefine.ModuleCF.MESSAGE);

        // === Thao tác database và room global ===
        M_Account account = C_Account.get(id);

        // Lấy list user Online
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        List<User> lstUser = room.getUserList();

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleCF.ACCOUNT, account.parse());
        packet.putUtfString(CmdDefine.ModuleCF.MESSAGE, message);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.SEND_MESSAGE_GLOBAL);
        trace(packet.getDump());

        this.send(CmdDefine.Module.MODULE_CHAT_AND_FRIEND, packet, lstUser);
    }

    private void HandleSendMessageGuild(User user, ISFSObject data) {
        trace("____________________________ HandleSendMessageGuild ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        String message = data.getUtfString(CmdDefine.ModuleCF.MESSAGE);

        // === Thao tác database và room guild ===
        M_Account account = C_Account.get(id);
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id));

        // Lấy list user Online
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Guild + id_guild);
        List<User> lstUser = room.getUserList();

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleCF.ACCOUNT, account.parse());
        packet.putUtfString(CmdDefine.ModuleCF.MESSAGE, message);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.SEND_MESSAGE_GUILD);
        trace(packet.getDump());

        this.send(CmdDefine.Module.MODULE_CHAT_AND_FRIEND, packet, lstUser);
    }

    private void HandleGetAccountGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetAccountGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database và room guild ===
        ArrayList<M_Account> lstAccOnl = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        ids.add(user.getName());

        // Online
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(Integer.parseInt(user.getName())));
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Guild + id_guild);
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
        ArrayList<M_Account> guildAcc = C_Account.getByIdGuild(id_guild);
        for (int i = 0; i < guildAcc.size(); i++) {
            for(int j = 0; j < ids.size(); j++){
                if(Integer.parseInt(ids.get(j)) == guildAcc.get(i).id) {
                    isEx = true;
                    break;
                }
                isEx = false;
            }
            if(!isEx){
                lstAccOff.add(guildAcc.get(i));
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

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_ACCOUNT_GUILD);
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
