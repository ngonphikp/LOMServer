package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Base.RoomManage;
import Controls.C_Account;
import Controls.C_Guild;
import Models.M_Account;
import Util.C_Util;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.ISFSEventParam;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.List;

public class HandlerLogin extends BaseHandler {

    public HandlerLogin(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {

        }
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {
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

    private void HandleLogin(ISFSEvent event) {
        trace("____________________________ HandleLogin ____________________________");

        ISFSObject dataRec = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
        ISFSObject dataSend = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
        ISFSObject packet = new SFSObject();

        // === Đọc dữ liệu gửi lên ===
        trace(dataRec.getDump());

        // Lấy thông tin đăng nhập
        String username = dataRec.getUtfString(CmdDefine.ModuleAccount.USERNAME);
        String password = dataRec.getUtfString(CmdDefine.ModuleAccount.PASSWORD);
        trace("Đăng nhập: " + username + " / " + password);

        // === Thao tác database ===
        // Lấy thông tin tài khoản theo tên đăng nhập và mật khẩu
        M_Account account = C_Account.get(username, password);
        if(account != null){
            trace("Tồn tại tài khoản");

            // === Thao tác room global ===
            // Lấy list user Online
            Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
            List<User> lstUser = room.getUserList();
            boolean isOnl = false;
            for (int i = 0; i < lstUser.size(); i++) {
                if(account.id == Integer.parseInt(lstUser.get(i).getName())){
                    trace("Tài khoản đang đăng nhập ở nơi khác");
                    packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.ACCOUNT_LOGON);
                    isOnl = true;
                    break;
                }
            }

            if(!isOnl){
                packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
                packet.putSFSObject(CmdDefine.ModuleAccount.ACCOUNT, account.parse());
            }
        }
        else {
            trace("Không tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.WRONG_USERNAME_OR_PASSWORD);
        }

        // === Gửi dữ liệu xuống ===
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.LOGIN);
        trace(packet.getDump());
        dataSend.putSFSObject(CmdDefine.ModuleAccount.LOGIN_OUT_DATA, packet);
    }

    private void HandleRegister(ISFSEvent event) {
        trace("____________________________ HandleRegister ____________________________");

        ISFSObject dataRec = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
        ISFSObject dataSend = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
        ISFSObject packet = new SFSObject();

        // === Đọc dữ liệu gửi lên ===
        trace(dataRec.getDump());

        // Lấy thông tin đăng ký
        String username = dataRec.getUtfString(CmdDefine.ModuleAccount.USERNAME);
        String password = dataRec.getUtfString(CmdDefine.ModuleAccount.PASSWORD);
        trace("Đăng ký: " + username + " / " + password);

        // Kiểm tra username
        if(C_Account.getByUserName(username) != null){
            trace("Tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.EXIT_ACCOUNT);
        }
        else {
            trace("Không tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

            // === Thao tác database ===
            // Thêm tài khoản
            int id = C_Account.insert(username, password);
            // Lấy thông tin tài khoản
            M_Account account = C_Account.get(id);
            packet.putSFSObject(CmdDefine.ModuleAccount.ACCOUNT, account.parse());
        }

        // === Gửi dữ liệu xuống ===
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.REGISTER);
        trace(packet.getDump());
        dataSend.putSFSObject(CmdDefine.ModuleAccount.LOGIN_OUT_DATA, packet);
    }

    private void OnUserLogout(ISFSEvent event) {
        trace("____________________________ OnUserLogout ____________________________");

        // === Thao tác với room global
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        User user = (User) event.getParameter(SFSEventParam.USER);
        RoomManage.userOutRoom(user, room);

        // === Thao tác với room guild
        String keyGuild = C_Guild.getKey(Integer.parseInt(user.getName()));
        if(keyGuild != null){
            int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, keyGuild);
            Room roomG = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Guild + id_guild);
            RoomManage.userOutRoom(user, roomG);

            if(roomG.getUserList().size() == 0) RoomManage.removeRoom(this.getParentExtension(), roomG);
        }
    }

    @Override
    protected void initHandlerClientRequest() {
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addEventHandler(SFSEventType.USER_LOGIN, this);
        this.extension.addEventHandler(SFSEventType.USER_LOGOUT, this);
    }
}
