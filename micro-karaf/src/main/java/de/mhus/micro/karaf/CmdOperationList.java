package de.mhus.micro.karaf;

import java.util.ArrayList;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.M;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.operation.OperationDescription;
import de.mhus.micro.api.MicroApi;
import de.mhus.micro.api.client.MicroFilter;
import de.mhus.micro.api.client.MicroOperation;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "micro", name = "operation-list", description = "Operations list")
@Service
public class CmdOperationList extends AbstractCmd {

    @Argument(
            index = 0,
            name = "filter",
            required = false,
            description = "",
            multiValued = false)
    String filter;

    @Override
    public Object execute2() throws Exception {
        MicroApi api = M.l(MicroApi.class);
        ConsoleTable out = new ConsoleTable(tblOpt);
        out.setHeaderValues("Path","Version","Labels","Id","Executor");
        ArrayList<MicroOperation> results = new ArrayList<>();
        MicroFilter f = new MicroFilter() {
            @Override
            public boolean matches(OperationDescription desc) {
                return filter == null || desc.getPath().matches(filter);
            }
        };
        api.operations(f, results);
        for (MicroOperation oper : results) {
            OperationDescription desc = oper.getDescription();
            out.addRowValues(desc.getPath(),desc.getVersionString(),desc.getLabels(),desc.getUuid(), oper.getClass().getCanonicalName());
        }
        out.print();
        return null;
    }

}
