package org.apachetest.response;

import org.apache.beam.sdk.transforms.DoFn;
import org.apachetest.util.TableUtil;

public class BuildResponse extends DoFn<String, String> {


    @ProcessElement
    public void processElement(ProcessContext c, OutputReceiver<String> out)
    {
        String element = c.element();
        String output = response(element);
        out.output(output);
    }

    private String response(String s) {
        var time = TableUtil.getCurrentTS();
        return time + s;
    }
}
