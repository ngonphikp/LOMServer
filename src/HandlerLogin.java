import Base.BaseExtension;
import Base.BaseHandler;
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

        //trace(dataRec.getDump());

        // Lấy thông tin đăng nhập
        String username = dataRec.getUtfString(CmdDefine.ModuleUser.USERNAME);
        String password = dataRec.getUtfString(CmdDefine.ModuleUser.PASSWORD);
        trace("Đăng nhập: " + username + " / " + password);

        // Kiểm tra tên đăng nhập và mật khẩu
        if(username.equals("admin") && password.equals("123456")){
            trace("Tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

            // Lấy thông tin tài khoản
            M_Account account = new M_Account();
            account.id = 1;
            account.username = username;
            account.password = password;
            account.name = "name";

            // Tạo dữ liệu gửi xuống
            ISFSObject obj = new SFSObject();
            obj.putInt(CmdDefine.ModuleUser.ID, account.id);
            obj.putUtfString(CmdDefine.ModuleUser.USERNAME, account.username);
            obj.putUtfString(CmdDefine.ModuleUser.PASSWORD, account.password);
            obj.putUtfString(CmdDefine.ModuleUser.NAME, account.name);

            packet.putSFSObject(CmdDefine.ModuleUser.ACCOUNT, obj);
        }
        else {
            trace("Không tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.WRONG_USERNAME_OR_PASSWORD);
        }

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.LOGIN);
        dataSend.putSFSObject(CmdDefine.ModuleUser.LOGIN_OUT_DATA, packet);
    }

    private void HandleRegister(ISFSEvent event) {
        ISFSObject dataRec = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);
        ISFSObject dataSend = (ISFSObject) event.getParameter(SFSEventParam.LOGIN_OUT_DATA);
        ISFSObject packet = new SFSObject();

        //trace(dataRec.getDump());

        // Lấy thông tin đăng ký
        String username = dataRec.getUtfString(CmdDefine.ModuleUser.USERNAME);
        String password = dataRec.getUtfString(CmdDefine.ModuleUser.PASSWORD);
        trace("Đăng ký: " + username + " / " + password);

        // Kiểm tra tên đăng nhập và mật khẩu
        if(username.equals("admin") && password.equals("123456")){
            trace("Tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.EXIT_ACCOUNT);
        }
        else {
            trace("Không tồn tại tài khoản");
            packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

            // Thêm tài khoản vào database

            // Lấy thông tin tài khoản
            M_Account account = new M_Account();
            account.id = 1;
            account.username = username;
            account.password = password;
            account.name = "name";

            // Tạo dữ liệu gửi xuống
            ISFSObject obj = new SFSObject();
            obj.putInt(CmdDefine.ModuleUser.ID, account.id);
            obj.putUtfString(CmdDefine.ModuleUser.USERNAME, account.username);
            obj.putUtfString(CmdDefine.ModuleUser.PASSWORD, account.password);
            obj.putUtfString(CmdDefine.ModuleUser.NAME, account.name);

            packet.putSFSObject(CmdDefine.ModuleUser.ACCOUNT, obj);
        }

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.REGISTER);
        dataSend.putSFSObject(CmdDefine.ModuleUser.LOGIN_OUT_DATA, packet);
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
