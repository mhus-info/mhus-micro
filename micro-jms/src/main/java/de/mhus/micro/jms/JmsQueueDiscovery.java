package de.mhus.micro.jms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.operation.OperationDescription;
import de.mhus.lib.jms.JmsApi;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.micro.core.api.Micro;
import de.mhus.micro.core.impl.AbstractDiscovery;

public class JmsQueueDiscovery extends AbstractDiscovery {

    private HashSet<String> queues = new HashSet<>();
    private List<OperationDescription> descriptions = new ArrayList<>();
    
    @Override
    public void check() {
        //TODO sync timeout
        reload();
    }

    @Override
    public void reload() {
        List<OperationDescription> newDescriptions = new ArrayList<>();
        for (String queue : queues) {
            String conName = MString.beforeIndex(queue, ':');
            String queueName = MString.afterIndex(queue, ':');
            JmsConnection con = M.l(JmsApi.class).getConnection(conName);
            try {
                List<OperationDescription> list = MicroJmsUtil.doGetOperationList(con, queueName);
                for (OperationDescription item : list) {
                    list.add(item);
                }
            } catch (Exception e) {
                log().d(queue,e);
            }
            orderDescriptions(newDescriptions);
        }
        descriptions = newDescriptions;
    }

    protected void orderDescriptions(List<OperationDescription> newDescriptions) {
        newDescriptions.sort(Micro.DESCRIPTION_COMPERATOR);
    }

    @Override
    public Boolean discover(Function<OperationDescription, Boolean> action) {
        for ( OperationDescription desc : descriptions)
            if (!action.apply(desc) )
                return Boolean.FALSE;
        return Boolean.TRUE;
    }

}
