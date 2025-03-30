package org.apachetest.response;

import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.KV;
import org.apachetest.util.ITableUtilWrapper;
import org.apachetest.util.TableUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.threeten.bp.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BuildResponseTest {

    //private  MockedStatic<TableUtil> tableUtilMockedStatic;
    private  String currentTime;

    @Mock
    private  TableUtil tableUtil;

    private  BuildResponse buildResponse;


    @BeforeEach
     void setUp() {
        currentTime  = Instant.now().toString().concat("test");
        tableUtil = Mockito.mock(TableUtil.class);
        Mockito.when(tableUtil.getCurrentTS()).thenReturn(currentTime);
        buildResponse = new BuildResponse(tableUtil);
        //tableUtilMockedStatic.when(TableUtil::getCurrentTS).thenReturn(currentTime);
    }

    @AfterEach
    void tearDown() {
        //tableUtilMockedStatic.close();
    }

    @Test
    public void BuildResponseProcessTest() throws Exception{
       // tableUtilMockedStatic.when(TableUtil::getCurrentTS).thenReturn(currentTime);
        System.out.println("currentTime -> "+ currentTime);
        System.out.println("table time -> "+ tableUtil.getCurrentTS()); // this gives the correct mocked output


        TestPipeline p = TestPipeline.create().enableAbandonedNodeEnforcement(false);

        String s = "Element";

        PCollection<String> input = p.apply(Create.of(s));
/*        ITableUtilWrapper mockITable = mock(TableUtilWrapper.class);
        when(mockITable.wrapperMethod()).thenReturn(currentTime);*/
        PCollection<String> output = input.apply(ParDo.of(buildResponse));
        //PCollection<String> output = input.apply(ParDo.of(new BuildResponseNew()));
        String expectedOutput = currentTime + s;
        PAssert.that(output).containsInAnyOrder(expectedOutput);
        p.run().waitUntilFinish();
    }

    public class BuildResponseNew extends DoFn<String, String> {
        @DoFn.ProcessElement
        public void processElement(ProcessContext c) {
            String timestamp = tableUtil.getCurrentTS();
            System.out.println("Timestamp used -> " + timestamp);
            c.output(timestamp + c.element());
        }
    }
}

