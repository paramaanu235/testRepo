package org.apachetest.response;

import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.KV;
import org.apachetest.util.TableUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.threeten.bp.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BuildResponseTest {

    private static MockedStatic<TableUtil> tableUtilMockedStatic;
    private static String currentTime;
    @BeforeAll
    static void setUp() {
        currentTime  = Instant.now().toString().concat("test");
        tableUtilMockedStatic = Mockito.mockStatic(TableUtil.class);
        tableUtilMockedStatic.when(TableUtil::getCurrentTS).thenReturn(currentTime);
    }

    @AfterAll
    static void tearDown() {
        tableUtilMockedStatic.close();
    }

    @Test
    public void BuildResponseProcessTest() throws Exception{
        tableUtilMockedStatic.when(TableUtil::getCurrentTS).thenReturn(currentTime);
        System.out.println("currentTime -> "+ currentTime);
        System.out.println("table time -> "+ TableUtil.getCurrentTS()); // this gives the correct mocked output

        TestPipeline p = TestPipeline.create().enableAbandonedNodeEnforcement(false);

        String s = "Element";

        PCollection<String> input = p.apply(Create.of(s));

        PCollection<String> output = input.apply(ParDo.of(new BuildResponseNew()));
        assertNotNull(output);

        String expectedOutput = currentTime + s;
        PAssert.that(output).containsInAnyOrder(expectedOutput);

        p.run().waitUntilFinish(); // this when runs gives the incorrect output
    }

    public static class BuildResponseNew extends DoFn<String, String> {
        @DoFn.ProcessElement
        public void processElement(ProcessContext c) {
            String timestamp = TableUtil.getCurrentTS();
            System.out.println("Timestamp used -> " + timestamp);
            c.output(timestamp + c.element());
        }
    }
}

