package Handler;

import Base.BaseExtension;
import Base.BaseHandler;

import java.util.ArrayList;
import java.util.List;

import Controls.C_Account;
import Controls.C_Character;
import Controls.C_Guild;
import Models.M_Account;
import Models.M_Character;
import Models.M_Guild;
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
        super(extension, CmdDefine.Module.MODULE_CHAT_AND_FRIEND);
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
            case CmdDefine.CMD.GET_DETAILS:
                HandlerGetDetails(user, data);
                break;
            case CmdDefine.CMD.MAKE_FRIEND:
                HandleMakeFriend(user, data);
                break;
            case CmdDefine.CMD.REMOVE_FRIEND:
                HandleRemoveFriend(user, data);
                break;
            case CmdDefine.CMD.GET_ACCOUNT_FRIEND:
                HandleGetAccountFriend(user, data);
                break;
            case CmdDefine.CMD.FIND_ACCOUNT_GLOBAL:
                HandleFindAccountGlobal(user, data);
                break;
            case CmdDefine.CMD.SEND_MESSAGE_PRIVATE:
                HandleSendMessagePrivate(user, data);
                break;
        }
    }

    private void HandleGetAccountGlobal(User user, ISFSObject data) {
        trace("____________________________ HandleGetAccountGlobal ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database và room global ===
        ArrayList<M_Account> lstAcc = C_Account.getAll();
        ISFSArray idOnls = new SFSArray();

        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        List<User> lstUser = room.getUserList();
        for (int i = 0; i < lstUser.size(); i++) idOnls.addInt(Integer.parseInt(lstUser.get(i).getName()));

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray accs = new SFSArray();
        for(int i = 0; i < lstAcc.size(); i++){
            accs.addSFSObject(lstAcc.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleCF.ACCOUNTS, accs);

        packet.putSFSArray(CmdDefine.ModuleCF.ID_ONLINES, idOnls);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_ACCOUNT_GLOBAL);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetAccountGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetAccountGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database và room guild ===
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(Integer.parseInt(user.getName())));

        ArrayList<M_Account> lstAcc = C_Account.getByIdGuild(id_guild);
        ISFSArray idOnls = new SFSArray();

        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Guild + id_guild);
        List<User> lstUser = room.getUserList();
        for (int i = 0; i < lstUser.size(); i++) idOnls.addInt(Integer.parseInt(lstUser.get(i).getName()));

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray accs = new SFSArray();
        for(int i = 0; i < lstAcc.size(); i++){
            accs.addSFSObject(lstAcc.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleCF.ACCOUNTS, accs);

        packet.putSFSArray(CmdDefine.ModuleCF.ID_ONLINES, idOnls);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_ACCOUNT_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetAccountFriend(User user, ISFSObject data) {
        trace("____________________________ HandleGetAccountFriend ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database và room global ===
        ArrayList<M_Account> lstAcc = C_Account.getFriends(id);
        ISFSArray idOnls = new SFSArray();

        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        List<User> lstUser = room.getUserList();
        for (int i = 0; i < lstUser.size(); i++) idOnls.addInt(Integer.parseInt(lstUser.get(i).getName()));

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray accs = new SFSArray();
        for(int i = 0; i < lstAcc.size(); i++){
            accs.addSFSObject(lstAcc.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleCF.ACCOUNTS, accs);

        packet.putSFSArray(CmdDefine.ModuleCF.ID_ONLINES, idOnls);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_ACCOUNT_FRIEND);
        trace(packet.getDump());
        this.send(this.module, packet, user);
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

        this.send(this.module, packet, lstUser);
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

        this.send(this.module, packet, lstUser);
    }

    private void HandlerGetDetails(User user, ISFSObject data) {
        trace("____________________________ HandlerGetDetails ____________________________");

        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_get = data.getInt(CmdDefine.ModuleAccount.ID);

        // === Thao tác database ===
        // Lấy thông tin tài khoản
        M_Account account = C_Account.get(id_get);

        // Lấy danh sách nhân vật => active
        ArrayList<M_Character> lstCharacter = C_Character.gets(id_get);
        ArrayList<M_Character> characters = new ArrayList<>();
        for (int i = 0; i < lstCharacter.size(); i++) {
            if(lstCharacter.get(i).idx != -1){
                characters.add(lstCharacter.get(i));
            }
        }

        // Lấy thông tin guild => id, name
        M_Guild guild = null;
        String keyGuild = C_Guild.getKey(id_get);
        if(keyGuild != null){
            int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, keyGuild);
            guild = C_Guild.get(id_guild, false);
        }

        // Kiểm tra mối quan hệ bạn bè
        boolean isFriend = C_Account.checkFriend(id_ac, id_get);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleAccount.ACCOUNT, account.parse());

        ISFSArray array = new SFSArray();
        for(int i = 0; i < lstCharacter.size(); i++){
            array.addSFSObject(lstCharacter.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleAccount.CHARACTERS, array);

        if(guild != null){
            packet.putInt(CmdDefine.ModuleGuild.ID, guild.id);
            packet.putUtfString(CmdDefine.ModuleGuild.NAME, guild.name);
        }

        packet.putBool(CmdDefine.ModuleCF.IS_FRIEND, isFriend);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_DETAILS);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleMakeFriend(User user, ISFSObject data) {
        trace("____________________________ HandleMakeFriend ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_make = data.getInt(CmdDefine.ModuleAccount.ID);

        // === Thao tác database ===
        C_Account.insertFriends(id, id_make);
        C_Account.insertFriends(id_make, id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.MAKE_FRIEND);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleRemoveFriend(User user, ISFSObject data) {
        trace("____________________________ HandleRemoveFriend ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_make = data.getInt(CmdDefine.ModuleAccount.ID);

        // === Thao tác database ===
        C_Account.removeFriends(id, id_make);
        C_Account.removeFriends(id_make, id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.REMOVE_FRIEND);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleFindAccountGlobal(User user, ISFSObject data) {
        trace("____________________________ HandleFindAccountGlobal ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        boolean isCheckId = data.getBool(CmdDefine.ModuleCF.IS_CHECK_ID);
        String content = data.getUtfString(CmdDefine.ModuleCF.CONTENT);

        // === Thao tác database và room global ===
        ArrayList<M_Account> lstAcc = new ArrayList<>();
        if(isCheckId){
            try {
                lstAcc.add(C_Account.get(Integer.parseInt(content)));
            }
            catch (NumberFormatException exception){

            }
        }
        else {
            ArrayList<M_Account> allAcc = C_Account.getAll();
            for (int i = 0; i < allAcc.size(); i++) {
                if(allAcc.get(i).name.toUpperCase().startsWith(content.toUpperCase())){
                    lstAcc.add(allAcc.get(i));
                }
            }
        }

        ISFSArray idOnls = new SFSArray();

        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        List<User> lstUser = room.getUserList();
        for (int i = 0; i < lstUser.size(); i++) idOnls.addInt(Integer.parseInt(lstUser.get(i).getName()));

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray accs = new SFSArray();
        for(int i = 0; i < lstAcc.size(); i++){
            accs.addSFSObject(lstAcc.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleCF.ACCOUNTS, accs);

        packet.putSFSArray(CmdDefine.ModuleCF.ID_ONLINES, idOnls);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.FIND_ACCOUNT_GLOBAL);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleSendMessagePrivate(User user, ISFSObject data) {
        trace("____________________________ HandleSendMessagePrivate ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        String message = data.getUtfString(CmdDefine.ModuleCF.MESSAGE);
        int id_rec = data.getInt(CmdDefine.ModuleAccount.ID);

        // === Thao tác database và room global ===
        M_Account account = C_Account.get(id);
        boolean isFriend = C_Account.checkFriend(id, id_rec);

        // Kiểm tra người nhận online không
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        User rec = room.getUserByName(id_rec + "");

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        if(isFriend){
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

            packet.putSFSObject(CmdDefine.ModuleCF.ACCOUNT, account.parse());
            packet.putUtfString(CmdDefine.ModuleCF.MESSAGE, message);
        }
        else {
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.UNFRIENDED);
        }

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.SEND_MESSAGE_PRIVATE);
        trace(packet.getDump());

        this.send(this.module, packet, user);
        if(rec != null && isFriend) this.send(this.module, packet, rec);
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
