import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class MathHandler extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User user, ISFSObject params){
        System.out.println("MathHandler");
        System.out.println(params.getDump());

        int n1 = params.getInt("n1");
        int n2 = params.getInt("n2");

        ISFSObject rtn = new SFSObject();
        rtn.putInt("sum", n1 + n2);

        MyExtension extension = (MyExtension) getParentExtension();
        extension.send("math", rtn, user);
    }
}
