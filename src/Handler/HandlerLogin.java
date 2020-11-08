package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_Account;
import Models.M_Account;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

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
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
            packet.putSFSObject(CmdDefine.ModuleAccount.ACCOUNT, account.parse());
        }
        else {
            trace("Không tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.WRONG_USERNAME_OR_PASSWORD);
        }

        // === Gửi dữ liệu xuống ===
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.LOGIN);
        dataSend.putSFSObject(CmdDefine.ModuleAccount.LOGIN_OUT_DATA, packet);
    }

    private void HandleRegister(ISFSEvent event) {
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
        if(C_Account.get(username) != null){
            trace("Tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.EXIT_ACCOUNT);
        }
        else {
            trace("Không tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

            // === Thao tác database ===
            // Thêm tài khoản
            C_Account.insert(username, password);
            // Lấy thông tin tài khoản
            M_Account account = C_Account.get(username, password);
            packet.putSFSObject(CmdDefine.ModuleAccount.ACCOUNT, account.parse());
        }

        // === Gửi dữ liệu xuống ===
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.REGISTER);
        dataSend.putSFSObject(CmdDefine.ModuleAccount.LOGIN_OUT_DATA, packet);
    }

    private void OnUserLogout(ISFSEvent event) {
        trace("____________________________ OnUserLogout ____________________________");
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
