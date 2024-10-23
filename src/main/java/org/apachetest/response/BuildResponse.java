package org.apachetest.response;

import org.apache.beam.sdk.transforms.DoFn;
import org.apachetest.util.ITableUtilWrapper;
import org.apachetest.util.TableUtil;

public class BuildResponse extends DoFn<String, String> {

    private final TableUtil tableUtil;

    public BuildResponse(TableUtil tableUtil) {
        this.tableUtil = tableUtil;
    }


    @ProcessElement
    public void processElement(ProcessContext c, OutputReceiver<String> out)
    {
        String element = c.element();
        String output = response(element);
        out.output(output);
    }

    private String response(String s) {
        var time = tableUtil.getCurrentTS();
        return time + s;
    }
}
