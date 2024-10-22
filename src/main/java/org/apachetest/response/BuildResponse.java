package org.apachetest.response;

import org.apache.beam.sdk.transforms.DoFn;
import org.apachetest.util.ITableUtilWrapper;
import org.apachetest.util.TableUtilWrapper;

public class BuildResponse extends DoFn<String, String> {

    public ITableUtilWrapper a;

    public BuildResponse(){
        a= new TableUtilWrapper();
    }
    public BuildResponse(ITableUtilWrapper t){
        a = t;
    }

    @ProcessElement
    public void processElement(ProcessContext c, OutputReceiver<String> out)
    {
        String element = c.element();
        String output = response(element);
        out.output(output);
    }

    private String response(String s) {
        var time = a.wrapperMethod();
        return time + s;
    }
}
