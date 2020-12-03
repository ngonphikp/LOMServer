package Base;

import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class QueueExtension {
    protected Zone zone;
    protected SFSExtension sfsExtension;

    public QueueExtension(Zone zone, SFSExtension sfsExtension) {
        this.zone = zone;
        this.sfsExtension = sfsExtension;
    }
}